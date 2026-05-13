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
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Date Created: April 21 2022
 * Time Created: 11:15 a.m.
 *
 * @author Kiran Hart
 */
public final class VaultEconomy extends MultiCurrencyEconomy {

	private final String pluginName;
	private BalanceCheck hasBalance;
	private TransactionAction withdrawFunds;
	private TransactionAction depositFunds;

	public VaultEconomy() {
		this(null);
	}

	public VaultEconomy(final String currencyName) {
		this.pluginName = Skulls.getInstance().getName();
		this.currencyName = currencyName == null || currencyName.trim().isEmpty() ? null : currencyName.trim();
		setupEconomy();
	}

	private void setupEconomy() {
		if (this.currencyName != null) {
			setupVaultUnlockedEconomy();
			return;
		}

		setupLegacyEconomy();
	}

	private void setupLegacyEconomy() {
		final RegisteredServiceProvider<Economy> rsp = Skulls.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			throw new IllegalStateException("Skulls could not find an economy provider for Vault.");

		final Economy economy = rsp.getProvider();

		if (!economy.isEnabled())
			throw new IllegalStateException("Skulls found a Vault economy provider, but it is not enabled.");

		this.hasBalance = (player, amount) -> economy.has(player, amount.doubleValue());
		this.withdrawFunds = (player, amount) -> {
			final EconomyResponse response = economy.withdrawPlayer(player, amount.doubleValue());
			return new TransactionResult(response.transactionSuccess(), response.errorMessage);
		};
		this.depositFunds = (player, amount) -> {
			final EconomyResponse response = economy.depositPlayer(player, amount.doubleValue());
			return new TransactionResult(response.transactionSuccess(), response.errorMessage);
		};
		Common.log("&aSetting up vault economy provider");
	}

	private void setupVaultUnlockedEconomy() {
		final Plugin vaultPlugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");
		if (vaultPlugin == null)
			throw new IllegalStateException("VaultUnlocked multi-currency requires VaultUnlocked to be installed.");

		try {
			final Class<?> economyClass = Class.forName("net.milkbowl.vault2.economy.Economy", false, vaultPlugin.getClass().getClassLoader());
			final RegisteredServiceProvider<?> rsp = Skulls.getInstance().getServer().getServicesManager().getRegistration(economyClass);

			if (rsp == null)
				throw new IllegalStateException("Skulls could not find a VaultUnlocked economy provider.");

			final Object economy = rsp.getProvider();
			final Method isEnabled = economyClass.getMethod("isEnabled");
			final Method hasMultiCurrencySupport = economyClass.getMethod("hasMultiCurrencySupport");
			final Method hasCurrency = economyClass.getMethod("hasCurrency", String.class);
			final Method has = economyClass.getMethod("has", String.class, UUID.class, String.class, BigDecimal.class);
			final Method withdraw = economyClass.getMethod("withdraw", String.class, UUID.class, String.class, BigDecimal.class);
			final Method deposit = economyClass.getMethod("deposit", String.class, UUID.class, String.class, BigDecimal.class);

			if (!((Boolean) isEnabled.invoke(economy)))
				throw new IllegalStateException("Skulls found a VaultUnlocked economy provider, but it is not enabled.");

			if (!((Boolean) hasMultiCurrencySupport.invoke(economy)))
				throw new IllegalStateException("Vault economy provider does not support multi-currency, but config requested currency: " + this.currencyName);

			if (!((Boolean) hasCurrency.invoke(economy, this.currencyName)))
				throw new CurrencyNotFoundException("Could not find the currency: '" + this.currencyName + "' from " + this.getName() + ", please check spelling or if it even exists.");

			this.hasBalance = (player, amount) -> (Boolean) has.invoke(economy, this.pluginName, player.getUniqueId(), this.currencyName, amount);
			this.withdrawFunds = (player, amount) -> toTransactionResult(withdraw.invoke(economy, this.pluginName, player.getUniqueId(), this.currencyName, amount));
			this.depositFunds = (player, amount) -> toTransactionResult(deposit.invoke(economy, this.pluginName, player.getUniqueId(), this.currencyName, amount));
			Common.log("&aSetting up vault economy provider with currency: " + this.currencyName);
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException("VaultUnlocked multi-currency requires VaultUnlocked. Install VaultUnlocked or change economy away from vault:" + this.currencyName, ex);
		} catch (ReflectiveOperationException ex) {
			throw new IllegalStateException("Could not hook into VaultUnlocked economy.", ex);
		}
	}

	private TransactionResult toTransactionResult(final Object response) throws ReflectiveOperationException {
		final Method transactionSuccess = response.getClass().getMethod("transactionSuccess");
		final boolean success = (Boolean) transactionSuccess.invoke(response);
		final Object errorMessage = response.getClass().getField("errorMessage").get(response);

		return new TransactionResult(success, errorMessage == null ? null : errorMessage.toString());
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
		try {
			return this.hasBalance.apply(player, value);
		} catch (ReflectiveOperationException ex) {
			Common.log("&cFailed to check " + player.getName() + "'s balance using Vault economy: " + ex.getMessage());
			return false;
		}
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		final BigDecimal value = BigDecimal.valueOf(amount);
		final TransactionResult response;
		try {
			response = this.withdrawFunds.apply(player, value);
		} catch (ReflectiveOperationException ex) {
			Common.log("&cFailed to withdraw from " + player.getName() + " using Vault economy: " + ex.getMessage());
			return;
		}

		if (!response.success())
			Common.log("&cFailed to withdraw from " + player.getName() + " using Vault economy: " + response.errorMessage());
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		final BigDecimal value = BigDecimal.valueOf(amount);
		final TransactionResult response;
		try {
			response = this.depositFunds.apply(player, value);
		} catch (ReflectiveOperationException ex) {
			Common.log("&cFailed to deposit to " + player.getName() + " using Vault economy: " + ex.getMessage());
			return;
		}

		if (!response.success())
			Common.log("&cFailed to deposit to " + player.getName() + " using Vault economy: " + response.errorMessage());
	}

	@FunctionalInterface
	private interface BalanceCheck {

		boolean apply(Player player, BigDecimal amount) throws ReflectiveOperationException;
	}

	@FunctionalInterface
	private interface TransactionAction {

		TransactionResult apply(Player player, BigDecimal amount) throws ReflectiveOperationException;
	}

	private record TransactionResult(boolean success, String errorMessage) {
	}
}
