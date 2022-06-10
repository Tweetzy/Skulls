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
import ca.tweetzy.skulls.api.interfaces.Category;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Date Created: April 20 2022
 * Time Created: 9:48 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class SkullCategory implements Category {

	private final String id;
	private final String name;
	private final boolean isCustom;
	private final List<Integer> skulls;

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
		return this.isCustom;
	}

	@Override
	public List<Integer> getSkulls() {
		return this.skulls;
	}

	@Override
	public void sync() {
		Skulls.getDataManager().updateCategory(this, null);
	}
}
