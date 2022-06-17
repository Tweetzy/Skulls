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

import ca.tweetzy.skulls.api.interfaces.PlacedSkull;
import lombok.AllArgsConstructor;
import org.bukkit.Location;

import java.util.UUID;

@AllArgsConstructor
public final class PlacedSkullLocation implements PlacedSkull {

	private final UUID id;
	private final int skullId;
	private final Location location;

	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public int getSkullId() {
		return this.skullId;
	}

	@Override
	public Location getLocation() {
		return this.location;
	}
}
