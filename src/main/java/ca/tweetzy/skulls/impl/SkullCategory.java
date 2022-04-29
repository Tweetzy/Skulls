package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.api.interfaces.Category;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Date Created: April 20 2022
 * Time Created: 9:48 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class SkullCategory implements Category {

	private final String id;
	private final String name;
	private final boolean isCustom;
	private final List<Integer> skulls;

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isCustom() {
		return this.isCustom;
	}

	@Override
	public List<Integer> getSkulls() {
		return this.skulls;
	}

	@Override
	public void sync() {

	}
}
