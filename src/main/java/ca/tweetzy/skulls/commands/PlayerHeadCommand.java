/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.commands;

import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.model.StringHelper;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
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

		Bukkit.getServer().getScheduler().runTaskAsynchronously(Skulls.getInstance(), () -> {
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
					.amount(args.length > 1 ? StringHelper.tryInt(args[1], 1) : 1)
					.make();

			if (args.length == 3) {
				final Player targetPlayer = Bukkit.getPlayerExact(args[2]);
				if (targetPlayer == null) {
					Common.tell(sender, TranslationManager.string(Translations.PLAYER_OFFLINE, "value", args[2]));
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
		return "skulls.command.phead";
	}

	@Override
	public String getSyntax() {
		return "skulls phead <player> [#] [target]";
	}

	@Override
	public String getDescription() {
		return "Give yourself or player a player head";
	}
}
