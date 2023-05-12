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

package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.skulls.api.interfaces.Economy;
import ca.tweetzy.skulls.model.PlayerInventoryHelper;
import ca.tweetzy.skulls.settings.Settings;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Date Created: April 21 2022
 * Time Created: 11:15 a.m.
 *
 * @author Kiran Hart
 */
public final class ItemEconomy implements Economy {

	public ItemEconomy() {
		Common.log("&aSetting up item economy provider");
	}

	@Override
	public String getName() {
		return "Item";
	}

	@Override
	public boolean requiresExternalPlugin() {
		return false;
	}

	@Override
	public boolean has(@NonNull Player player, double amount) {
		final ItemStack item = Settings.ITEM_ECONOMY_ITEM.getItemStack();
		final int total = PlayerUtil.getItemCountInPlayerInventory(player, item);
		return total >= (int) amount;
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		final ItemStack item = Settings.ITEM_ECONOMY_ITEM.getItemStack();
		PlayerInventoryHelper.removeSpecificItemQuantityFromPlayer(player, item, (int) amount);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
	}
}
