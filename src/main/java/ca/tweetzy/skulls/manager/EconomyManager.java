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

package ca.tweetzy.skulls.manager;

import ca.tweetzy.skulls.api.interfaces.Economy;
import ca.tweetzy.skulls.impl.economy.ItemEconomy;
import ca.tweetzy.skulls.impl.economy.UltraEconomyEconomy;
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
		if (Settings.ECONOMY.getString().equalsIgnoreCase("vault") && Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
			this.economy = new VaultEconomy();
		} else if (Settings.ECONOMY.getString().startsWith("UltraEconomy:") && Bukkit.getServer().getPluginManager().isPluginEnabled("UltraEconomy")) {
			final String[] ultraEconomyCurrencyName = Settings.ECONOMY.getString().split(":");

			if (ultraEconomyCurrencyName.length < 2) {
				this.economy = new ItemEconomy();
				return;
			}

			this.economy = new UltraEconomyEconomy(ultraEconomyCurrencyName[1]);

		} else
			this.economy = new ItemEconomy();
	}
}
