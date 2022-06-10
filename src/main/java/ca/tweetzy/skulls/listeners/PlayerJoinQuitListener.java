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

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.impl.SkullPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

/**
 * Date Created: April 28 2022
 * Time Created: 4:42 p.m.
 *
 * @author Kiran Hart
 */
public final class PlayerJoinQuitListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		if (Skulls.getPlayerManager().findPlayer(player) == null)
			Skulls.getDataManager().insertPlayer(new SkullPlayer(player.getUniqueId(), new ArrayList<>()), (createError, created) -> {
				if (createError == null)
					Skulls.getPlayerManager().addPlayer(created);

			});
	}
}
