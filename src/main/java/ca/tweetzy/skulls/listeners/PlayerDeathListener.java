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

import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.skulls.api.events.PlayerPreSkullDropEvent;
import ca.tweetzy.skulls.api.events.PlayerSkullDropEvent;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public final class PlayerDeathListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!Settings.PLAYER_HEAD_DROP.getBoolean()) return;

		final Player player = event.getEntity();
		final Random random = new Random();

		if (random.nextDouble() * 100D < Settings.PLAYER_HEAD_DROP_CHANCE.getInt()) {
			final Location dropLocation = player.getLocation();
			final ItemStack skull = QuickItem.of(player).name(Settings.PLAYER_HEAD_NAME.getString().replace("%player_name%", player.getName())).make();

			// Send pre-skull drop event
			PlayerPreSkullDropEvent preEvent = new PlayerPreSkullDropEvent(player, dropLocation, skull);
			Bukkit.getPluginManager().callEvent(preEvent);
			if (preEvent.isCancelled())
				return;

			// Drop skull
			Item droppedSkull = player.getWorld().dropItemNaturally(dropLocation, skull);

			// Send skull drop event
			PlayerSkullDropEvent postEvent = new PlayerSkullDropEvent(player, droppedSkull);
			Bukkit.getPluginManager().callEvent(postEvent);
		}
	}
}
