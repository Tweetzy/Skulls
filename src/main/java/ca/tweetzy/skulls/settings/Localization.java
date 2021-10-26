package ca.tweetzy.skulls.settings;

import ca.tweetzy.tweety.settings.SimpleLocalization;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 12:42 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Localization extends SimpleLocalization {

	public static String LOADING;
	public static String NO_MONEY;
	public static String WITHDRAW;
	public static String SEARCH;
	public static String PLAYER_NOT_FOUND;
	public static String INVALID_NUMBER;
	public static String NOTHING_IN_HAND;
	public static String NOT_A_SKULL;
	public static List<String> SKULL_INFO;
	public static String ENTER_CATEGORY_ID;
	public static String CATEGORY_ID_TAKEN;
	public static String CATEGORY_CREATED;

	private static void init() {
		pathPrefix(null);
		LOADING = getString("Loading");
		NO_MONEY = getString("No Money");
		WITHDRAW = getString("Withdraw");
		SEARCH = getString("Search");
		PLAYER_NOT_FOUND = getString("Player Not Found");
		INVALID_NUMBER = getString("Invalid Number");
		NOTHING_IN_HAND = getString("Nothing In Hand");
		NOT_A_SKULL = getString("Not A Skull");
		SKULL_INFO = getStringList("Skull Info");
		ENTER_CATEGORY_ID = getString("Enter Category Id");
		CATEGORY_ID_TAKEN = getString("Category Id Taken");
		CATEGORY_CREATED = getString("Category Created");
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}
}
