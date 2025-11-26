/*
 * Skulls
 * Copyright 2023 Kiran Hart
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

import java.util.List;

public final class SetCommand extends Command {

	public SetCommand() {
		super(AllowedExecutor.PLAYER, "set");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
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
		return null;
	}
}

