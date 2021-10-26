package ca.tweetzy.skulls.model;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.SerializeUtil;
import ca.tweetzy.tweety.collection.StrictMap;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 10:13 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class SkullPlayerManager {

	@Getter
	private final StrictMap<UUID, SkullPlayer> players = new StrictMap<>();

	public void addPlayer(@NonNull final SkullPlayer skullPlayer) {
		this.players.put(skullPlayer.getPlayerId(), skullPlayer);
		loadPlayer(skullPlayer.getPlayerId());
	}

	public void removePlayer(@NonNull final SkullPlayer skullPlayer) {
		this.players.remove(skullPlayer.getPlayerId());
		savePlayer(skullPlayer);
	}

	public void removePlayer(@NonNull final UUID uuid) {
		this.players.remove(uuid);
	}

	public SkullPlayer getPlayer(@NonNull final UUID uuid) {
		return this.players.getOrDefault(uuid, null);
	}

	public void savePlayer(@NonNull final SkullPlayer skullPlayer) {
		Skulls.getInstance().getDataFile().setField("Players." + skullPlayer.getPlayerId().toString(), SerializeUtil.serialize(skullPlayer));
	}

	public void savePlayers() {
		this.players.forEachIterate((id, player) -> savePlayer(player));
	}

	public void loadPlayer(@NonNull final UUID playerId) {
		Common.runAsync(() -> {
			if (!Skulls.getInstance().getDataFile().contains("Players." + playerId.toString())) return;
			SkullPlayer skullPlayer = SerializeUtil.deserialize(SkullPlayer.class, Skulls.getInstance().getDataFile().getConfigField("Players." + playerId.toString()));
			if (skullPlayer != null)
				this.players.get(playerId).favouriteSkulls().addAll(skullPlayer.favouriteSkulls());
			skullPlayer = null;
		});
	}
}
