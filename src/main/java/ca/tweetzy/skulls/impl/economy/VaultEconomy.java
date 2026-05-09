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
import ca.tweetzy.skulls.exception.CurrencyNotFoundException;
import lombok.NonNull;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.math.BigDecimal;
import java.util.function.BiFunction;

/**
 * Date Created: April 21 2022
 * Time Created: 11:15 a.m.
 *
 * @author Kiran Hart
 */
public final class VaultEconomy extends MultiCurrencyEconomy {

	private final String pluginName;
	private Economy economy;
	private BiFunction<Player, BigDecimal, Boolean> hasBalance;
	private BiFunction<Player, BigDecimal, EconomyResponse> withdrawFunds;
	private BiFunction<Player, BigDecimal, EconomyResponse> depositFunds;

	public VaultEconomy() {
		this(null);
	}

	public VaultEconomy(final String currencyName) {
		this.pluginName = Skulls.getInstance().getName();
		this.currencyName = currencyName == null || currencyName.trim().isEmpty() ? null : currencyName.trim();
		setupEconomy();
	}

	private void setupEconomy() {
		final RegisteredServiceProvider<Economy> rsp = Skulls.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			throw new IllegalStateException("Skulls could not find an economy provider for Vault.");

		this.economy = rsp.getProvider();

		if (!this.economy.isEnabled())
			throw new IllegalStateException("Skulls found a Vault economy provider, but it is not enabled.");

		if (this.currencyName == null) {
			this.hasBalance = (player, amount) -> this.economy.has(this.pluginName, player.getUniqueId(), player.getWorld().getName(), amount);
			this.withdrawFunds = (player, amount) -> this.economy.withdraw(this.pluginName, player.getUniqueId(), player.getWorld().getName(), amount);
			this.depositFunds = (player, amount) -> this.economy.deposit(this.pluginName, player.getUniqueId(), player.getWorld().getName(), amount);
			Common.log("&aSetting up vault economy provider");
			return;
		}

		if (!this.economy.hasMultiCurrencySupport())
			throw new IllegalStateException("Vault economy provider does not support multi-currency, but config requested currency: " + this.currencyName);

		if (!this.economy.hasCurrency(this.currencyName))
			throw new CurrencyNotFoundException("Could not find the currency: '" + this.currencyName + "' from " + this.getName() + ", please check spelling or if it even exists.");

		this.hasBalance = (player, amount) -> this.economy.has(this.pluginName, player.getUniqueId(), player.getWorld().getName(), this.currencyName, amount);
		this.withdrawFunds = (player, amount) -> this.economy.withdraw(this.pluginName, player.getUniqueId(), player.getWorld().getName(), this.currencyName, amount);
		this.depositFunds = (player, amount) -> this.economy.deposit(this.pluginName, player.getUniqueId(), player.getWorld().getName(), this.currencyName, amount);
		Common.log("&aSetting up vault economy provider with currency: " + this.currencyName);
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
		final BigDecimal value = BigDecimal.valueOf(amount);
		return this.hasBalance.apply(player, value);
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		final BigDecimal value = BigDecimal.valueOf(amount);
		final EconomyResponse response = this.withdrawFunds.apply(player, value);

		if (!response.transactionSuccess())
			Common.log("&cFailed to withdraw from " + player.getName() + " using Vault economy: " + response.errorMessage);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		final BigDecimal value = BigDecimal.valueOf(amount);
		final EconomyResponse response = this.depositFunds.apply(player, value);

		if (!response.transactionSuccess())
			Common.log("&cFailed to deposit to " + player.getName() + " using Vault economy: " + response.errorMessage);
	}
}
