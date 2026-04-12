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
import ca.tweetzy.skulls.BuildExpiration;
import ca.tweetzy.skulls.settings.Settings;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ExpireCommand extends Command {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss z")
			.withZone(ZoneId.systemDefault());

	public ExpireCommand() {
		super(AllowedExecutor.BOTH, "expire");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
		long expirationMs = BuildExpiration.getExpirationEpochMs();
		if (expirationMs == 0L) {
			tell(context.getSender(), "&aThis build does not expire.");
			return ReturnType.SUCCESS;
		}
		String dateStr = FORMATTER.format(Instant.ofEpochMilli(expirationMs));
		if (BuildExpiration.isExpired()) {
			tell(context.getSender(), "&cThis build expired on &e" + dateStr);
		} else {
			tell(context.getSender(), "&aThis build expires on &e" + dateStr);
		}
		return ReturnType.SUCCESS;
	}

	@Override
	protected ReturnType execute(org.bukkit.command.CommandSender sender, String... args) {
		return execute(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	protected List<String> tab(CommandContext context) {
		return null;
	}

	@Override
	protected List<String> tab(org.bukkit.command.CommandSender sender, String... args) {
		return tab(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	public String getPermissionNode() {
		return "skulls.command.expire";
	}

	@Override
	public String getSyntax() {
		return null;
	}

	@Override
	public String getDescription() {
		return "Check when this build expires";
	}
}
