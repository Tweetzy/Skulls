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

package ca.tweetzy.skulls.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class NumberHelper {

	public int tryInt(String value, int def) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return def;
		}
		return Integer.parseInt(value);
	}

	public boolean isInt(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
}
