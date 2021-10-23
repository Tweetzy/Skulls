package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.skulls.api.interfaces.IEconomy;
import ca.tweetzy.tweety.model.HookManager;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 1:57 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class EconomyVault implements IEconomy {

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
		return HookManager.isVaultLoaded() && HookManager.getBalance(player) >= amount;
	}

	@Override
	public void withdraw(@NonNull Player player, double amount) {
		HookManager.withdraw(player, amount);
	}

	@Override
	public void deposit(@NonNull Player player, double amount) {
		HookManager.deposit(player, amount);
	}
}
