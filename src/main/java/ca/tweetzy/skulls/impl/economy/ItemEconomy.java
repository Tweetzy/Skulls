package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.skulls.api.interfaces.Economy;
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
		return false;
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {

	}

	@Override
	public void deposit(@NonNull Player player, double amount) {

	}
}
