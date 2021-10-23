package ca.tweetzy.skulls.api.interfaces;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:42 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface ISkullCategory {

	/**
	 * The id of this particular category
	 *
	 * @return the id of this category
	 */
	String getId();

	/**
	 * The actual display name of this category, this is what
	 * will be shown on items in menus
	 *
	 * @return the display name of this category
	 */
	String getName();
}
