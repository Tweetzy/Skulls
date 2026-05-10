/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.manager;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.PlacedSkull;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.impl.TexturedSkull;
import ca.tweetzy.skulls.settings.Settings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Date Created: April 04 2022
 * Time Created: 8:48 p.m.
 *
 * @author Kiran Hart
 */
public final class SkullManager implements Manager {

	private final Random random = new Random();

	@Getter
	@Setter
	private boolean downloading = false;

	@Getter
	@Setter
	private boolean loading = false;

	@Getter
	private final ConcurrentHashMap<Integer, Skull> skulls = new ConcurrentHashMap<>();

	@Getter
	private final Set<Integer> idList = Collections.synchronizedSet(new HashSet<>());

	@Getter
	private final Map<Location, PlacedSkull> placedSkulls = new ConcurrentHashMap<>();

	// Cached offline players list
	private List<OfflinePlayer> cachedOfflinePlayers = new ArrayList<>();
	private long lastOfflinePlayersCacheTime = 0;
	private static final long DEFAULT_CACHE_REFRESH_INTERVAL = 300000L; // 5 minutes

	// ItemStack cache for performance
	private final Map<Integer, ItemStack> itemStackCache = new ConcurrentHashMap<>();
	private static final int DEFAULT_MAX_CACHE_SIZE = 5000;

