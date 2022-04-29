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
