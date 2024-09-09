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

import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
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
		return this.players.get(uuid);
	}

	public SkullUser findOrCreate(@NonNull final Player player) {
		return findOrCreate(player.getUniqueId());
	}

	public SkullUser findOrCreate(@NonNull final UUID uuid) {
		final SkullUser skullUser = findPlayer(uuid);
		final SkullUser blank = new SkullPlayer(uuid, new ArrayList<>());

		if (skullUser == null) {
			Skulls.getDataManager().insertPlayer(new SkullPlayer(uuid, new ArrayList<>()), (error, result) -> {
				SkullUser toAdd = blank;

				if (error == null && result != null)
					toAdd = result;

				addPlayer(toAdd);
			});

			return blank;
		}

		return skullUser;
	}

	public boolean playerCanClaim(@NonNull final Player player) {
		if (!Settings.CLAIM_DELAY_ENABLED.getBoolean()) return true;
		List<Integer> possibleTimes = new ArrayList<>();

		Settings.CLAIM_DELAY_PERMS.getStringList().forEach(line -> {
			String[] split = line.split(":");
			if (player.hasPermission("skulls.claimdelay." + split[0])) {
				possibleTimes.add(Integer.parseInt(split[1]));
			}
		});

		if (possibleTimes.isEmpty()) return true;
		int maxSecs = Collections.max(possibleTimes);
		long currentMillis = System.currentTimeMillis();

		if (!player.getPersistentDataContainer().has(Skulls.getClaimDelayKey())) {
			// set the time
			final long newMillis = currentMillis + (maxSecs * 1000L);
			player.getPersistentDataContainer().set(Skulls.getClaimDelayKey(), PersistentDataType.LONG, newMillis);
			return true;
		}

		final long lastClaimedAt = player.getPersistentDataContainer().get(Skulls.getClaimDelayKey(), PersistentDataType.LONG);
		if (lastClaimedAt > currentMillis) {
			Common.tell(player, TranslationManager.string(Translations.CLAIM_DELAY, "time_difference", getFriendlyTimeDifference(currentMillis,lastClaimedAt)));
			return false;
		} else {
			final long newMillis = currentMillis + (maxSecs * 1000L);
			player.getPersistentDataContainer().set(Skulls.getClaimDelayKey(), PersistentDataType.LONG, newMillis);
		}

		return true;
	}

	private String getFriendlyTimeDifference(long startMillis, long endMillis) {
		long diffMillis = endMillis - startMillis;
		long seconds = diffMillis / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		StringBuilder result = new StringBuilder();

		if (days > 0) {
			result.append(days).append("d ");
			hours %= 24;
		}
		if (hours > 0 || result.length() > 0) {
			result.append(hours).append("h ");
			minutes %= 60;
		}
		if (minutes > 0 || result.length() > 0) {
			result.append(minutes).append("m ");
			seconds %= 60;
		}
		result.append(seconds).append("s");

		return result.toString().trim();
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
