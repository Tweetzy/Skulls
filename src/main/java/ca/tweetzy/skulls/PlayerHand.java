package ca.tweetzy.skulls;

import ca.tweetzy.tweety.MinecraftVersion;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 3:32 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class PlayerHand {

	public ItemStack get(@NonNull final Player player) {
		return MinecraftVersion.newerThan(MinecraftVersion.V.v1_8) ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInHand();
	}
}
