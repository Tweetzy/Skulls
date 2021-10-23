package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.menus.MenuMain;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.command.SimpleCommandGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: August 22 2021
 * Time Created: 11:26 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkullsCommandGroup extends SimpleCommandGroup {

	@Getter
	private final static SkullsCommandGroup instance = new SkullsCommandGroup();

	@Override
	protected String getHeaderPrefix() {
		return "" + ChatColor.YELLOW + ChatColor.BOLD;
	}

	@Override
	protected void zeroArgActions(CommandSender commandSender) {
		if (Skulls.getSkullManager().isLoading()) {
			Common.tell(commandSender, Localization.LOADING);
			return;
		}

		if (!(commandSender instanceof Player)) return;
		final Player player = (Player) commandSender;

		new MenuMain(SkullsAPI.getPlayer(player.getUniqueId())).displayTo(player);
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new CommandSearch());
	}

	@Override
	protected String getCredits() {
		return null;
	}

	@Override
	protected boolean useZeroArgAction() {
		return true;
	}
}
