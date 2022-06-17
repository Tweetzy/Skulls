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

import ca.tweetzy.rose.RoseCore;
import ca.tweetzy.rose.RosePlugin;
import ca.tweetzy.rose.command.CommandManager;
import ca.tweetzy.rose.comp.enums.CompMaterial;
import ca.tweetzy.rose.database.DataMigrationManager;
import ca.tweetzy.rose.database.DatabaseConnector;
import ca.tweetzy.rose.database.SQLiteConnector;
import ca.tweetzy.rose.gui.GuiManager;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.skulls.commands.*;
import ca.tweetzy.skulls.database.DataManager;
import ca.tweetzy.skulls.database.migrations._1_InitialMigration;
import ca.tweetzy.skulls.database.migrations._2_PlacedSkullsMigration;
import ca.tweetzy.skulls.listeners.PlayerJoinQuitListener;
import ca.tweetzy.skulls.listeners.SkullBlockListener;
import ca.tweetzy.skulls.manager.CategoryManager;
import ca.tweetzy.skulls.manager.EconomyManager;
import ca.tweetzy.skulls.manager.PlayerManager;
import ca.tweetzy.skulls.manager.SkullManager;
import ca.tweetzy.skulls.settings.Locale;
import ca.tweetzy.skulls.settings.Settings;

/**
 * Date Created: April 04 2022
 * Time Created: 9:46 a.m.
 *
 * @author Kiran Hart
 */
public final class Skulls extends RosePlugin {

	private final GuiManager guiManager = new GuiManager(this);
	private final CommandManager commandManager = new CommandManager(this);

	private final SkullManager skullManager = new SkullManager();
	private final CategoryManager categoryManager = new CategoryManager();
	private final PlayerManager playerManager = new PlayerManager();
	private EconomyManager economyManager;

	private DatabaseConnector databaseConnector;
	private DataManager dataManager;

	@Override
	protected void onWake() {
		// setup sqlite
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration(),
				new _2_PlacedSkullsMigration()
		);

		// run table migrations
		dataMigrationManager.runMigrations();
	}

	@Override
	protected void onFlight() {
		RoseCore.registerPlugin(this, 5, CompMaterial.ZOMBIE_HEAD.name());

		// settings and locale setup
		Settings.setup();
		Locale.setup();

		Common.setPrefix(Settings.PREFIX.getString());

		this.skullManager.load();
		this.playerManager.load();
		this.categoryManager.load();

		this.economyManager = new EconomyManager();
		this.economyManager.init();
		this.guiManager.init();

		// command
		this.commandManager.registerCommandDynamically(new SkullsCommand()).addSubCommands(new SearchCommand(), new PlayerHeadCommand(), new GiveCommand(), new InspectCommand());

		// events
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);
		getServer().getPluginManager().registerEvents(new SkullBlockListener(), this);
	}

	public static Skulls getInstance() {
		return (Skulls) RosePlugin.getInstance();
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

	@Override
	protected int getBStatsId() {
		return 10616;
	}
}
