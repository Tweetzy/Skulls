package ca.tweetzy.skulls.listeners;

import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.RandomUtil;
import ca.tweetzy.tweety.menu.model.SkullCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 19 2022
 * Time Created: 1:27 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class PlayerDeathListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!Settings.DROP_HEADS) return;

		final Player player = event.getEntity();
		if (RandomUtil.chanceD(Settings.DEFAULT_DROP_CHANCE)) {
			player.getWorld().dropItemNaturally(player.getLocation(), SkullCreator.itemFromName(player.getName()));
		}
	}
}
