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

package ca.tweetzy.skulls.database.migrations;


import ca.tweetzy.flight.database.DataMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Date Created: April 01 2022
 * Time Created: 11:25 p.m.
 *
 * @author Kiran Hart
 */
public final class _3_HistoryRemovalMigration extends DataMigration {

	public _3_HistoryRemovalMigration() {
		super(3);
	}

	@Override
	public void migrate(Connection connection, String tablePrefix) throws SQLException {

		try (Statement statement = connection.createStatement()) {
			statement.execute("DROP TABLE IF EXISTS " + tablePrefix + "history");
		}
	}
}
