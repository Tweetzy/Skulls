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

import ca.tweetzy.flight.database.Callback;
import ca.tweetzy.flight.database.DataManagerAbstract;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.UpdateCallback;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.SerializeUtil;
import ca.tweetzy.flight.database.query.QueryBuilder;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.Category;
import ca.tweetzy.skulls.api.interfaces.PlacedSkull;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.skulls.impl.PlacedSkullLocation;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.impl.SkullPlayer;
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
		final int batchSize = 1000;
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			connection.setAutoCommit(false);

			PreparedStatement statement = connection.prepareStatement(
					"INSERT OR IGNORE INTO " + this.getTablePrefix() + "skull(id, name, category, texture, tags, price, blocked) VALUES(?, ?, ?, ?, ?, ?, ?)"
			);

			int count = 0;
			for (Skull skull : skulls) {
				statement.setInt(1, skull.getId());
				statement.setString(2, skull.getName());
				statement.setString(3, skull.getCategory());
				statement.setString(4, skull.getTexture());
				statement.setString(5, String.join(",", skull.getTags()));
				statement.setDouble(6, skull.getPrice());
				statement.setBoolean(7, skull.isBlocked());
				statement.addBatch();

				if (++count % batchSize == 0) {
					statement.executeBatch(); // Execute batch every batchSize
				}
			}

			statement.executeBatch();
			connection.commit();
			statement.close();

			Skulls.getSkullManager().setDownloading(false);
			Common.log("&r&aFinished inserting all heads into the data file!");
		}));
	}

	public void insertPlacedSkull(@NonNull final PlacedSkull placedSkull, Callback<PlacedSkull> callback) {
		this.runAsync(() -> {
			try {
				getQueryBuilder().insert("placed_skull")
						.set("id", placedSkull.getId().toString())
						.set("skull_id", placedSkull.getSkullId())
						.set("location", SerializeUtil.serializeLocation(placedSkull.getLocation()))
						.execute((ex, affectedRows) -> {
							if (ex != null) {
								ex.printStackTrace();
								resolveCallback(callback, ex);
								return;
							}

							if (callback != null) {
								callback.accept(null, placedSkull);
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		});
	}

	public void getPlacedSkulls(Callback<ArrayList<PlacedSkull>> callback) {
		this.runAsync(() -> {
			getQueryBuilder().select("placed_skull")
					.fetch(rs -> {
						try {
							return extractPlacedSkull(rs);
						} catch (SQLException e) {
							return null;
						}
					}, (ex, results) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						callback.accept(null, new ArrayList<>(results));
					});
		});
	}

	public void deletePlacedSkull(final UUID id, Callback<Boolean> callback) {
		this.runAsync(() -> {
			getQueryBuilder().delete("placed_skull")
					.where("id", id.toString())
					.execute((ex, affectedRows) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						callback.accept(null, affectedRows != null && affectedRows > 0);
					});
		});
	}

	//id, name, category, tags, price, blocked
	public void getSkulls(Callback<ArrayList<Skull>> callback) {
		this.runAsync(() -> {
			getQueryBuilder().select("skull")
					.fetch(rs -> {
						try {
							return extractSkull(rs);
						} catch (SQLException e) {
							return null;
						}
					}, (ex, results) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						this.sync(() -> callback.accept(null, new ArrayList<>(results)));
					});
		});
	}

	public void syncSkullPricesByCategory(Callback<Boolean> callback) {
		this.runAsync(() -> {
			// Use batch updates for each category
			final QueryBuilder qb = getQueryBuilder();
			int[] updateCounts = new int[BaseCategory.values().length];
			final int[] completed = {0};
			final boolean[] hasError = {false};

			for (int i = 0; i < BaseCategory.values().length; i++) {
				final BaseCategory category = BaseCategory.values()[i];
				qb.update("skull")
						.set("price", category.getDefaultPrice())
						.where("category", category.getId())
						.execute((ex, affectedRows) -> {
							if (ex != null && !hasError[0]) {
								hasError[0] = true;
								if (callback != null) {
									resolveCallback(callback, ex);
								}
								return;
							}

							if (affectedRows != null) {
								updateCounts[completed[0]] = affectedRows;
							}
							completed[0]++;

							// All updates completed
							if (completed[0] == BaseCategory.values().length && !hasError[0]) {
								int totalUpdated = 0;
								for (int count : updateCounts) {
									totalUpdated += count;
								}
								final int finalTotalUpdated = totalUpdated;
								Common.log("updated " + finalTotalUpdated);
								if (callback != null) {
									this.sync(() -> callback.accept(null, finalTotalUpdated > 0));
								}
							}
						});
			}
		});
	}

	public void getCategories(Callback<ArrayList<Category>> callback) {
		this.runAsync(() -> {
			getQueryBuilder().select("categories")
					.fetch(rs -> {
						try {
							return extractCategory(rs);
						} catch (SQLException e) {
							return null;
						}
					}, (ex, results) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						callback.accept(null, new ArrayList<>(results));
					});
		});
	}

	public void getPlayers(Callback<ArrayList<SkullUser>> callback) {
		this.runAsync(() -> {
			getQueryBuilder().select("players")
					.fetch(rs -> {
						try {
							return extractSkullPlayer(rs);
						} catch (SQLException e) {
							return null;
						}
					}, (ex, results) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						this.sync(() -> callback.accept(null, new ArrayList<>(results)));
					});
		});
	}

	public void insertPlayer(@NonNull final SkullUser user, Callback<SkullUser> callback) {
		this.runAsync(() -> {
			try {
				// SQLite uses INSERT OR IGNORE instead of ON CONFLICT DO NOTHING
				// We'll use the query builder but need to handle the conflict manually
				getQueryBuilder().insert("players")
						.set("uuid", user.getUUID().toString())
						.set("favourites", user.getFavourites().stream().map(String::valueOf).collect(Collectors.joining(",")))
						.execute((ex, affectedRows) -> {
							if (ex != null) {
								// If it's a constraint violation, that's okay - player already exists
								if (ex.getMessage() != null && ex.getMessage().contains("UNIQUE constraint")) {
									if (callback != null) {
										callback.accept(null, user);
									}
									return;
								}
								ex.printStackTrace();
								resolveCallback(callback, ex);
								return;
							}

							if (callback != null) {
								callback.accept(null, user);
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		});
	}

	public void insertCategory(@NonNull final Category category, Callback<Category> callback) {
		this.runAsync(() -> {
			try {
				getQueryBuilder().insert("categories")
						.set("id", category.getId())
						.set("name", category.getName())
						.set("skulls", category.getSkulls().stream().map(String::valueOf).collect(Collectors.joining(",")))
						.execute((ex, affectedRows) -> {
							if (ex != null) {
								ex.printStackTrace();
								resolveCallback(callback, ex);
								return;
							}

							if (callback != null) {
								callback.accept(null, category);
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		});
	}

	public void updateCategory(@NonNull final Category category, Callback<Boolean> callback) {
		this.runAsync(() -> {
			getQueryBuilder().update("categories")
					.set("name", category.getName())
					.set("skulls", category.getSkulls().stream().map(String::valueOf).collect(Collectors.joining(",")))
					.where("id", category.getId())
					.execute((ex, affectedRows) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						if (callback != null) {
							callback.accept(null, affectedRows != null && affectedRows > 0);
						}
					});
		});
	}

	public void updateSkull(@NonNull final Skull skull, Callback<Boolean> callback) {
		this.runAsync(() -> {
			getQueryBuilder().update("skull")
					.set("name", skull.getName())
					.set("price", skull.getPrice())
					.set("blocked", skull.isBlocked())
					.where("id", skull.getId())
					.execute((ex, affectedRows) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						if (callback != null) {
							callback.accept(null, affectedRows != null && affectedRows > 0);
						}
					});
		});
	}

	public void updatePlayer(@NonNull final SkullUser user, Callback<Boolean> callback) {
		this.runAsync(() -> {
			getQueryBuilder().update("players")
					.set("favourites", user.getFavourites().stream().map(String::valueOf).collect(Collectors.joining(",")))
					.where("uuid", user.getUUID().toString())
					.execute((ex, affectedRows) -> {
						if (ex != null) {
							resolveCallback(callback, ex);
							return;
						}
						if (callback != null) {
							callback.accept(null, affectedRows != null && affectedRows > 0);
						}
					});
		});
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

	public PlacedSkull extractPlacedSkull(@NonNull final ResultSet resultSet) throws SQLException {
		return new PlacedSkullLocation(
				UUID.fromString(resultSet.getString("id")),
				resultSet.getInt("skull_id"),
				SerializeUtil.deserializeLocation(resultSet.getString("location"))
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
