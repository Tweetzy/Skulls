/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.manager;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.skulls.impl.SkullPlayer;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

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
		final AtomicReference<SkullUser> skullUser = new AtomicReference<>(this.players.getOrDefault(uuid, null));

		if (skullUser.get() != null) return skullUser.get();

		Skulls.getDataManager().insertPlayer(new SkullPlayer(uuid, new ArrayList<>()), (createError, created) -> {
			if (createError == null) {
				addPlayer(created);
				skullUser.set(created);
			}
		});

		return skullUser.get();
	}

	public void load() {
		this.players.clear();
		Skulls.getDataManager().getPlayers((error, loaded) -> {
			if (error == null)
				loaded.forEach(this::addPlayer);
			else
				error.printStackTrace();
		});
	}
}
