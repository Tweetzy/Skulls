package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.skulls.api.interfaces.IEconomy;
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
		return false;
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {

	}

	@Override
	public void deposit(@NonNull Player player, double amount) {

	}
}
