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

package ca.tweetzy.skulls.database;

import ca.tweetzy.feather.database.Callback;
import ca.tweetzy.feather.database.DataManagerAbstract;
import ca.tweetzy.feather.database.DatabaseConnector;
import ca.tweetzy.feather.database.UpdateCallback;
import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.skulls.model.Serialize;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.*;
import ca.tweetzy.skulls.impl.*;
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
import java.util.UUID;
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
			PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO " + this.getTablePrefix() + "skull(id, name, category, texture, tags, price, blocked) VALUES(?, ?, ?, ?, ?, ?, ?)");
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

	public void insertPlacedSkull(@NonNull final PlacedSkull placedSkull, Callback<PlacedSkull> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + this.getTablePrefix() + "placed_skull (id, skull_id, location) VALUES(?, ?, ?)")) {
				PreparedStatement fetch = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "placed_skull WHERE id = ?");

				fetch.setString(1, placedSkull.getId().toString());
				statement.setString(1, placedSkull.getId().toString());
				statement.setInt(2, placedSkull.getSkullId());
				statement.setString(3, Serialize.serializeLocation(placedSkull.getLocation()));
				statement.executeUpdate();

				if (callback != null) {
					ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractPlacedSkull(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void getPlacedSkulls(Callback<ArrayList<PlacedSkull>> callback) {
		ArrayList<PlacedSkull> placedSkulls = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "placed_skull")) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					placedSkulls.add(extractPlacedSkull(resultSet));
				}

				callback.accept(null, placedSkulls);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void deletePlacedSkull(final UUID id, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "placed_skull WHERE id = ?")) {
				statement.setString(1, id.toString());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
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

	public void syncSkullPricesByCategory(Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final PreparedStatement statement = connection.prepareStatement("UPDATE " + this.getTablePrefix() + "skull SET price = ? WHERE category = ?");

			for (BaseCategory value : BaseCategory.values()) {
				statement.setDouble(1, value.getDefaultPrice());
				statement.setString(2, value.getId());
				statement.addBatch();
			}

			int[] updated = statement.executeBatch();
			Common.log("updated" + updated.length);

			if (callback != null)
				this.sync(() -> callback.accept(null, updated.length > 0));
		}));
	}

	public void getCategories(Callback<ArrayList<Category>> callback) {
		ArrayList<Category> categories = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "categories")) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					categories.add(extractCategory(resultSet));
				}

				callback.accept(null, categories);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void getPlayers(Callback<ArrayList<SkullUser>> callback) {
		ArrayList<SkullUser> skulls = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "players")) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					skulls.add(extractSkullPlayer(resultSet));
				}

				this.sync(() -> callback.accept(null, skulls));
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void insertPlayer(@NonNull final SkullUser user, Callback<SkullUser> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + this.getTablePrefix() + "players (uuid, favourites) VALUES(?, ?)")) {
				PreparedStatement fetch = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "players WHERE uuid = ?");

				fetch.setString(1, user.getUUID().toString());
				statement.setString(1, user.getUUID().toString());
				statement.setString(2, user.getFavourites().stream().map(String::valueOf).collect(Collectors.joining(",")));
				statement.executeUpdate();

				if (callback != null) {
					ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractSkullPlayer(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void insertCategory(@NonNull final Category category, Callback<Category> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + this.getTablePrefix() + "categories (id, name, skulls) VALUES(?, ?, ?)")) {
				PreparedStatement fetch = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "categories WHERE id = ?");

				fetch.setString(1, category.getId());
				statement.setString(1, category.getId());
				statement.setString(2, category.getName());
				statement.setString(3, category.getSkulls().stream().map(String::valueOf).collect(Collectors.joining(",")));
				statement.executeUpdate();

				if (callback != null) {
					ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractCategory(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void updateCategory(@NonNull final Category category, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("UPDATE " + this.getTablePrefix() + "categories SET name = ?, skulls = ? WHERE id = ?")) {

				statement.setString(1, category.getName());
				statement.setString(2, category.getSkulls().stream().map(String::valueOf).collect(Collectors.joining(",")));
				statement.setString(3, category.getId());

				int result = statement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void updateSkull(@NonNull final Skull skull, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("UPDATE " + this.getTablePrefix() + "skull SET name = ?, price = ?, blocked = ? WHERE id = ?")) {

				statement.setString(1, skull.getName());
				statement.setDouble(2, skull.getPrice());
				statement.setBoolean(3, skull.isBlocked());
				statement.setInt(4, skull.getId());

				int result = statement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void updatePlayer(@NonNull final SkullUser user, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("UPDATE " + this.getTablePrefix() + "players SET favourites = ? WHERE uuid = ?")) {

				statement.setString(1, user.getFavourites().stream().map(String::valueOf).collect(Collectors.joining(",")));
				statement.setString(2, user.getUUID().toString());

				int result = statement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

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

	public Category extractCategory(@NonNull final ResultSet resultSet) throws SQLException {
		final String skulls = resultSet.getString("skulls");
		final String[] split = skulls.split(",");

		return new SkullCategory(
				resultSet.getString("id"),
				resultSet.getString("name"),
				true,
				skulls.length() == 0 || split.length == 0 ? new ArrayList<>() : Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList())
		);
	}

	public History extractHistory(@NonNull final ResultSet resultSet) throws SQLException {
		return new InsertHistory(
				resultSet.getInt("id"),
				resultSet.getLong("time"),
				Arrays.stream(resultSet.getString("skulls").split(",")).map((Integer::parseInt)).collect(Collectors.toList())
		);
	}

	public PlacedSkull extractPlacedSkull(@NonNull final ResultSet resultSet) throws SQLException {
		return new PlacedSkullLocation(
				UUID.fromString(resultSet.getString("id")),
				resultSet.getInt("skull_id"),
				Serialize.deserializeLocation(resultSet.getString("location"))
		);
	}


	public SkullUser extractSkullPlayer(@NonNull final ResultSet resultSet) throws SQLException {
		final String favs = resultSet.getString("favourites");
		final String[] split = favs.split(",");

		return new SkullPlayer(
				UUID.fromString(resultSet.getString("uuid")),
				favs.length() == 0 || split.length == 0 ? new ArrayList<>() : Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList())
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
