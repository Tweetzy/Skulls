package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.api.interfaces.ISkullPlayer;
import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.model.ConfigSerializable;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:54 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public final class SkullPlayer implements ISkullPlayer, ConfigSerializable {

	private final UUID playerId;
	private final StrictList<Integer> favouriteSkulls;

	@Override
	public UUID getPlayerId() {
		return this.playerId;
	}

	@Override
	public StrictList<Integer> favouriteSkulls() {
		return this.favouriteSkulls;
	}

	@Override
	public SerializedMap serialize() {
		final SerializedMap map = new SerializedMap();
		map.put("uuid", playerId);
		map.put("favourites", favouriteSkulls);
		return map;
	}

	public static SkullPlayer deserialize(SerializedMap map) {
		return new SkullPlayer(
				map.getUUID("uuid"),
				new StrictList<>(map.getList("favourites", Integer.class))
		);
	}
}
