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


import ca.tweetzy.feather.database.DataMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Date Created: April 01 2022
 * Time Created: 11:25 p.m.
 *
 * @author Kiran Hart
 */
public final class _1_InitialMigration extends DataMigration {

	public _1_InitialMigration() {
		super(1);
	}

	@Override
	public void migrate(Connection connection, String tablePrefix) throws SQLException {

		try (Statement statement = connection.createStatement()) {
			statement.execute("CREATE TABLE " + tablePrefix + "history (" +
					"id INTEGER PRIMARY KEY, " +
					"time LONG NOT NULL," +
					"skulls TEXT NULL" +
					")");

			statement.execute("CREATE TABLE " + tablePrefix + "players (" +
					"uuid VARCHAR(36) PRIMARY KEY, " +
					"favourites TEXT" +
					")");

			statement.execute("CREATE TABLE " + tablePrefix + "categories (" +
					"id VARCHAR(64) PRIMARY KEY, " +
					"name VARCHAR(72) NOT NULL, " +
					"skulls TEXT NULL" +
					")");

			statement.execute("CREATE TABLE " + tablePrefix + "skull (" +
					"id INTEGER PRIMARY KEY, " +
					"name VARCHAR(64) NOT NULL, " +
					"category VARCHAR(16) NOT NULL, " +
					"tags VARCHAR(160) NOT NULL, " +
					"texture VARCHAR(144) NOT NULL, " +
					"price DOUBLE NOT NULL, " +
					"blocked BOOLEAN NOT NULL" +
					")");
		}
	}
}
