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

package ca.tweetzy.skulls;

import ca.tweetzy.flight.FlightPlugin;
import ca.tweetzy.flight.command.CommandManager;
import ca.tweetzy.flight.database.DataMigrationManager;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.SQLiteConnector;
import ca.tweetzy.flight.gui.GuiManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.commands.*;
import ca.tweetzy.skulls.database.DataManager;
import ca.tweetzy.skulls.database.migrations._1_InitialMigration;
import ca.tweetzy.skulls.database.migrations._2_PlacedSkullsMigration;
import ca.tweetzy.skulls.database.migrations._3_HistoryRemovalMigration;
import ca.tweetzy.skulls.impl.SkullsAPIImplementation;
import ca.tweetzy.skulls.listeners.PlayerDeathListener;
import ca.tweetzy.skulls.listeners.PlayerJoinQuitListener;
import ca.tweetzy.skulls.listeners.SkullBlockListener;
import ca.tweetzy.skulls.manager.CategoryManager;
import ca.tweetzy.skulls.manager.EconomyManager;
import ca.tweetzy.skulls.manager.PlayerManager;
import ca.tweetzy.skulls.manager.SkullManager;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.NamespacedKey;

import javax.inject.Named;

/**
 * Date Created: April 04 2022
 * Time Created: 9:46 a.m.
 *
 * @author Kiran Hart
 */
public final class Skulls extends FlightPlugin {

	private static TaskChainFactory taskChainFactory;

	private final GuiManager guiManager = new GuiManager(this);
	private final CommandManager commandManager = new CommandManager(this);

	private final SkullManager skullManager = new SkullManager();
	private final CategoryManager categoryManager = new CategoryManager();
	private final PlayerManager playerManager = new PlayerManager();
	private EconomyManager economyManager;

	private final SkullsAPI api = new SkullsAPIImplementation();

	private DatabaseConnector databaseConnector;
	private DataManager dataManager;


	private final NamespacedKey SKULLS_CLAIM_DELAY = new NamespacedKey(this, "SkullsClaimDelay");


	@Override
	protected void onFlight() {
		// settings and locale setup
		Settings.setup();
		Translations.init();

		Common.setPrefix(Settings.PREFIX.getString());
		Common.setPluginName("<GRADIENT:DD5E89>&lSkulls</GRADIENT:fbc7d4>");
		taskChainFactory = BukkitTaskChainFactory.create(this);

		// setup sqlite
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration(),
				new _2_PlacedSkullsMigration(),
				new _3_HistoryRemovalMigration()
		);

		// run table migrations
		dataMigrationManager.runMigrations();

		this.skullManager.load();
		this.playerManager.load();
		this.categoryManager.load();

		this.economyManager = new EconomyManager();
		this.economyManager.init();
		this.guiManager.init();

		// command
		this.commandManager.registerCommandDynamically(new SkullsCommand()).addSubCommands(
				new SearchCommand(),
				new PlayerHeadCommand(),
				new GiveCommand(),
				new InspectCommand(),
				new ReloadCommand()
		);

		// events
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
		if (Settings.SKULL_TRACKING.getBoolean()) {
			getServer().getPluginManager().registerEvents(new SkullBlockListener(), this);
		}
	}

	public static Skulls getInstance() {
		return (Skulls) FlightPlugin.getInstance();
	}

	// gui manager
	public static GuiManager getGuiManager() {
		return getInstance().guiManager;
	}

	// data manager
	public static DataManager getDataManager() {
		return getInstance().dataManager;
	}

	// skull manager
	public static SkullManager getSkullManager() {
		return getInstance().skullManager;
	}

	// category manager
	public static CategoryManager getCategoryManager() {
		return getInstance().categoryManager;
	}

	// player manager
	public static PlayerManager getPlayerManager() {
		return getInstance().playerManager;
	}

	// economy manager
	public static EconomyManager getEconomyManager() {
		return getInstance().economyManager;
	}

	public static <T> TaskChain<T> newChain() {
		return taskChainFactory.newChain();
	}

	// api
	public static SkullsAPI getAPI() {
		return getInstance().api;
	}

	public static NamespacedKey getClaimDelayKey() {
		return getInstance().SKULLS_CLAIM_DELAY;
	}

	@Override
	protected int getBStatsId() {
		return 10616;
	}
}
