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
import ca.tweetzy.skulls.commands.SearchCommand;
import ca.tweetzy.skulls.commands.SkullsCommand;
import ca.tweetzy.skulls.database.DataManager;
import ca.tweetzy.skulls.database.migrations._1_InitialMigration;
import ca.tweetzy.skulls.manager.EconomyManager;
import ca.tweetzy.skulls.manager.SkullManager;
import ca.tweetzy.skulls.settings.Locale;
import ca.tweetzy.skulls.settings.Settings;
import lombok.Getter;

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
	private EconomyManager economyManager;

	private DatabaseConnector databaseConnector;
	private DataManager dataManager;


	@Override
	protected void onWake() {
		// setup sqlite
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration()
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
		this.economyManager = new EconomyManager();
		this.economyManager.init();

		this.guiManager.init();

		// command
		this.commandManager.registerCommandDynamically(new SkullsCommand()).addSubCommands(new SearchCommand());
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

	// economy manager
	public static EconomyManager getEconomyManager() {
		return getInstance().economyManager;
	}


	@Override
	protected int getBStatsId() {
		return 10616;
	}
}
