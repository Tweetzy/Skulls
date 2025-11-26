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
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.guis.MainGUI;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Date Created: April 20 2022
 * Time Created: 9:42 p.m.
 *
 * @author Kiran Hart
 */
public final class SkullsCommand extends Command {

	public SkullsCommand() {
		super(AllowedExecutor.BOTH, "skulls");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
		if (context.isPlayer()) {
			final Player player = context.getPlayer();

//			if (Skulls.getSkullManager().isLoading()) {
//				Common.tell(player, TranslationManager.string(Translations.LOADING));
//				return ReturnType.FAIL;
//			}

			if (player.hasPermission("skulls.command.main") || Settings.MAIN_MENU_REQUIRES_NO_PERM.getBoolean())
				Skulls.getGuiManager().showGUI(player, new MainGUI(player));
		}

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
		return null;
	}

	@Override
	public String getSyntax() {
		return null;
	}

	@Override
	public String getDescription() {
		return "The main command for the plugin";
	}

}