	public List<OfflinePlayer> getOnlineOfflinePlayers() {
		// If caching is disabled, use the old method
		if (!Settings.OFFLINE_PLAYERS_CACHE_ENABLED.getBoolean()) {
			return getOnlineOfflinePlayersUncached();
		}

		long now = System.currentTimeMillis();
		long refreshInterval = Settings.OFFLINE_PLAYERS_CACHE_REFRESH_INTERVAL.getInt() * 1000L; // Convert seconds to milliseconds
		if (refreshInterval <= 0) refreshInterval = DEFAULT_CACHE_REFRESH_INTERVAL;

		// Check if cache needs refresh
		boolean needsRefresh = cachedOfflinePlayers.isEmpty() || 
		                      (now - lastOfflinePlayersCacheTime) > refreshInterval;

		if (needsRefresh) {
			// Refresh cache asynchronously
			refreshOfflinePlayersCacheAsync();
			
			// Return current cache (or minimal list) while refreshing
			if (cachedOfflinePlayers.isEmpty()) {
				// First time - return online players only
				return new ArrayList<>(Bukkit.getOnlinePlayers());
			}
		}

		// Return cached list with online players added/updated
		final List<OfflinePlayer> result = new ArrayList<>(cachedOfflinePlayers);
		final Set<java.util.UUID> onlineUuids = new java.util.HashSet<>();
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			onlineUuids.add(player.getUniqueId());
			// Remove old entry if exists and add current online player
			result.removeIf(p -> p.getUniqueId().equals(player.getUniqueId()));
			result.add(player);
		});

		// Apply max players limit if configured
		int maxPlayers = Settings.MAX_OFFLINE_PLAYERS_TO_LOAD.getInt();
		if (maxPlayers > 0 && result.size() > maxPlayers) {
			// Keep online players first, then limit offline players
			List<OfflinePlayer> online = new ArrayList<>();
			List<OfflinePlayer> offline = new ArrayList<>();
			
			for (OfflinePlayer player : result) {
				if (player.isOnline()) {
					online.add(player);
				} else {
					offline.add(player);
				}
			}
			
			// Keep all online players + limited offline players
			result.clear();
			result.addAll(online);
			int remaining = maxPlayers - online.size();
			if (remaining > 0 && offline.size() > remaining) {
				result.addAll(offline.subList(0, remaining));
			} else {
				result.addAll(offline);
			}
		}

		return result;
	}

	/**
	 * Get players without caching (legacy method)
	 */
	private List<OfflinePlayer> getOnlineOfflinePlayersUncached() {
		final List<OfflinePlayer> players = new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers()));
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (players.stream().anyMatch(target -> target.getUniqueId().equals(player.getUniqueId()))) return;
			players.add(player);
		});

		return players;
	}

	/**
	 * Refresh offline players cache asynchronously
	 */
	private void refreshOfflinePlayersCacheAsync() {
		Bukkit.getServer().getScheduler().runTaskAsynchronously(Skulls.getInstance(), () -> {
			try {
				// Load offline players asynchronously
				OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
				
				// Switch back to main thread to update cache
				Bukkit.getServer().getScheduler().runTask(Skulls.getInstance(), () -> {
					cachedOfflinePlayers = new ArrayList<>(Arrays.asList(offlinePlayers));
					lastOfflinePlayersCacheTime = System.currentTimeMillis();
				});
			} catch (Exception e) {
				// On error, just update timestamp to prevent constant retries
				lastOfflinePlayersCacheTime = System.currentTimeMillis();
			}
		});
	}

	public Skull getSkull(final int id) {
		return this.skulls.getOrDefault(id, null);
	}

	public List<Skull> getSkulls(BaseCategory category) {
		String categoryId = category.getId();
		return this.skulls.values().stream()
				.filter(skull -> skull.getCategory().equalsIgnoreCase(categoryId))
				.collect(Collectors.toList());
	}

	public List<Skull> getSkulls(String category) {
		BaseCategory baseCategory = BaseCategory.getById(category);
		if (baseCategory != null) {
			// Use cached category ID
			String categoryId = baseCategory.getId();
			return this.skulls.values().stream()
					.filter(skull -> skull.getCategory().equalsIgnoreCase(categoryId))
					.collect(Collectors.toList());
		}

		// For custom categories, cache the skull IDs list
		ca.tweetzy.skulls.api.interfaces.Category customCategory = Skulls.getCategoryManager().findCategory(category);
		if (customCategory == null) {
			return new ArrayList<>();
		}
		java.util.Set<Integer> categorySkullIds = new java.util.HashSet<>(customCategory.getSkulls());
		return this.skulls.values().stream()
				.filter(skull -> categorySkullIds.contains(skull.getId()))
				.collect(Collectors.toList());
	}

	public Skull getRandomSkull() {
		final List<Skull> enabledSkulls = getSkulls().values().stream()
				.filter(skull -> {
					BaseCategory category = BaseCategory.getById(skull.getCategory());
					return category != null && category.isEnabled();
				})
				.collect(Collectors.toList());
		if (enabledSkulls.isEmpty()) {
			return null;
		}
		return enabledSkulls.get(random.nextInt(enabledSkulls.size()));
	}

	public Skull getRandomAllowedSkull(Player player) {
		// Cache permission prefix to avoid repeated string operations
		final List<Skull> enabledSkulls = getSkulls().values().stream()
				.filter(skull -> {
					BaseCategory category = BaseCategory.getById(skull.getCategory());
					if (category == null || !category.isEnabled() || skull.isBlocked()) {
						return false;
					}
					// Cache permission string building
					String perm = "skulls.category." + category.getId().toLowerCase().replace(" ", "").replace("&", "");
					return player.hasPermission(perm);
				})
				.collect(Collectors.toList());
		if (enabledSkulls.isEmpty()) {
			return null;
		}
		return enabledSkulls.get(random.nextInt(enabledSkulls.size()));
	}

	public List<Skull> getSkullsBySearch(Player player, String phraseOriginal) {
		String phrase = ChatColor.stripColor(phraseOriginal);

		int id = -1;
		if (phrase.startsWith("id:")) {
			String[] parts = phrase.split(":", 2);
			if (parts.length > 1 && NumberUtils.isNumber(parts[1])) {
				id = Integer.parseInt(parts[1]);
			}
		}

		if (id != -1) {
			Skull skull = getSkull(id);
			return skull != null ? Collections.singletonList(skull) : Collections.emptyList();
		}

		return this.skulls.values().stream()
				.filter(skull -> {
					BaseCategory category = BaseCategory.getById(skull.getCategory());
					if (category == null || !category.isEnabled()) {
						return false;
					}
					
					// Check permission
					String perm = "skulls.category." + category.getId().toLowerCase().replace(" ", "").replace("&", "");
					if (!player.hasPermission(perm)) {
						return false;
					}
					
					// Check matches
					return Common.match(phrase, skull.getName()) || 
					       Common.match(phrase, skull.getCategory()) || 
					       skull.getTags().stream().anyMatch(tag -> Common.match(phrase, tag));
				})
				.collect(Collectors.toList());
	}

	public List<Skull> getSkulls(List<Integer> ids) {
		final List<Skull> results = new ArrayList<>();

		for (Integer id : ids) {
			final Skull found = this.skulls.getOrDefault(id, null);
			if (found != null)
				results.add(found);
		}

		return results;
	}

	public long getSkullCount(String category) {
		// Use direct iteration for counting - more efficient than stream
		long count = 0;
		for (Skull skull : this.skulls.values()) {
			if (skull.getCategory().equalsIgnoreCase(category)) {
				count++;
			}
		}
		return count;
	}

	public ItemStack getSkullItem(final int id) {
		// Use cache if enabled
		if (Settings.ITEMSTACK_CACHE_ENABLED.getBoolean()) {
			ItemStack cached = itemStackCache.get(id);
			if (cached != null) {
				// Return a clone to prevent modification of cached item
				return cached.clone();
			}
		}

		synchronized (this.skulls) {
			final Skull skull = getSkull(id);
			if (skull == null) {
				return QuickItem.of(CompMaterial.PLAYER_HEAD).make();
			}

			ItemStack item = skull.getItemStack();

			// Cache the item if caching is enabled and cache isn't full
			if (Settings.ITEMSTACK_CACHE_ENABLED.getBoolean()) {
				int maxCacheSize = Settings.ITEMSTACK_CACHE_SIZE.getInt();
				if (maxCacheSize <= 0) maxCacheSize = DEFAULT_MAX_CACHE_SIZE;

				// Only cache if we're under the limit
				if (itemStackCache.size() < maxCacheSize) {
					itemStackCache.put(id, item.clone());
				}
			}

			return item;
		}
	}

	public void addPlacedSkull(@NonNull final PlacedSkull placedSkull) {
		Skulls.getDataManager().insertPlacedSkull(placedSkull, (error, inserted) -> {
			if (error == null)
				this.placedSkulls.put(inserted.getLocation(), inserted);
		});
	}

	public void removePlacedSkull(@NonNull final PlacedSkull placedSkull) {
		Skulls.getDataManager().deletePlacedSkull(placedSkull.getId(), (error, deleted) -> {
			if (error == null)
				this.placedSkulls.remove(placedSkull.getLocation());
		});
	}

	/**
	 * Invalidate ItemStack cache for a specific skull
	 * Call this when skull data changes (name, price, blocked status, etc.)
	 */
	public void invalidateItemStackCache(int skullId) {
		itemStackCache.remove(skullId);
	}

	/**
	 * Clear the entire ItemStack cache
	 */
	public void clearItemStackCache() {
		itemStackCache.clear();
	}

	/**
	 * Get cache statistics
	 */
	public int getItemStackCacheSize() {
		return itemStackCache.size();
	}

	/**
	 * Get cached ItemStack (internal use)
	 */
	public ItemStack getCachedItemStack(int skullId) {
		return itemStackCache.get(skullId);
	}

	/**
	 * Cache an ItemStack (internal use)
	 */
	public void cacheItemStack(int skullId, ItemStack item) {
		itemStackCache.put(skullId, item);
	}

	/**
	 * For internal use only
	 */

	private void checkAndFixDatabase() {
		Bukkit.getServer().getScheduler().runTaskAsynchronously(Skulls.getInstance(), () -> {
			final Map<Integer, Skull> heads = new HashMap<>();

			Common.log("&r&aRunning database check :)");

			final List<Skull> downloaded = performHeadDownload(true);
			downloaded.forEach(skull -> {
				if (!this.skulls.containsKey(skull.getId()))
					heads.put(skull.getId(),skull);
			});

			int oldSize = this.skulls.size();
			this.skulls.putAll(heads);
			int newSize = this.skulls.size();

			if (newSize > oldSize) {
				Skulls.getDataManager().insertSkulls(new ArrayList<>(heads.values()));
				Common.log("&r&eFound some new skulls, saving them now :D");
			} else {
				Common.log("&r&aEverything looks up-to-date, no issues found. Enjoy!");
			}
		});
	}

	public void downloadHeads() {
		setDownloading(true);
		Bukkit.getServer().getScheduler().runTaskAsynchronously(Skulls.getInstance(), () -> {
			Common.log("&r&aBeginning initial download, it may take some time to insert all the skulls into the data file!");

			final List<Skull> heads = new ArrayList<>(performHeadDownload(false));

			heads.forEach(skull -> this.skulls.putIfAbsent(skull.getId(), skull));
//			this.idList.addAll(heads.stream().map(Skull::getId).toList());
			Skulls.getDataManager().insertSkulls(heads);
		});
	}

	public List<Skull> performHeadDownload(boolean silentDownload) {
		final List<Skull> skulls = new ArrayList<>();

		 final String DOWNLOAD_URL = Settings.SKULLS_DATA_SOURCE_URL.getString();

		try {
			long start = System.nanoTime();
			final JsonArray json = DOWNLOAD_URL.startsWith("file://")
					? getJsonFromFile(DOWNLOAD_URL.substring(7))
					: getJsonFromUrl(DOWNLOAD_URL);
			json.forEach(jsonElement -> {
				final JsonObject jsonObject = jsonElement.getAsJsonObject();
				final BaseCategory category = BaseCategory.getById(replace(jsonObject.get("category").toString()));

				final Skull head = new TexturedSkull(
						Integer.parseInt(replace(jsonObject.get("id").toString())),
						replace(jsonObject.get("name").toString()),
						replace(jsonObject.get("category").toString()),
						Arrays.asList(replace(jsonObject.get("tags").toString()).split(",")),
						replace(jsonObject.get("texture").toString()),
						category.getDefaultPrice(),
						false
				);

				skulls.add(head);
				this.idList.add(head.getId());

			});

			if (!silentDownload)
				Common.log("&aDownloaded &e" + skulls.size() + " &askulls in &f" + String.format("%,.3f", (System.nanoTime() - start) / 1e+6) + "&ems");
		} catch (Exception e) {
			if (!silentDownload) {
				Common.log("&cCould not download skulls, try again later. If the issue persist, join the Support Server");
				e.printStackTrace();
			}
		}

		return skulls;
	}

	private JsonArray getJsonFromUrl(final String url) throws IOException {
		final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(30_000);
		connection.setReadTimeout(600_000); // 10 minutes for large files (e.g. 40MB)
		connection.setRequestMethod("GET");

		try (InputStream inputStream = connection.getInputStream();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			final StringBuilder builder = new StringBuilder(8192);
			final char[] buffer = new char[65536];
			int len;
			while ((len = reader.read(buffer)) != -1) {
				builder.append(buffer, 0, len);
			}
			return JsonParser.parseString(builder.toString()).getAsJsonArray();
		} finally {
			connection.disconnect();
		}
	}

	private JsonArray getJsonFromFile(String filePath) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(filePath)));
		return JsonParser.parseString(content).getAsJsonArray();
	}

	private String replace(String in) {
		return in.replace("\"", "");
	}

	@Override
	public void load() {
		setLoading(true);
		long start = System.nanoTime();

		Skulls.getDataManager().getSkulls((error, all) -> {
			if (error != null) {
				error.printStackTrace();
				return;
			}

			all.forEach(skull -> this.skulls.put(skull.getId(), skull));
//			this.idList.addAll(all.stream().map(Skull::getId).toList());

			if (this.skulls.isEmpty()) {
				Common.log("&cCould not find any skulls, attempting to redownload them!");
				downloadHeads();
			} else {
				Common.log("&aLoaded &e" + this.skulls.size() + " &askulls in &f" + String.format("%,.3f", (System.nanoTime() - start) / 1e+6) + "&ams");
				setLoading(false);
				checkAndFixDatabase();
			}
		});

		if (Settings.SKULL_TRACKING.getBoolean()) {
			Skulls.getDataManager().getPlacedSkulls((error, all) -> {
				if (error != null) {
					error.printStackTrace();
					return;
				}

				all.forEach(placedSkull -> this.placedSkulls.put(placedSkull.getLocation(), placedSkull));
			});
		}
	}
}

