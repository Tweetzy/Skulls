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

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;

/**
 * Date Created: April 28 2022
 * Time Created: 4:42 p.m.
 *
 * @author Kiran Hart
 */
public final class PlayerJoinQuitListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onOpdPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if (!player.isOp()) return;

		if (!Settings.TELL_OP_PATREON_LINK.getBoolean()) return;
		Bukkit.getServer().getScheduler().runTaskLater(Skulls.getInstance(), () -> {
			Common.tellNoPrefix(player,
					"",
					"<center>%pl_name%",
					"<center>&7If you like the plugin, please consider supporting",
					"<center>&7me on patreon for as low as &a$1 &7a month.",
					"<center>&7It will help me keep the skulls db up and for me",
					"<center>&7to be able to provide updates regularly",
					"",
					"<center>&6https://patreon.com/kiranhart",
					""
			);
		}, 5L);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;
		final Player player = event.getPlayer();

		SkullUser skullUser = Skulls.getPlayerManager().findPlayer(player);
		if (skullUser == null) {
			// insert the user
			Skulls.getDataManager().insertPlayer(new SkullPlayer(player.getUniqueId(), new ArrayList<>()), (createError, created) -> {
				if (createError == null)
					Skulls.getPlayerManager().addPlayer(created);
			});
		}
	}

}
