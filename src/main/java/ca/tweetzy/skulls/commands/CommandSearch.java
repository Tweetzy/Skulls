package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.menus.MenuList;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.tweety.Common;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 19 2021
 * Time Created: 5:58 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandSearch extends SkullsSubCommand {

	public CommandSearch() {
		super("search");
		setMinArguments(1);
		setUsage("<keywords>");
		setDescription("Search for skulls");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String keywords = joinArgs(0);

		if (Skulls.getSkullManager().isLoading()) {
			Common.tell(player, Localization.LOADING);
			return;
		}

		new MenuList(player, SkullsAPI.getSkullsByTerm(SkullsAPI.cleanSearch(keywords)), SkullsAPI.cleanSearch(keywords)).displayTo(player);
	}
}
