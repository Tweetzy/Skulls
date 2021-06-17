package ca.tweetzy.skulls.economy.economies;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.economy.Economy;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: June 17 2021
 * Time Created: 12:08 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VaultEconomy implements Economy {

    @Override
    public boolean has(Player player, double amount) {
        return Skulls.getInstance().getEconomy().has(player, amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        Skulls.getInstance().getEconomy().withdrawPlayer(player, amount);
    }

    @Override
    public double getBalance(Player player) {
        return Skulls.getInstance().getEconomy().getBalance(player);
    }
}
