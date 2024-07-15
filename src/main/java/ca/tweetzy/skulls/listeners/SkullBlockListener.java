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

package ca.tweetzy.skulls.listeners;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.nbtapi.NBT;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.PlacedSkull;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.hooks.WorldGuardHook;
import ca.tweetzy.skulls.impl.PlacedSkullLocation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public final class SkullBlockListener implements Listener {

	@EventHandler()
	public void onSkullPlace(final BlockPlaceEvent event) {
		if (event.isCancelled()) return;
		if (!WorldGuardHook.isAllowedPlace(event.getPlayer(), event.getBlock())) {
			event.setBuild(false);
			event.setCancelled(true);
			return;
		}

		final ItemStack item = PlayerUtil.getHand(event.getPlayer());
		if (item == null || item.getType() == CompMaterial.AIR.parseMaterial() || item.getAmount() == 0) return;

		if (!NBT.get(item, nbt -> (boolean) nbt.hasTag("Skulls:ID"))) {
			return;
		}

		final Block block = event.getBlockPlaced();
		if (block.getType() == CompMaterial.PLAYER_HEAD.parseMaterial() || block.getType() == CompMaterial.PLAYER_WALL_HEAD.parseMaterial()) {
			final int skullId = Integer.parseInt(NBT.get(item, nbt -> (String) nbt.getString("Skulls:ID")));
			Skulls.getSkullManager().addPlacedSkull(new PlacedSkullLocation(UUID.randomUUID(), skullId, block.getLocation()));
		}

	}

	@EventHandler()
	public void onSkullDrop(final BlockDropItemEvent event) {
		if (event.isCancelled()) return;

		final BlockState block = event.getBlockState();

		if (!(block instanceof org.bukkit.block.Skull))
			return;

		if (block.getType() != CompMaterial.PLAYER_HEAD.parseMaterial() && block.getType() != CompMaterial.PLAYER_WALL_HEAD.parseMaterial())
			return;

		if (!Skulls.getSkullManager().getPlacedSkulls().containsKey(block.getLocation()))
			return;

		final PlacedSkull placedSkull = Skulls.getSkullManager().getPlacedSkulls().get(block.getLocation());
		Skulls.getSkullManager().removePlacedSkull(placedSkull);

		final Skull skull = Skulls.getSkullManager().getSkull(placedSkull.getSkullId());

		for (Item item : event.getItems()) {
			ItemStack itemStack = item.getItemStack();
			if (!(itemStack.getItemMeta() instanceof SkullMeta))
				continue;

			item.setItemStack(skull.getItemStack());
			break;
		}
	}
}
