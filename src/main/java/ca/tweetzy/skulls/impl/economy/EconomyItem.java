package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.api.interfaces.IEconomy;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.PlayerUtil;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 2:01 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class EconomyItem implements IEconomy {

	@Override
	public String getName() {
		return "Item";
	}

	@Override
	public boolean requiresExternalPlugin() {
		return false;
	}

	@Override
	public boolean has(@NonNull Player player, double amount) {
		return SkullsAPI.getItemCountInPlayerInventory(player, Settings.ITEM_ECONOMY_MATERIAL.toItem()) >= amount;
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		SkullsAPI.removeSpecificItemQuantityFromPlayer(player, Settings.ITEM_ECONOMY_MATERIAL.toItem(), (int) amount);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		for (int i = 0; i < amount; i++)
			PlayerUtil.addItems(player.getInventory(), Settings.ITEM_ECONOMY_MATERIAL.toItem());
	}
}
