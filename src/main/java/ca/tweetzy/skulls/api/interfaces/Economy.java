package ca.tweetzy.skulls.api.interfaces;

import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Date Created: April 04 2022
 * Time Created: 9:53 a.m.
 *
 * @author Kiran Hart
 */
public interface Economy {

	/**
	 * The name of the economy provider
	 *
	 * @return the name of the economy provider
	 */
	String getName();

	/**
	 * Only should be true if not using the item economy
	 *
	 * @return
	 */
	boolean requiresExternalPlugin();

	/**
	 * Check whether a player has a specific amount of funds in their account
	 *
	 * @param player is the {@link Player}
	 * @return true if they have enough funds
	 */
	boolean has(@NonNull final Player player, final double amount);

	/**
	 * Withdraw money from a player's account
	 *
	 * @param player is the {@link Player}
	 * @param amount is the amount of money being withdrawn
	 */
	void withdraw(@NonNull final Player player, final double amount);

	/**
	 * Deposit money into a player's account
	 *
	 * @param player is the {@link Player}
	 * @param amount is the amount of money being deposited
	 */
	void deposit(@NonNull final Player player, final double amount);
}
