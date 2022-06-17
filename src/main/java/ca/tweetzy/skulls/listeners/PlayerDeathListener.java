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

import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public final class PlayerDeathListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!Settings.PLAYER_HEAD_DROP.getBoolean()) return;

		final Player player = event.getEntity();
		final Random random = new Random();

		if (random.nextDouble() * 100D < Settings.PLAYER_HEAD_DROP_CHANCE.getInt()) {
			player.getWorld().dropItemNaturally(player.getLocation(), QuickItem.of(player).name(Settings.PLAYER_HEAD_NAME.getString().replace("%player_name%", player.getName())).make());
		}
	}
}
