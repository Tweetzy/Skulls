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

import ca.tweetzy.skulls.api.interfaces.History;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Date Created: April 05 2022
 * Time Created: 1:46 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class InsertHistory implements History {

	private final int id;
	private final long time;
	private final List<Integer> skulls;

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public List<Integer> getSkulls() {
		return this.skulls;
	}
}
