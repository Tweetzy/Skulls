package ca.tweetzy.skulls.settings;

import ca.tweetzy.tweety.settings.SimpleLocalization;

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

	private static void init() {
		pathPrefix(null);
		LOADING = getString("Loading");
		NO_MONEY = getString("No Money");
		WITHDRAW = getString("Withdraw");
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}
}
