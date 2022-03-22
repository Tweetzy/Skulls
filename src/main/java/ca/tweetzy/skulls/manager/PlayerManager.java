package ca.tweetzy.skulls.manager;

import ca.tweetzy.skulls.api.interfaces.SkullUser;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date Created: April 04 2022
 * Time Created: 8:48 p.m.
 *
 * @author Kiran Hart
 */
public final class PlayerManager {

	private final Map<UUID, SkullUser> players = new ConcurrentHashMap<>();

	public void addPlayer(@NonNull final SkullUser skullUser) {
		this.players.put(skullUser.getUUID(), skullUser);
	}

	public void removePlayer(@NonNull final UUID uuid) {
		this.players.remove(uuid);
	}

	public SkullUser findPlayer(@NonNull final Player player) {
		return this.findPlayer(player.getUniqueId());
	}

	public SkullUser findPlayer(@NonNull final UUID uuid) {
		return this.players.getOrDefault(uuid, null);
	}
}
