package ca.tweetzy.skulls.model;

import ca.tweetzy.skulls.api.interfaces.IEconomy;
import ca.tweetzy.skulls.impl.economy.EconomyItem;
import ca.tweetzy.skulls.impl.economy.EconomyVault;
import ca.tweetzy.skulls.settings.Settings;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 2:05 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class EconomyManager implements IEconomy {

	@Getter
	private final static EconomyManager instance = new EconomyManager();

	private IEconomy economy;

	private EconomyManager() {
		if (Settings.ECONOMY_PROVIDER.equalsIgnoreCase("vault")) {
			economy = new EconomyVault();
		} else if (Settings.ECONOMY_PROVIDER.equalsIgnoreCase("item")) {
			economy = new EconomyItem();
		}
	}

	@Override
	public String getName() {
		return "EconomyProvider";
	}

	@Override
	public boolean requiresExternalPlugin() {
		return this.economy.requiresExternalPlugin();
	}

	@Override
	public boolean has(@NonNull Player player, double amount) {
		return this.economy.has(player, amount);
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		this.economy.withdraw(player, amount);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		this.economy.deposit(player, amount);
	}
}
