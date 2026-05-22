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

import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Date Created: April 21 2022
 * Time Created: 11:15 a.m.
 *
 * @author Kiran Hart
 */
public final class VaultEconomy extends MultiCurrencyEconomy {

	private final MultiCurrencyEconomy economy;

	public VaultEconomy() {
		this(null);
	}

	public VaultEconomy(final String currencyName) {
		this.economy = currencyName == null || currencyName.trim().isEmpty() ? new LegacyVaultEconomy() : new VaultUnlockedEconomy(currencyName);
		this.currencyName = this.economy.getCurrencyName();
	}

	@Override
	public String getName() {
		return this.economy.getName();
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
