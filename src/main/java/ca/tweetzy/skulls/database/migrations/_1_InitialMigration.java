package ca.tweetzy.skulls.database.migrations;


import ca.tweetzy.tweety.database.DataMigration;

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
