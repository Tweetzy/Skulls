package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Economy;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Date Created: April 21 2022
 * Time Created: 11:15 a.m.
 *
 * @author Kiran Hart
 */
public final class VaultEconomy implements Economy {

	private net.milkbowl.vault.economy.Economy economy;

	public VaultEconomy() {
		final RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = Skulls.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (rsp == null) {
			Common.log("&cSkulls could not find an economy provider for Vault!");
			return;
		}

		this.economy = rsp.getProvider();
	}

	@Override
	public String getName() {
		return "Vault";
	}

	@Override
	public boolean requiresExternalPlugin() {
		return true;
	}

	@Override
	public boolean has(@NonNull Player player, double amount) {
		return this.economy.has(player, amount);
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		this.economy.withdrawPlayer(player, amount);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		this.economy.depositPlayer(player, amount);
	}
}
