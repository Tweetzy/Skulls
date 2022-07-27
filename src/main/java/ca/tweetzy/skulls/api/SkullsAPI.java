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

package ca.tweetzy.skulls.api;

import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.Category;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Date Created: April 04 2022
 * Time Created: 9:52 a.m.
 *
 * @author Kiran Hart
 */
public interface SkullsAPI {

	/**
	 * Gets the skull with the given id.
	 *
	 * @param id The id of the skull.
	 * @return A skull object.
	 */
	Skull getSkull(final int id);

	/**
	 * It returns a skull item with the specified id
	 *
	 * @param id The id of the skull.
	 * @return An ItemStack
	 */
	ItemStack getSkullItem(final int id);

	/**
	 * Gets a random skull from a random category,
	 * it will not get a skull from a disabled category
	 *
	 * @return a random {@link Skull} from enabled category
	 */
	Skull getRandomSkull();

	/**
	 * Get all the skulls in the given category.
	 *
	 * @param category The category to get the skulls from.
	 * @return A list of Skulls.
	 */
	List<Skull> getSkulls(@NonNull final BaseCategory category);

	/**
	 * Get a list of skulls from the specified category.
	 *
	 * @param category The category of the skull you want to get.
	 * @return A list of Skulls.
	 */
	List<Skull> getSkulls(@NonNull final String category);

	/**
	 * This returns a list of skulls that match the given search phrase.
	 *
	 * @param phrase The phrase to search for.
	 * @return A list of Skulls that match the search phrase.
	 */
	List<Skull> getSkullsBySearch(@NonNull final String phrase);

	/**
	 * This returns a list of skulls, given a list of IDs.
	 *
	 * @param ids The list of ids to get the skulls for.
	 * @return A list of Skulls.
	 */
	List<Skull> getSkulls(@NonNull final List<Integer> ids);

	/**
	 * It returns the number of skulls in the given category.
	 *
	 * @param category The category of the skull.
	 * @return The number of skulls in the category.
	 */
	long getSkullCount(@NonNull final String category);

	/**
	 * Finds a SkullUser object for the given player.
	 *
	 * @param player The player to find the skull of.
	 * @return A SkullUser object.
	 */
	SkullUser findPlayer(@NonNull final Player player);

	/**
	 * Finds a player by their UUID
	 *
	 * @param uuid The UUID of the player you want to find.
	 * @return A SkullUser object.
	 */
	SkullUser findPlayer(@NonNull final UUID uuid);

	/**
	 * Find a category by its ID.
	 *
	 * @param id The id of the category to find.
	 * @return A Category object
	 */
	Category findCategory(@NonNull final String id);

	/**
	 * Get a list of all the custom categories.
	 *
	 * @return A list of categories.
	 */
	List<Category> getCustomCategories();
}
