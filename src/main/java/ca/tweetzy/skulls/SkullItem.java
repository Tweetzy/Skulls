package ca.tweetzy.skulls;

import ca.tweetzy.rose.comp.enums.CompMaterial;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.inventory.ItemStack;

/**
 * Date Created: May 01 2022
 * Time Created: 11:46 a.m.
 *
 * @author Kiran Hart
 */
@UtilityClass
public final class SkullItem {

	public ItemStack get(@NonNull final String texture) {
		if (!texture.startsWith("skulls:"))
			return CompMaterial.matchCompMaterial(texture).orElse(CompMaterial.STONE).parseItem();

		final String[] split = texture.split(":");

		if (!NumberUtils.isNumber(split[1]))
			return CompMaterial.STONE.parseItem();

		return Skulls.getSkullManager().getSkullItem(Integer.parseInt(split[1]));
	}
}
