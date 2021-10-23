package ca.tweetzy.skulls.api.interfaces;

import ca.tweetzy.tweety.collection.StrictList;
import org.bukkit.inventory.ItemStack;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:41 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface ISkull {

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
	StrictList<String> getTags();

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

	/**
	 * Get the actual {@link ItemStack} of this skull
	 *
	 * @return an {@link ItemStack} of this skull
	 */
	ItemStack getItemStack();
}
