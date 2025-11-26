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
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.skulls.guis.SkullsViewGUI;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.model.StringHelper;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Date Created: April 20 2022
 * Time Created: 9:42 p.m.
 *
 * @author Kiran Hart
 */
public final class SearchCommand extends Command {

	public SearchCommand() {
		super(AllowedExecutor.PLAYER, "search");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
		final Player player = context.getPlayer();
		if (!context.hasArg(0)) return ReturnType.INVALID_SYNTAX;

		// Join all arguments into a search query
		String query = StringHelper.escapeRegex(context.joinArgs(0));

		SkullUser skullUser = Skulls.getPlayerManager().findOrCreate(player);
		if (skullUser == null)
			Skulls.getDataManager().insertPlayer(new SkullPlayer(player.getUniqueId(), new ArrayList<>()), (createError, created) -> {
				if (createError == null) {
					Skulls.getPlayerManager().addPlayer(created);
					Skulls.getGuiManager().showGUI(player, new SkullsViewGUI(null, created, query, ViewMode.SEARCH));
				} else {
					Skulls.getGuiManager().showGUI(player, new SkullsViewGUI(null, new SkullPlayer(player.getUniqueId(), new ArrayList<>()), query, ViewMode.SEARCH));
				}
			});
		else
			Skulls.getGuiManager().showGUI(player, new SkullsViewGUI(null, skullUser, query, ViewMode.SEARCH));

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
		return Settings.GENERAL_USAGE_REQUIRES_NO_PERM.getBoolean() ? null : "skulls.command.search";
	}

	@Override
	public String getSyntax() {
		return "search <keywords>";
	}

	@Override
	public String getDescription() {
		return "Search for skulls";
	}
}
