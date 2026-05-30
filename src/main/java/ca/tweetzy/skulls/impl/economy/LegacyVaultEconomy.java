/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.skulls.Skulls;
import lombok.NonNull;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

final class LegacyVaultEconomy extends MultiCurrencyEconomy {

	private final Economy economy;

	LegacyVaultEconomy() {
		final RegisteredServiceProvider<Economy> rsp = Skulls.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			throw new IllegalStateException("Skulls could not find an economy provider for Vault.");

		this.economy = rsp.getProvider();

		if (!this.economy.isEnabled())
			throw new IllegalStateException("Skulls found a Vault economy provider, but it is not enabled.");

		Common.log("&aSetting up vault economy provider");
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
		try {
			return this.economy.has(player, amount);
		} catch (RuntimeException ex) {
			Common.log("&cFailed to check " + player.getName() + "'s balance using Vault economy: " + ex.getMessage());
			return false;
		}
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		final EconomyResponse response;
		try {
			response = this.economy.withdrawPlayer(player, amount);
		} catch (RuntimeException ex) {
			Common.log("&cFailed to withdraw from " + player.getName() + " using Vault economy: " + ex.getMessage());
			return;
		}

		if (!response.transactionSuccess())
			Common.log("&cFailed to withdraw from " + player.getName() + " using Vault economy: " + response.errorMessage);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		final EconomyResponse response;
		try {
			response = this.economy.depositPlayer(player, amount);
		} catch (RuntimeException ex) {
			Common.log("&cFailed to deposit to " + player.getName() + " using Vault economy: " + ex.getMessage());
			return;
		}

		if (!response.transactionSuccess())
			Common.log("&cFailed to deposit to " + player.getName() + " using Vault economy: " + response.errorMessage);
	}
}
