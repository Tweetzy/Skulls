package ca.tweetzy.skulls.api.interfaces;

import java.util.List;

/**
 * Date Created: April 04 2022
 * Time Created: 8:33 p.m.
 *
 * @author Kiran Hart
 */
public interface Category {

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

	/**
	 * Used to determine whether a category is custom made
	 *
	 * @return true if the category is custom
	 */
	boolean isCustom();

	/**
	 * List of skull ids that were added to this custom
	 * category (only applies to custom categories obviously
	 *
	 * @return a list of ids
	 */
	List<Integer> getSkulls();
}
