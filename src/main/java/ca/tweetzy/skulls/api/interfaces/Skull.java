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

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Date Created: April 04 2022
 * Time Created: 9:52 a.m.
 *
 * @author Kiran Hart
 */
public interface Skull extends DataSync {

	/**
	 * The id of the skull, this is a unique number that can
	 * used to quickly search heads or save them
	 *
	 * @return the id of the head
	 */
	int getId();

	/**
	 * The actual name of the skull, this is what the player
	 * will see within menus or when holding it
	 *
	 * @return the name of the skull
	 */
	String getName();

	/**
	 * The category of this particular skull
	 *
	 * @return the category the skull belongs to
	 */
	String getCategory();

	/**
	 * A list of tags that further describe this particular head
	 * these can be used during the searching process
	 *
	 * @return a list of unique tags for this head
	 */
	List<String> getTags();

	/**
	 * The actual texture that head uses
	 *
	 * @return the skull texture url
	 */
	String getTexture();

	/**
	 * Get the price of this particular skull
	 * by default all skulls with be set to $1
	 *
	 * @return the price of this particular skull
	 */
	double getPrice();


	boolean isBlocked();

	void setName(String name);

	void setPrice(double price);

	void setBlocked(boolean blocked);

	/**
	 * Get the actual {@link ItemStack} of this skull
	 *
	 * @return an {@link ItemStack} of this skull
	 */
	ItemStack getItemStack();
}
