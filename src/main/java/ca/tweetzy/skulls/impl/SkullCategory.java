package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.api.interfaces.ISkullCategory;
import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.model.ConfigSerializable;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:54 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public final class SkullCategory implements ISkullCategory, ConfigSerializable {

	private final String id;

	@Setter
	private String name;

	private final boolean custom;
	private final StrictList<Integer> skulls;

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
		return this.custom;
	}

	@Override
	public StrictList<Integer> getSkulls() {
		return this.skulls;
	}

	@Override
	public SerializedMap serialize() {
		final SerializedMap map = new SerializedMap();
		map.put("id", this.id.toLowerCase());
		map.put("name", this.name);
		map.put("skulls", this.skulls);
		return map;
	}

	public static SkullCategory deserialize(SerializedMap map) {
		return new SkullCategory(
				map.getString("id"),
				map.getString("name"),
				true,
				new StrictList<>(map.getList("skulls", Integer.class))
		);
	}
}
