package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.skulls.PlayerInventoryHelper;
import ca.tweetzy.skulls.api.interfaces.Economy;
import ca.tweetzy.skulls.settings.Settings;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Date Created: April 21 2022
 * Time Created: 11:15 a.m.
 *
 * @author Kiran Hart
 */
public final class ItemEconomy implements Economy {

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
		return PlayerInventoryHelper.getItemCountInPlayerInventory(player, Settings.ITEM_ECONOMY_ITEM.getMaterial().parseItem()) >= (int) amount;
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		PlayerInventoryHelper.removeSpecificItemQuantityFromPlayer(player, Settings.ITEM_ECONOMY_ITEM.getMaterial().parseItem(), (int) amount);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
	}
}
