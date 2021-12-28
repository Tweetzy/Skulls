package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.menus.MenuList;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.tweety.Common;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 3:07 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandFavourites extends SkullsSubCommand {

	public CommandFavourites() {
		super("favourites|favs");
		setDescription(Localization.Commands.FAVOURITES);

	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();

		if (Skulls.getSkullManager().isLoading()) {
			Common.tell(player, Localization.LOADING);
			return;
		}

		new MenuList(SkullsAPI.getPlayer(player.getUniqueId())).displayTo(player);
	}
}
