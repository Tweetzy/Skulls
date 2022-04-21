package ca.tweetzy.skulls.api.interfaces;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Date Created: April 04 2022
 * Time Created: 9:52 a.m.
 *
 * @author Kiran Hart
 */
public interface Skull {

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

	void setBlocked(boolean blocked);

	boolean isBlocked();

	/**
	 * Get the actual {@link ItemStack} of this skull
	 *
	 * @return an {@link ItemStack} of this skull
	 */
	ItemStack getItemStack();
}
