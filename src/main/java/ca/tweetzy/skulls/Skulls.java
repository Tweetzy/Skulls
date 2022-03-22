package ca.tweetzy.skulls;

import ca.tweetzy.skulls.commands.SkullsCommandHandler;
import ca.tweetzy.skulls.database.DataManager;
import ca.tweetzy.skulls.database.migrations._1_InitialMigration;
import ca.tweetzy.skulls.manager.SkullManager;
import ca.tweetzy.skulls.settings.Locale;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.TweetyPlugin;
import ca.tweetzy.tweety.configuration.Config;
import ca.tweetzy.tweety.database.DataMigrationManager;
import ca.tweetzy.tweety.database.DatabaseConnector;
import ca.tweetzy.tweety.database.SQLiteConnector;
import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.util.MinecraftVersion;
import lombok.Getter;

/**
 * Date Created: April 04 2022
 * Time Created: 9:46 a.m.
 *
 * @author Kiran Hart
 */
public final class Skulls extends TweetyPlugin {

	@Getter
	private final Config coreConfig = new Config(this);

	@Getter
	private Config lang;

	@Getter
	private DataManager dataManager;

	private DatabaseConnector databaseConnector;

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

		Common.ADD_LOG_PREFIX = true;
		Common.ADD_TELL_PREFIX = true;

		Common.setLogPrefix(Settings.PREFIX.getString());
		Common.setTellPrefix(Settings.PREFIX.getString());

		this.lang = new Config(this, "/locale/", Settings.LANG.getString() + ".yml");
		Locale.setup();

		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration()
		);

		dataMigrationManager.runMigrations();

		this.skullManager = new SkullManager();
		this.skullManager.load();

		SkullsCommandHandler.getInstance().register();
	}

	public static Skulls getInstance() {
		return (Skulls) TweetyPlugin.getInstance();
	}

	@Override
	public int getMetricsPluginId() {
		return 10616;
	}

	@Override
	public MinecraftVersion.V getMinimumVersion() {
		return MinecraftVersion.V.v1_8;
	}

	@Override
	public MinecraftVersion.V getMaximumVersion() {
		return MinecraftVersion.V.v1_18;
	}
}
