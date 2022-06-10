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

package ca.tweetzy.skulls.api.interfaces;

import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Date Created: April 04 2022
 * Time Created: 9:53 a.m.
 *
 * @author Kiran Hart
 */
public interface Economy {

	/**
	 * The name of the economy provider
	 *
	 * @return the name of the economy provider
	 */
	String getName();

	/**
	 * Only should be true if not using the item economy
	 *
	 * @return
	 */
	boolean requiresExternalPlugin();

	/**
	 * Check whether a player has a specific amount of funds in their account
	 *
	 * @param player is the {@link Player}
	 * @return true if they have enough funds
	 */
	boolean has(@NonNull final Player player, final double amount);

	/**
	 * Withdraw money from a player's account
	 *
	 * @param player is the {@link Player}
	 * @param amount is the amount of money being withdrawn
	 */
	void withdraw(@NonNull final Player player, final double amount);

	/**
	 * Deposit money into a player's account
	 *
	 * @param player is the {@link Player}
	 * @param amount is the amount of money being deposited
	 */
	void deposit(@NonNull final Player player, final double amount);
}
