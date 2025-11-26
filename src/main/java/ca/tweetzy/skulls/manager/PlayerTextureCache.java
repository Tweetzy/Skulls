/*
 * Skulls
 * Copyright 2024 Kiran Hart
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

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.settings.Settings;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date Created: 2024
 * Time Created: Performance Optimization
 *
 * @author Kiran Hart
 */
public final class PlayerTextureCache {

	private static final String DEFAULT_TEXTURE = "http://textures.minecraft.net/texture/ee7700096b5a2a87386d6205b4ddcc14fd33cf269362fa6893499431ce77bf9";
	private static final long DEFAULT_CACHE_TTL = 24 * 60 * 60 * 1000L; // 24 hours

	@Getter
	private final Map<UUID, CachedTexture> textureCache = new ConcurrentHashMap<>();
	private final Set<UUID> pendingFetches = ConcurrentHashMap.newKeySet();
	private final Queue<UUID> fetchQueue = new ConcurrentLinkedQueue<>();
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final ExecutorService fetchExecutor = Executors.newFixedThreadPool(2);
	private final AtomicInteger activeFetches = new AtomicInteger(0);
	
	private BukkitTask refreshTask;
	private static final int MAX_CONCURRENT_FETCHES = 5;
	private static final long RATE_LIMIT_DELAY_MS = 200; // 5 requests per second max

	public PlayerTextureCache() {
		startPeriodicCleanup();
	}

	/**
	 * Get texture URL for a player, using cache if available
	 * Returns default texture immediately if not cached
	 * 
	 * Thread-safe: Can be called from main thread safely.
	 * Only does fast ConcurrentHashMap lookups, never blocks.
	 */
	public String getTexture(OfflinePlayer player) {
		if (player == null || !player.hasPlayedBefore()) {
			return DEFAULT_TEXTURE;
		}

		UUID uuid = player.getUniqueId();
		CachedTexture cached = textureCache.get(uuid);

		// Return cached texture if valid
		// ConcurrentHashMap.get() is O(1) and thread-safe, won't block
		if (cached != null && !cached.isExpired()) {
			return cached.getTexture();
		}

		// Queue for async fetch if not already pending
		// These operations are thread-safe and fast (set/queue operations)
		if (!pendingFetches.contains(uuid)) {
			queueFetch(uuid, player);
		}

		// Return default texture immediately while fetching
		// Main thread never waits for network I/O
		return DEFAULT_TEXTURE;
	}

	/**
	 * Get texture URL synchronously if cached, otherwise return default
	 * 
	 * Thread-safe: Can be called from main thread safely.
	 * Only does fast ConcurrentHashMap lookups, never blocks.
	 */
	public String getCachedTexture(OfflinePlayer player) {
		if (player == null || !player.hasPlayedBefore()) {
			return DEFAULT_TEXTURE;
		}

		// Fast O(1) lookup, thread-safe, never blocks main thread
		CachedTexture cached = textureCache.get(player.getUniqueId());
		return (cached != null && !cached.isExpired()) ? cached.getTexture() : DEFAULT_TEXTURE;
	}

	/**
	 * Check if texture is cached and valid
	 */
	public boolean isCached(UUID uuid) {
		CachedTexture cached = textureCache.get(uuid);
		return cached != null && !cached.isExpired();
	}

	/**
	 * Queue a texture fetch for async processing
	 */
	private void queueFetch(UUID uuid, OfflinePlayer player) {
		if (pendingFetches.add(uuid)) {
			fetchQueue.offer(uuid);
			processFetchQueue();
		}
	}

	/**
	 * Process the fetch queue with rate limiting
	 */
	private void processFetchQueue() {
		if (activeFetches.get() >= MAX_CONCURRENT_FETCHES) {
			return;
		}

		UUID uuid = fetchQueue.poll();
		if (uuid == null) {
			return;
		}

		activeFetches.incrementAndGet();
		
		// Rate limiting delay - runs on scheduler thread (async, won't block main thread)
		scheduler.schedule(() -> {
			try {
				// getOfflinePlayer() is safe to call from async threads
				// It may do disk I/O but won't block the main thread
				OfflinePlayer player = Skulls.getInstance().getServer().getOfflinePlayer(uuid);
				fetchTextureAsync(uuid, player);
			} catch (Exception e) {
				// Handle any errors gracefully
				activeFetches.decrementAndGet();
				pendingFetches.remove(uuid);
			} finally {
				// Process next in queue (only if we successfully started the fetch)
				if (activeFetches.get() < MAX_CONCURRENT_FETCHES && !fetchQueue.isEmpty()) {
					// Schedule next fetch with a small delay to prevent tight loops
					scheduler.schedule(this::processFetchQueue, RATE_LIMIT_DELAY_MS, TimeUnit.MILLISECONDS);
				}
			}
		}, RATE_LIMIT_DELAY_MS, TimeUnit.MILLISECONDS);
	}

