package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.api.interfaces.ISkullCategory;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:54 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public final class SkullCategory implements ISkullCategory {

	private final String id;

	@Setter
	private String name;

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
