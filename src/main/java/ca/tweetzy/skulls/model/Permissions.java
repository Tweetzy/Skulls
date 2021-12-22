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

	@Permission("Allows the player player to use /skulls")
	public static final String MAIN_COMMAND = "skulls.command";

	@Permission("Allows the player unlimited access to skulls w.o paying")
	public static final String FREE_SKULLS = "skulls.freeskulls";

	@Permission("Allows the player to edit a skull's price")
	public static final String EDIT_PRICE = "skulls.editprice";

	@Permission("Allows the player to add a skull to a custom category")
	public static final String ADD_TO_CATEGORY = "skulls.addtocategory";

	@Permission("Allows the player to remove a skull from a custom category")
	public static final String REMOVE_FROM_CATEGORY = "skulls.removefromcategory";

	@Permission("Allows the player to add a custom category")
	public static final String ADD_NEW_CATEGORY = "skulls.addnewcategory";

	@Permission("Allows the player to delete a custom category")
	public static final String DELETE_CATEGORY = "skulls.deletecategory";

	@Permission("Allows the player to favourite a skull")
	public static final String FAVOURITE = "skulls.favourite";

	@Permission("Allows the player to toggle the blocks status of a skull")
	public static final String TOGGLE_BLOCK = "skulls.block";

	@Permission("Allows the player to buy skulls that are blocked")
	public static final String BUY_BLOCKED = "skulls.buyblocked";

}
