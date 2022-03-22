package ca.tweetzy.skulls.api.interfaces;

import ca.tweetzy.tweety.collection.StrictList;

/**
 * Date Created: April 04 2022
 * Time Created: 9:50 p.m.
 *
 * @author Kiran Hart
 */
public interface History {

	int getID();

	long getTime();

	StrictList<Integer> getSkulls();
}
