package ca.tweetzy.skulls.economy;

import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: June 17 2021
 * Time Created: 12:07 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface Economy {

    boolean has(Player player, double amount);

    void withdraw(Player player, double amount);

    double getBalance(Player player);
}
