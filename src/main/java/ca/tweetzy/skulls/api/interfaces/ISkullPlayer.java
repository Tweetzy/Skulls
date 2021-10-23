package ca.tweetzy.skulls.api.interfaces;

import ca.tweetzy.tweety.collection.StrictList;

import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:48 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface ISkullPlayer {

	/**
	 * The {@link UUID} of the {@link org.bukkit.entity.Player}
	 *
	 * @return the {@link UUID} of the {@link org.bukkit.entity.Player}
	 */
	UUID getPlayerId();

	/**
	 * A list of skull heads that the player
	 * has added to their favourites list
	 *
	 * @return a {@link StrictList<Integer>} of skull ids
	 */
	StrictList<Integer> favouriteSkulls();
}
