package ca.tweetzy.skulls.model;

import ca.tweetzy.tweety.command.annotation.Permission;
import ca.tweetzy.tweety.constants.TweetyPermissions;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 2:11 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Permissions extends TweetyPermissions {

	@Permission("Allows the player unlimited access to skulls w.o paying")
	public static final String FREE_SKULLS = "skulls.freeskulls";

	@Permission("Allows the player to edit a skull's price")
	public static final String EDIT_PRICE = "skulls.editprice";

	@Permission("Allows the player to add a skull to a custom category")
	public static final String ADD_TO_CATEGORY = "skulls.addtocategory";

	@Permission("Allows the player to favourite a skull")
	public static final String FAVOURITE = "skulls.favourite";
}
