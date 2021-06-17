package ca.tweetzy.skulls.economy;

import ca.tweetzy.skulls.economy.economies.MaterialEconomy;
import ca.tweetzy.skulls.economy.economies.VaultEconomy;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: June 17 2021
 * Time Created: 12:06 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class EconomyManager {

    final Economy economy;

    public EconomyManager() {
        this.economy = Settings.USE_ITEM_ECONOMY.getBoolean() ? new MaterialEconomy() : new VaultEconomy();
    }

    public boolean has(Player player, double amount) {
        return this.economy.has(player, amount);
    }

    public void withdrawPlayer(Player player, double amount) {
        this.economy.withdraw(player, amount);
    }

    public double getBalance(Player player) {
        return this.economy.getBalance(player);
    }
}
