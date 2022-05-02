package ca.tweetzy.skulls.commands;

import ca.tweetzy.rose.command.AllowedExecutor;
import ca.tweetzy.rose.command.Command;
import ca.tweetzy.rose.command.ReturnType;
import ca.tweetzy.rose.comp.NBTEditor;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Date Created: May 02 2022
 * Time Created: 12:58 p.m.
 *
 * @author Kiran Hart
 */
public final class PlayerHeadCommand extends Command {

	public PlayerHeadCommand() {
		super(AllowedExecutor.BOTH, "phead");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length < 3 && !(sender instanceof Player)) {
			return ReturnType.INVALID_SYNTAX;
		}

		Common.runAsync(() -> {
			if (args.length == 0) {
				final Player executor = (Player) sender;
				executor.getInventory().addItem(QuickItem.of(executor).name(Settings.PLAYER_HEAD_NAME.getString().replace("%player_name%", executor.getName())).make());
				return;
			}

			OfflinePlayer targetUser = Bukkit.getPlayer(args[0]);

			if (targetUser == null) {
				targetUser = Bukkit.getOfflinePlayer(args[0]);
			}

			final ItemStack item = QuickItem.of(targetUser)
					.name(Settings.PLAYER_HEAD_NAME.getString().replace("%player_name%", targetUser.getName()))
					.amount(args.length > 1 ? tryInt(args[1], 1) : 1)
					.make();

			if (args.length == 3) {
				final Player targetPlayer = Bukkit.getPlayerExact(args[2]);
				if (targetPlayer == null) {
					Common.tell(sender, Translation.PLAYER_OFFLINE.getString("player", args[2]));
					return;
				}

				targetPlayer.getInventory().addItem(item);
			} else {
				final Player executor = (Player) sender;
				executor.getInventory().addItem(item);
			}
		});

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "skulls.cmd.phead";
	}

	@Override
	public String getSyntax() {
		return "skulls phead <player> [#] [target]";
	}

	@Override
	public String getDescription() {
		return "Give yourself or player a player head";
	}

	public int tryInt(String value, int def) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return def;
		}
		return Integer.parseInt(value);
	}
}
