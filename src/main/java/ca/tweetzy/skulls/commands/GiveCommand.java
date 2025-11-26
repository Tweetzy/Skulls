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
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.model.StringHelper;
import ca.tweetzy.skulls.settings.Translations;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public final class GiveCommand extends Command {

	public GiveCommand() {
		super(AllowedExecutor.BOTH, "give");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
		if (context.getArgCount() < 2) return ReturnType.FAIL;

		String firstArg = context.getArg(0);
		String secondArg = context.getArg(1);
		
		if (firstArg == null || secondArg == null) return ReturnType.FAIL;

		final boolean isGiveAll = firstArg.equals("*");

		Player target = null;

		if (!isGiveAll)
			target = Bukkit.getPlayerExact(firstArg);

		if (target == null && !isGiveAll) {
			Common.tell(context.getSender(), TranslationManager.string(Translations.PLAYER_OFFLINE, "value", firstArg));
			return ReturnType.FAIL;
		}

		boolean isRandomHead = secondArg.equalsIgnoreCase("random");

		if (!isRandomHead && !StringHelper.isInt(secondArg)) {
			Common.tell(context.getSender(), TranslationManager.string(Translations.NOT_A_NUMBER, "value", secondArg));
			return ReturnType.FAIL;
		}

		int amount = context.hasArg(2) ? StringHelper.tryInt(context.getArg(2), 1) : 1;
		if (amount > 36) amount = 36;

		Skull skull = isRandomHead ? Skulls.getSkullManager().getRandomSkull() : Skulls.getSkullManager().getSkull(Integer.parseInt(secondArg));

		if (skull == null) {
			Common.tell(context.getSender(), TranslationManager.string(Translations.SKULL_NOT_FOUND));
			return ReturnType.FAIL;
		}

		for (int i = 0; i < amount; i++) {
			if (isRandomHead)
				skull = Skulls.getSkullManager().getRandomSkull();

			if (isGiveAll)
				for (Player player : Bukkit.getOnlinePlayers()) {
					giveHead(player, skull);
				}
			else
				giveHead(target, skull);
		}

		return ReturnType.SUCCESS;
	}

	private void giveHead(Player target, Skull skull) {
		if (target.getInventory().firstEmpty() == -1)
			target.getWorld().dropItemNaturally(target.getLocation(), skull.getItemStack());
		else
			target.getInventory().addItem(skull.getItemStack());
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
		return "skulls.command.give";
	}

	@Override
	public String getSyntax() {
		return "skulls give <*/player> <random/id> [#]";
	}

	@Override
	public String getDescription() {
		return "Gives player(s) a specific or random head";
	}
}
