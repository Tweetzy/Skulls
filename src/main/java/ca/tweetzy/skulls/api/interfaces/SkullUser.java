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

package ca.tweetzy.skulls.api.interfaces;


import java.util.List;
import java.util.UUID;

/**
 * Date Created: April 04 2022
 * Time Created: 8:50 p.m.
 *
 * @author Kiran Hart
 */
public interface SkullUser extends DataSync {

	UUID getUUID();

	List<Integer> getFavourites();

	void toggleFavourite(int skullId);
}
