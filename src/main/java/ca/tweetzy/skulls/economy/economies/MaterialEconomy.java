package ca.tweetzy.skulls.economy.economies;

import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.economy.Economy;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * The current file has been created by Kiran Hart
 * Date Created: June 17 2021
 * Time Created: 12:10 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class MaterialEconomy implements Economy {

    @Override
    public boolean has(Player player, double amount) {
        int flatAmount = (int) Math.round(amount);
        return SkullAPI.getInstance().getItemCountInPlayerInventory(player, Objects.requireNonNull(Settings.ITEM_ECONOMY_MATERIAL.getMaterial().parseItem())) >= flatAmount;
    }

    @Override
    public void withdraw(Player player, double amount) {
        int flatAmount = (int) Math.round(amount);
        SkullAPI.getInstance().removeSpecificItemQuantityFromPlayer(player, Objects.requireNonNull(Settings.ITEM_ECONOMY_MATERIAL.getMaterial().parseItem()), flatAmount);
    }

    @Override
    public double getBalance(Player player) {
        return SkullAPI.getInstance().getItemCountInPlayerInventory(player, Objects.requireNonNull(Settings.ITEM_ECONOMY_MATERIAL.getMaterial().parseItem()));
    }
}
