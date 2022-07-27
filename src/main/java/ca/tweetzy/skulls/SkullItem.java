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

package ca.tweetzy.skulls;

import ca.tweetzy.feather.comp.enums.CompMaterial;
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
