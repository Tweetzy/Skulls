package ca.tweetzy.skulls.listeners;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.collection.StrictList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 1:40 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class PlayerJoinLeaveListener implements Listener {

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if (player.isOp()) {
			Common.tell(player, "&aIf you need support visit https://discord.gg/WTg4PhBHEg");
		}

		Common.runAsync(() -> {
			final SkullPlayer skullPlayer = new SkullPlayer(player.getUniqueId(), new StrictList<>());
			SkullsAPI.addPlayer(skullPlayer);
		});
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		final SkullPlayer skullPlayer = SkullsAPI.getPlayer(player.getUniqueId());

		Common.runAsync(() -> {
			Skulls.getSkullPlayerManager().savePlayer(skullPlayer);
			SkullsAPI.removePlayer(skullPlayer.getPlayerId());
		});
	}
}
