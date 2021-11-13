package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.tweety.PlayerUtil;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 2:28 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandGive extends SkullsSubCommand {

	public CommandGive() {
		super("give|g");
		setMinArguments(1);
		setUsage("<id/random> [#] [player]");
		setDescription(Localization.Commands.GIVE);
	}

	@Override
	protected void onCommand() {
		final String skullToGive = args[0];
		final int skullId = NumberUtils.isNumber(skullToGive) ? Integer.parseInt(skullToGive) : -1;

		int amount = 1;

		if (args.length >= 2) {
			amount = findNumber(1, Localization.INVALID_NUMBER);
		}

		if (args.length == 3) {
			final Player toGive = findPlayer(args[2], Localization.PLAYER_NOT_FOUND);
			giveRandomOrId(toGive, amount, skullId, skullId == -1);
			return;
		}

		checkConsole();
		final Player player = getPlayer();
		giveRandomOrId(player, amount, skullId, skullId == -1);
	}

	private void giveRandomOrId(@NonNull final Player player, final int amount, final int id, final boolean random) {
		for (int i = 0; i < amount; i++)
			if (random)
				PlayerUtil.addItems(player.getInventory(), SkullsAPI.getRandomSkull().getItemStack());
			else
				PlayerUtil.addItems(player.getInventory(), SkullsAPI.getSkull(id).getItemStack());
	}
}
