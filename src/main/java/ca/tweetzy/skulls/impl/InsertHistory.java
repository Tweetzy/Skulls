package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.api.interfaces.History;
import ca.tweetzy.tweety.collection.StrictList;
import lombok.AllArgsConstructor;

/**
 * Date Created: April 05 2022
 * Time Created: 1:46 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class InsertHistory implements History {

	private final int id;
	private final long time;
	private final StrictList<Integer> skulls;

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public StrictList<Integer> getSkulls() {
		return this.skulls;
	}
}
