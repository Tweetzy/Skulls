package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.tweety.collection.StrictList;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Date Created: April 04 2022
 * Time Created: 9:51 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class SkullPlayer implements SkullUser {

	private final UUID uuid;
	private final StrictList<Integer> favourites;

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public StrictList<Integer> getFavourites() {
		return this.favourites;
	}
}
