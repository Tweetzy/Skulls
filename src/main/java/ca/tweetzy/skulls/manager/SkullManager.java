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
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	public List<OfflinePlayer> getOnlineOfflinePlayers() {
		final List<OfflinePlayer> players = new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers()));
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (players.stream().anyMatch(target -> target.getUniqueId().equals(player.getUniqueId()))) return;
			players.add(player);
		});

		return players;
	}

	public Skull getSkull(final int id) {
		return this.skulls.getOrDefault(id, null);
	}

	public List<Skull> getSkulls(BaseCategory category) {
		return this.skulls.values().stream().filter(skull -> skull.getCategory().equalsIgnoreCase(category.getId())).collect(Collectors.toList());
	}

	public List<Skull> getSkulls(String category) {
		if (BaseCategory.getById(category) != null)

			return this.skulls.values().stream().filter(skull -> skull.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());

		return this.skulls.values().stream().filter(skull -> Skulls.getCategoryManager().findCategory(category).getSkulls().contains(skull.getId())).collect(Collectors.toList());
	}

	public Skull getRandomSkull() {
		final List<Skull> enabledSkulls = getSkulls().values().stream().filter(skull -> BaseCategory.getById(skull.getCategory()).isEnabled()).toList();
		return enabledSkulls.get(random.nextInt(enabledSkulls.size()));
	}

	public List<Skull> getSkullsBySearch(Player player, String phrase) {
		int id = -1;
		if (phrase.startsWith("id:")) {
			if (NumberUtils.isNumber(phrase.split(":")[1])) {
				id = Integer.parseInt(phrase.split(":")[1]);
			}
		}

		if (id != -1)
			return Collections.singletonList(getSkull(id));

		return this.skulls.values().stream().filter(skull -> player.hasPermission("skulls.category." + BaseCategory.getById(skull.getCategory()).getId().toLowerCase().replace(" ", "").replace("&", "")) && BaseCategory.getById(skull.getCategory()).isEnabled() && (Common.match(phrase, skull.getName()) || Common.match(phrase, skull.getCategory()) || skull.getTags().stream().anyMatch(tag -> Common.match(phrase, tag)))).collect(Collectors.toList());
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
		return this.skulls.values().stream().filter(skull -> skull.getCategory().equalsIgnoreCase(category)).count();
	}

	public ItemStack getSkullItem(final int id) {
		synchronized (this.skulls) {
			final Skull skull = getSkull(id);
			return skull == null ? QuickItem.of(CompMaterial.PLAYER_HEAD).make() : skull.getItemStack();
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
			if (!silentDownload)
				Common.log("&cCould not download skulls, try again later. If the issue persist, join the Support Server");
		}

		return skulls;
	}

	private JsonArray getJsonFromUrl(final String url) throws IOException {
		final InputStream inputStream = new URL(url).openStream();
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		final StringBuilder builder = new StringBuilder();

		int character;
		while ((character = bufferedReader.read()) != -1) {
			builder.append((char) character);
		}

		final JsonParser parser = new JsonParser();
		return (JsonArray) parser.parse(builder.toString());
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
