package ca.tweetzy.skulls.database;

import ca.tweetzy.rose.database.Callback;
import ca.tweetzy.rose.database.DataManagerAbstract;
import ca.tweetzy.rose.database.DatabaseConnector;
import ca.tweetzy.rose.database.UpdateCallback;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.History;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.impl.InsertHistory;
import ca.tweetzy.skulls.impl.TexturedSkull;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Date Created: April 01 2022
 * Time Created: 11:23 p.m.
 *
 * @author Kiran Hart
 */
public final class DataManager extends DataManagerAbstract {

	public DataManager(DatabaseConnector databaseConnector, Plugin plugin) {
		super(databaseConnector, plugin);
	}

	public void insertSkulls(Collection<Skull> skulls) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {

			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + this.getTablePrefix() + "skull(id, name, category, texture, tags, price, blocked) VALUES(?, ?, ?, ?, ?, ?, ?)");
			for (Skull skull : skulls) {
				statement.setInt(1, skull.getId());
				statement.setString(2, skull.getName());
				statement.setString(3, skull.getCategory());
				statement.setString(4, skull.getTexture());
				statement.setString(5, String.join(",", skull.getTags()));
				statement.setDouble(6, skull.getPrice());
				statement.setBoolean(7, skull.isBlocked());
				statement.addBatch();
			}

			statement.executeBatch();
			Skulls.getInstance().getSkullManager().setDownloading(false);
			Common.broadcast("&r&aFinished inserting all heads into the data file!");
		}));
	}

	public void insertHistories(Collection<History> histories) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + this.getTablePrefix() + "history(id, time, skulls) VALUES(?, ?, ?)");
			for (History history : histories) {
				statement.setInt(1, history.getID());
				statement.setLong(2, history.getTime());
				statement.setString(3, history.getSkulls().stream().map(String::valueOf).collect(Collectors.joining(",")));
				statement.addBatch();
			}

			statement.executeBatch();
		}));
	}

	public void insertHistory(@NonNull final History history, Callback<History> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + this.getTablePrefix() + "history (id, time, skulls) VALUES(?, ?, ?)")) {
				PreparedStatement fetch = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "history WHERE id = ?");

				fetch.setInt(1, history.getID());
				statement.setInt(1, history.getID());
				statement.setLong(2, history.getTime());
				statement.setString(3, history.getSkulls().stream().map(String::valueOf).collect(Collectors.joining(",")));
				statement.executeUpdate();

				if (callback != null) {
					ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractHistory(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void getHistories(Callback<ArrayList<History>> callback) {
		ArrayList<History> histories = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "history")) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					histories.add(extractHistory(resultSet));
				}

				callback.accept(null, histories);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	//id, name, category, tags, price, blocked
	public void getSkulls(Callback<ArrayList<Skull>> callback) {
		ArrayList<Skull> skulls = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "skull")) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					skulls.add(extractSkull(resultSet));
				}
				this.sync(() -> callback.accept(null, skulls));
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public Skull extractSkull(@NonNull final ResultSet resultSet) throws SQLException {
		return new TexturedSkull(
				resultSet.getInt("id"),
				resultSet.getString("name"),
				resultSet.getString("category"),
				Arrays.asList(resultSet.getString("tags").split(",")),
				resultSet.getString("texture"),
				resultSet.getDouble("price"),
				resultSet.getBoolean("blocked")
		);
	}

	public History extractHistory(@NonNull final ResultSet resultSet) throws SQLException {
		return new InsertHistory(
				resultSet.getInt("id"),
				resultSet.getLong("time"),
				Arrays.stream(resultSet.getString("skulls").split(",")).map((Integer::parseInt)).collect(Collectors.toList())
		);
	}

	private void resolveUpdateCallback(@Nullable UpdateCallback callback, @Nullable Exception ex) {
		if (callback != null) {
			callback.accept(ex);
		} else if (ex != null) {
			ex.printStackTrace();
		}
	}

	private void resolveCallback(@Nullable Callback<?> callback, @NotNull Exception ex) {
		if (callback != null) {
			callback.accept(ex, null);
		} else {
			ex.printStackTrace();
		}
	}

	private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		for (int x = 1; x <= columns; x++) {
			if (columnName.equals(rsmd.getColumnName(x))) {
				return true;
			}
		}
		return false;
	}
}
