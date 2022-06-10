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

package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Date Created: April 04 2022
 * Time Created: 9:51 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class SkullPlayer implements SkullUser {

	private final UUID uuid;
	private final List<Integer> favourites;

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public List<Integer> getFavourites() {
		return this.favourites;
	}

	@Override
	public void toggleFavourite(int skullId) {
		if (this.favourites.contains(skullId))
			this.favourites.remove(Integer.valueOf(skullId));
		else
			this.favourites.add(skullId);
	}

	@Override
	public void sync() {
		Skulls.getDataManager().updatePlayer(this, null);
	}
}
