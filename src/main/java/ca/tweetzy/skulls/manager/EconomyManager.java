package ca.tweetzy.skulls.manager;

import ca.tweetzy.skulls.api.interfaces.Economy;
import ca.tweetzy.skulls.impl.economy.ItemEconomy;
import ca.tweetzy.skulls.impl.economy.VaultEconomy;
import ca.tweetzy.skulls.settings.Settings;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Date Created: April 27 2022
 * Time Created: 11:04 p.m.
 *
 * @author Kiran Hart
 */
@NoArgsConstructor
public final class EconomyManager implements Economy {

	private Economy economy;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean requiresExternalPlugin() {
		return false;
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

	public void init() {
		if (Settings.ECONOMY.getString().equalsIgnoreCase("vault") && Bukkit.getServer().getPluginManager().isPluginEnabled("Vault"))
			this.economy = new VaultEconomy();
		else
			this.economy = new ItemEconomy();
	}
}
