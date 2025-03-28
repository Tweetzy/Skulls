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

package ca.tweetzy.skulls.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@UtilityClass
public final class WorldGuardHook {

	public boolean isAllowedPlace(@NonNull final Player player, @NonNull final Block block) {
		if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return true;

		final RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		final Location loc = BukkitAdapter.adapt(block.getLocation());
		final World world = BukkitAdapter.adapt(block.getWorld());

		if (WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(WorldGuardPlugin.inst().wrapPlayer(player), world))
			return true;

		// BUILD and BLOCK-PLACE flags are verified
		return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD, Flags.BLOCK_PLACE);
	}

	public boolean isAllowedBreak(@NonNull final Player player, @NonNull final Block block) {
		if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return true;

		final RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		final Location loc = BukkitAdapter.adapt(block.getLocation());
		final World world = BukkitAdapter.adapt(block.getWorld());

		if (WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(WorldGuardPlugin.inst().wrapPlayer(player), world))
			return true;


		return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD) || query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), Flags.BLOCK_BREAK);

	}
}
