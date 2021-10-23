package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.PlayerHand;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.tweety.ChatUtil;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.model.Replacer;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.remain.CompMetadata;
import ca.tweetzy.tweety.remain.nbt.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 3:29 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandInfo extends SkullsSubCommand {

	public CommandInfo() {
		super("info|i");
		setDescription("Provides information on the held skull");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final ItemStack hand = PlayerHand.get(player);

		if (hand == null || hand.getType() == CompMaterial.AIR.toMaterial()) {
			tell(Localization.NOTHING_IN_HAND);
			return;
		}

		if (hand.getType() != CompMaterial.PLAYER_HEAD.toMaterial()) {
			tell(Localization.NOT_A_SKULL);
			return;
		}

		final String id = CompMetadata.getMetadata(hand, "Skulls:ID");
		final String texture = CompMetadata.getMetadata(hand, "Skulls:Texture");

		tellNoPrefix(Replacer.replaceArray(
				Localization.SKULL_INFO,
				"chat_line", Common.chatLineSmooth(),
				"skull_id", id,
				"skull_texture", texture
		));


	}
}
