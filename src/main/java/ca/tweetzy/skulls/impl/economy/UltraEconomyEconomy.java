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

import ca.tweetzy.skulls.exception.CurrencyNotFoundException;
import lombok.NonNull;
import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.objects.Account;
import me.TechsCode.UltraEconomy.objects.Currency;
import org.bukkit.entity.Player;

public final class UltraEconomyEconomy extends MultiCurrencyEconomy {

	private final Currency currency;

	public UltraEconomyEconomy(@NonNull final String currentName) {
		this.currencyName = currentName;
		this.currency = UltraEconomy.getAPI().getCurrencies().name(this.currencyName).orElse(null);

		if (this.currency == null)
			throw new CurrencyNotFoundException("Could not find the currency: '" + this.currencyName + "' from " + this.getName() + ", please check spelling or if it even exists.");
	}

	@Override
	public String getName() {
		return "UltraEconomy";
	}

	@Override
	public boolean requiresExternalPlugin() {
		return true;
	}

	@Override
	public boolean has(@NonNull Player player, double amount) {
		final Account account = UltraEconomy.getAPI().getAccounts().uuid(player.getUniqueId()).orElse(null);

		if (account == null || this.currencyName == null)
			return false;

		return account.getBalance(this.currency).getSum() >= amount;
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		final Account account = UltraEconomy.getAPI().getAccounts().uuid(player.getUniqueId()).orElse(null);

		if (account == null || this.currencyName == null)
			return;

		final float oldAmount = account.getBalance(this.currency).getOnBank();
		account.getBalance(this.currency).setBank(oldAmount - (float) amount);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		final Account account = UltraEconomy.getAPI().getAccounts().uuid(player.getUniqueId()).orElse(null);

		if (account == null || this.currencyName == null)
			return;

		account.getBalance(this.currency).addBank((float) amount);
	}
}
