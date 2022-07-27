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

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.Category;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public final class SkullsAPIImplementation implements SkullsAPI {

	@Override
	public Skull getSkull(int id) {
		return Skulls.getSkullManager().getSkull(id);
	}

	@Override
	public ItemStack getSkullItem(int id) {
		return Skulls.getSkullManager().getSkullItem(id);
	}

	@Override
	public Skull getRandomSkull() {
		return Skulls.getSkullManager().getRandomSkull();
	}

	@Override
	public List<Skull> getSkulls(@NonNull BaseCategory category) {
		return Skulls.getSkullManager().getSkulls(category);
	}

	@Override
	public List<Skull> getSkulls(@NonNull String category) {
		return Skulls.getSkullManager().getSkulls(category);
	}

	@Override
	public List<Skull> getSkullsBySearch(@NonNull String phrase) {
		return Skulls.getSkullManager().getSkullsBySearch(phrase);
	}

	@Override
	public List<Skull> getSkulls(@NonNull List<Integer> ids) {
		return Skulls.getSkullManager().getSkulls(ids);
	}

	@Override
	public long getSkullCount(@NonNull String category) {
		return Skulls.getSkullManager().getSkullCount(category);
	}

	@Override
	public SkullUser findPlayer(@NonNull Player player) {
		return Skulls.getPlayerManager().findPlayer(player);
	}

	@Override
	public SkullUser findPlayer(@NonNull UUID uuid) {
		return Skulls.getPlayerManager().findPlayer(uuid);
	}

	@Override
	public Category findCategory(@NonNull String id) {
		return Skulls.getCategoryManager().findCategory(id);
	}

	@Override
	public List<Category> getCustomCategories() {
		return Skulls.getCategoryManager().getCustomCategories();
	}
}