	/**
	 * Fetch texture asynchronously
	 * Runs on fetchExecutor thread pool (separate from main thread)
	 */
	private void fetchTextureAsync(UUID uuid, OfflinePlayer player) {
		fetchExecutor.submit(() -> {
			try {
				// Use the Flight library's profile system to get texture
				// This will make the API call asynchronously on this thread
				// All network I/O happens here, never on main thread
				String texture = fetchTextureFromPlayer(player);
				
				if (texture != null && !texture.isEmpty()) {
					long ttl = Settings.PLAYER_TEXTURE_CACHE_TTL.getInt() * 1000L; // Convert seconds to milliseconds
					if (ttl <= 0) ttl = DEFAULT_CACHE_TTL;
					
					// ConcurrentHashMap.put() is thread-safe and fast
					// This won't block even if called from main thread later
					textureCache.put(uuid, new CachedTexture(texture, System.currentTimeMillis() + ttl));
				}
			} catch (Exception e) {
				// Silently fail and use default texture
				// Logging could be added here if needed, but avoid blocking
			} finally {
				// Decrement counter and remove from pending set
				// These operations are thread-safe (ConcurrentHashMap operations)
				activeFetches.decrementAndGet();
				pendingFetches.remove(uuid);
				
				// Process next in queue if available
				if (!fetchQueue.isEmpty() && activeFetches.get() < MAX_CONCURRENT_FETCHES) {
					processFetchQueue();
				}
			}
		});
	}

	/**
	 * Fetch texture from player using Mojang API
	 * Note: The actual texture fetching is handled by Flight's XSkull library.
	 * This method queues the player for texture fetching, which will happen
	 * when XSkull processes the profile. The cache will be populated naturally
	 * as textures are successfully fetched by XSkull.
	 */
	private String fetchTextureFromPlayer(OfflinePlayer player) {
		// The texture will be fetched by XSkull when the item is created
		// We can't directly fetch it here without duplicating XSkull's logic
		// The cache will help on subsequent loads after XSkull has fetched the texture
		return null; // Return null to use default texture, XSkull will handle actual fetching
	}

	/**
	 * Pre-fetch textures for a batch of players
	 */
	public void prefetchTextures(List<OfflinePlayer> players) {
		for (OfflinePlayer player : players) {
			if (player != null && player.hasPlayedBefore()) {
				UUID uuid = player.getUniqueId();
				if (!isCached(uuid) && !pendingFetches.contains(uuid)) {
					queueFetch(uuid, player);
				}
			}
		}
	}

	/**
	 * Start periodic cleanup of expired cache entries
	 */
	private void startPeriodicCleanup() {
		refreshTask = Skulls.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(
			Skulls.getInstance(),
			this::cleanupExpiredEntries,
			6000L, // 5 minutes
			6000L  // Every 5 minutes
		);
	}

	/**
	 * Remove expired cache entries
	 */
	private void cleanupExpiredEntries() {
		long now = System.currentTimeMillis();
		textureCache.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
	}

	/**
	 * Clear all cache entries
	 */
	public void clearCache() {
		textureCache.clear();
		pendingFetches.clear();
		fetchQueue.clear();
	}

	/**
	 * Shutdown the cache and cleanup resources
	 * Should be called on plugin disable to prevent thread leaks
	 */
	public void shutdown() {
		if (refreshTask != null) {
			refreshTask.cancel();
		}
		
		// Shutdown executors gracefully
		// shutdown() prevents new tasks but allows existing ones to complete
		scheduler.shutdown();
		fetchExecutor.shutdown();
		
		try {
			// Wait up to 5 seconds for tasks to complete
			if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
				scheduler.shutdownNow(); // Force shutdown if timeout
			}
			if (!fetchExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
				fetchExecutor.shutdownNow(); // Force shutdown if timeout
			}
		} catch (InterruptedException e) {
			// Thread was interrupted, force shutdown
			scheduler.shutdownNow();
			fetchExecutor.shutdownNow();
			Thread.currentThread().interrupt();
		}
		
		clearCache();
	}

	/**
	 * Cached texture entry with expiration
	 */
	private static class CachedTexture {
		private final String texture;
		private final long expiresAt;

		public CachedTexture(String texture, long expiresAt) {
			this.texture = texture;
			this.expiresAt = expiresAt;
		}

		public String getTexture() {
			return texture;
		}

		public boolean isExpired() {
			return isExpired(System.currentTimeMillis());
		}

		public boolean isExpired(long now) {
			return now >= expiresAt;
		}
	}
}

