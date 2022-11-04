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

package ca.tweetzy.skulls.model;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Date Created: April 28 2022
 * Time Created: 4:23 p.m.
 *
 * @author Kiran Hart
 */
@UtilityClass
public final class PlayerInventoryHelper {

	/**
	 * Get the total amount of an item in the player's inventory
	 *
	 * @param player is the player being checked
	 * @param stack  is the item you want to find
	 * @return the total count of the item(s)
	 */
	public int getItemCountInPlayerInventory(@NonNull final Player player, ItemStack stack) {
		int total = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null || !item.isSimilar(stack)) continue;
			total += item.getAmount();
		}
		return total;
	}

	/**
	 * Removes a set amount of a specific item from the player inventory
	 *
	 * @param player is the player you want to remove the item from
	 * @param stack  is the item that you want to remove
	 * @param amount is the amount of items you want to remove.
	 */
	public void removeSpecificItemQuantityFromPlayer(@NonNull final Player player, @NonNull final ItemStack stack, int amount) {
		int i = amount;
		for (int j = 0; j < player.getInventory().getSize(); j++) {
			ItemStack item = player.getInventory().getItem(j);
			if (item == null) continue;
			if (!item.isSimilar(stack)) continue;

			if (i >= item.getAmount()) {
				player.getInventory().clear(j);
				i -= item.getAmount();
			} else if (i > 0) {
				item.setAmount(item.getAmount() - i);
				i = 0;
			} else {
				break;
			}
		}
	}
}
