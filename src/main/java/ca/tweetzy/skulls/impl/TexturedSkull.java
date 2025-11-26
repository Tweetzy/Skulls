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

package ca.tweetzy.skulls.impl;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.profiles.builder.XSkull;
import ca.tweetzy.flight.utils.profiles.objects.ProfileInputType;
import ca.tweetzy.flight.utils.profiles.objects.Profileable;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Date Created: April 05 2022
 * Time Created: 1:51 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class TexturedSkull implements Skull {

	private final int id;
	private String name;
	private final String category;
	private final List<String> tags;
	private final String texture;
	private double price;
	private boolean blocked;

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getCategory() {
		return this.category;
	}

	@Override
	public List<String> getTags() {
		return this.tags;
	}

	@Override
	public String getTexture() {
		return this.texture;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

	@Override
	public void setBlocked(boolean blocked) {
		if (this.blocked != blocked) {
			this.blocked = blocked;
			// Invalidate cache when blocked status changes
			Skulls.getSkullManager().invalidateItemStackCache(this.id);
		}
	}

	@Override
	public boolean isBlocked() {
		return this.blocked;
	}

	@Override
	public void setName(String name) {
		if (!this.name.equals(name)) {
			this.name = name;
			// Invalidate cache when name changes (affects ItemStack display name)
			Skulls.getSkullManager().invalidateItemStackCache(this.id);
		}
	}

	@Override
	public void setPrice(double price) {
		// Price change doesn't affect ItemStack appearance, so no cache invalidation needed
		this.price = price;
	}

	@Override
	public ItemStack getItemStack() {
		// Use cache if enabled - this provides significant performance improvement
		if (Settings.ITEMSTACK_CACHE_ENABLED.getBoolean()) {
			ItemStack cached = Skulls.getSkullManager().getCachedItemStack(this.id);
			if (cached != null) {
				// Return a clone to prevent modification of cached item
				return cached.clone();
			}
		}

		ItemStack itemUnTextured = QuickItem
				.of(CompMaterial.PLAYER_HEAD)
				.name(TranslationManager.string(Translations.SKULL_TITLE, "skull_name", this.name))
				.tag("Skulls:ID", String.valueOf(this.id)).make();

		ItemStack item = XSkull.of(itemUnTextured).profile(Profileable.of(ProfileInputType.TEXTURE_URL, this.texture)).lenient().apply();

		// Cache the item if caching is enabled
		if (Settings.ITEMSTACK_CACHE_ENABLED.getBoolean()) {
			int maxCacheSize = Settings.ITEMSTACK_CACHE_SIZE.getInt();
			if (maxCacheSize <= 0) maxCacheSize = 5000;

			// Only cache if we're under the limit
			if (Skulls.getSkullManager().getItemStackCacheSize() < maxCacheSize) {
				Skulls.getSkullManager().cacheItemStack(this.id, item.clone());
			}
		}

		return item;
	}

	@Override
	public void sync() {
		Skulls.getDataManager().updateSkull(this, null);
	}
}
