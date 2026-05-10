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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;
import su.nightexpress.excellenteconomy.api.currency.operation.NotificationTarget;
import su.nightexpress.excellenteconomy.api.currency.operation.OperationContext;

public final class ExcellentEconomyEconomy extends MultiCurrencyEconomy {

    private final ExcellentEconomyAPI api;
    private final OperationContext operationContext;

    public ExcellentEconomyEconomy(@NonNull final String currencyName) {
        this.currencyName = currencyName;

        final RegisteredServiceProvider<ExcellentEconomyAPI> provider = Bukkit.getServer().getServicesManager().getRegistration(ExcellentEconomyAPI.class);
        if (provider == null)
            throw new CurrencyNotFoundException("Could not hook into ExcellentEconomy API.");

        this.api = provider.getProvider();
        if (!this.api.hasCurrency(this.currencyName))
            throw new CurrencyNotFoundException("Could not find the currency: '" + this.currencyName + "' from " + this.getName() + ", please check spelling or if it even exists.");

        this.operationContext = OperationContext.custom(Skulls.getInstance().getName()).silentFor(NotificationTarget.USER, NotificationTarget.EXECUTOR);
        Common.log("&aSetting up ExcellentEconomy economy provider");
    }

    @Override
    public String getName() {
        return "ExcellentEconomy";
    }

    @Override
    public boolean requiresExternalPlugin() {
        return true;
    }

    @Override
    public boolean has(@NonNull Player player, double amount) {
        if (this.currencyName == null)
            return false;

        double balance = this.api.getBalance(player, this.currencyName);

        return balance >= amount;
    }

    @Override
    public void withdraw(@NonNull Player player, double amount) {
        if (this.currencyName == null)
            return;

        if (!this.api.canPerformOperations())
            return;

        if (!this.api.withdraw(player, this.currencyName, amount, this.operationContext))
            Common.log("&cFailed to withdraw from " + player.getName() + " using ExcellentEconomy currency: " + this.currencyName);
    }

    @Override
    public void deposit(@NonNull Player player, double amount) {
        if (this.currencyName == null)
            return;

        if (!this.api.canPerformOperations())
            return;

        if (!this.api.deposit(player, this.currencyName, amount, this.operationContext))
            Common.log("&cFailed to deposit to " + player.getName() + " using ExcellentEconomy currency: " + this.currencyName);
    }
}
