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
import ca.tweetzy.flight.command.CommandContext;
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
	protected ReturnType execute(CommandContext context) {
		if (context.getArgCount() < 3 && !context.isPlayer()) {
			return ReturnType.INVALID_SYNTAX;
		}

		Bukkit.getServer().getScheduler().runTaskAsynchronously(Skulls.getInstance(), () -> {
			if (context.getArgCount() == 0) {
				if (!context.isPlayer()) return;
				final Player executor = context.getPlayer();
				executor.getInventory().addItem(QuickItem.of(executor).name(Settings.PLAYER_HEAD_NAME.getString().replace("%player_name%", executor.getName())).make());
				return;
			}

			String targetName = context.getArg(0);
			if (targetName == null) return;

			OfflinePlayer targetUser = Bukkit.getPlayer(targetName);

			if (targetUser == null) {
				targetUser = Bukkit.getOfflinePlayer(targetName);
			}

			final ItemStack item = QuickItem.of(targetUser)
					.name(Settings.PLAYER_HEAD_NAME.getString().replace("%player_name%", targetUser.getName()))
					.amount(context.hasArg(1) ? StringHelper.tryInt(context.getArg(1), 1) : 1)
					.make();

			if (context.getArgCount() == 3) {
				String targetPlayerName = context.getArg(2);
				if (targetPlayerName == null) return;
				
				final Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
				if (targetPlayer == null) {
					Common.tell(context.getSender(), TranslationManager.string(Translations.PLAYER_OFFLINE, "value", targetPlayerName));
					return;
				}

				targetPlayer.getInventory().addItem(item);
			} else {
				if (!context.isPlayer()) return;
				final Player executor = context.getPlayer();
				executor.getInventory().addItem(item);
			}
		});

		return ReturnType.SUCCESS;
	}

	@Override
	protected ReturnType execute(org.bukkit.command.CommandSender sender, String... args) {
		return execute(new ca.tweetzy.flight.command.CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	protected List<String> tab(CommandContext context) {
		return null;
	}

	@Override
	protected List<String> tab(org.bukkit.command.CommandSender sender, String... args) {
		return tab(new ca.tweetzy.flight.command.CommandContext(sender, args, getSubCommands().get(0)));
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
