package ca.tweetzy.skulls.model;

import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.impl.Skull;
import ca.tweetzy.tweety.remain.CompMaterial;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.inventory.ItemStack;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 17 2021
 * Time Created: 8:58 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class SkullMaterial {

	public ItemStack get(@NonNull final String value) {
		if (value.contains(":") && value.split(":").length == 2) {
			final String[] parts = value.split(":");
			if (!parts[0].equalsIgnoreCase("Skulls"))
				return CompMaterial.PLAYER_HEAD.toItem();
			if (!NumberUtils.isNumber(parts[1]))
				return CompMaterial.PLAYER_HEAD.toItem();

			final Skull skull = SkullsAPI.getSkull(Integer.parseInt(parts[1]));
			if (skull == null)
				return CompMaterial.PLAYER_HEAD.toItem();

			return SkullsAPI.getSkullItemStack(Integer.parseInt(parts[1]));
		}

		return CompMaterial.fromString(value).toItem();
	}
}
