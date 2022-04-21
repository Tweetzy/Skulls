package ca.tweetzy.skulls;

import ca.tweetzy.rose.RosePlugin;
import ca.tweetzy.rose.database.DataMigrationManager;
import ca.tweetzy.rose.database.DatabaseConnector;
import ca.tweetzy.rose.database.SQLiteConnector;
import ca.tweetzy.skulls.database.DataManager;
import ca.tweetzy.skulls.database.migrations._1_InitialMigration;
import ca.tweetzy.skulls.manager.SkullManager;
import ca.tweetzy.skulls.settings.Settings;
import lombok.Getter;

/**
 * Date Created: April 04 2022
 * Time Created: 9:46 a.m.
 *
 * @author Kiran Hart
 */
public final class Skulls extends RosePlugin {

	private DatabaseConnector databaseConnector;
	private DataManager dataManager;


	@Getter
	private SkullManager skullManager;

	@Override
	protected void onWake() {
		if (!this.getDataFolder().exists())
			this.getDataFolder().mkdir();
	}

	@Override
	protected void onFlight() {
		Settings.setup();

		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration()
		);

		dataMigrationManager.runMigrations();

		this.skullManager = new SkullManager();
		this.skullManager.load();
	}

	public static Skulls getInstance() {
		return (Skulls) RosePlugin.getInstance();
	}

	public static DataManager getDataManager() {
		return getInstance().dataManager;
	}

}
