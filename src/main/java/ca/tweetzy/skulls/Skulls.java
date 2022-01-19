package ca.tweetzy.skulls;

import ca.tweetzy.skulls.api.DataFile;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.api.enums.SkullsDefaultCategory;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.listeners.PlayerDeathListener;
import ca.tweetzy.skulls.listeners.PlayerJoinLeaveListener;
import ca.tweetzy.skulls.model.SkullCategoryManager;
import ca.tweetzy.skulls.model.SkullManager;
import ca.tweetzy.skulls.model.SkullPlayerManager;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.Messenger;
import ca.tweetzy.tweety.MinecraftVersion;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.model.SpigotUpdater;
import ca.tweetzy.tweety.plugin.TweetyPlugin;
import ca.tweetzy.tweety.remain.Remain;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * The current file has been created by Kiran Hart
 * Date Created: August 22 2021
 * Time Created: 11:02 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Skulls extends TweetyPlugin {

	@Getter
	private final DataFile dataFile = new DataFile("data", this);

	private final SkullManager skullManager = new SkullManager();
	private final SkullCategoryManager skullCategoryManager = new SkullCategoryManager();
	private final SkullPlayerManager skullPlayerManager = new SkullPlayerManager();

	@Getter
	private boolean bStats = false;

	@Override
	protected void onPluginStart() {
		normalizePrefix();

		if (Settings.AUTO_STATS) {
			final File file = new File("plugins" + File.separator + "bStats" + File.separator + "config.yml");
			if (!file.exists()) bStats = true;
			else {
				final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
				configuration.set("enabled", true);
				try {
					configuration.save(file);
					bStats = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (!bStats) {
			Common.logFramed("&cPlease enable bStats within your plugins folder", "&cit helps me collect data on Skulls.");
		}

		Common.runAsync(() -> {
			for (SkullsDefaultCategory value : SkullsDefaultCategory.values()) {
				SkullsAPI.addCategory(new SkullCategory(value.getId(), value.getName(), false, null));
			}

			this.skullCategoryManager.loadCustomCategories();
			skullManager.downloadHeads(false);
		});

		registerEvents(new PlayerJoinLeaveListener());
		registerEvents(new PlayerDeathListener());
	}

	@Override
	protected void onReloadablesStart() {
		this.skullPlayerManager.getPlayers().clear();
		Remain.getOnlinePlayers().forEach(player -> this.skullPlayerManager.addPlayer(new SkullPlayer(
				player.getUniqueId(),
				new StrictList<>()
		)));
	}

	@Override
	protected void onPluginStop() {
		this.skullPlayerManager.savePlayers();
		this.skullPlayerManager.getPlayers().clear();

		this.skullCategoryManager.saveCategories();
		this.skullCategoryManager.getCategories().clear();

		Bukkit.getServer().getScheduler().cancelTasks(this);
	}


	private void normalizePrefix() {
		Common.ADD_TELL_PREFIX = Settings.PREFIX.length() != 0;
		Common.ADD_LOG_PREFIX = true;

		Common.setLogPrefix(Settings.PREFIX + " ");
		Common.setTellPrefix(Settings.PREFIX);

		final String prefix = Settings.PREFIX + (Settings.PREFIX.length() != 0 ? " " : "");

		Messenger.setInfoPrefix(prefix);
		Messenger.setAnnouncePrefix(prefix);
		Messenger.setErrorPrefix(prefix);
		Messenger.setQuestionPrefix(prefix);
		Messenger.setSuccessPrefix(prefix);
		Messenger.setWarnPrefix(prefix);
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
	public int getFoundedYear() {
		return 2021;
	}

	@Override
	public SpigotUpdater getUpdateCheck() {
		return new SpigotUpdater(90098);
	}

	public static Skulls getInstance() {
		return (Skulls) TweetyPlugin.getInstance();
	}

	public static SkullManager getSkullManager() {
		return ((Skulls) TweetyPlugin.getInstance()).skullManager;
	}

	public static SkullCategoryManager getSkullCategoryManager() {
		return ((Skulls) TweetyPlugin.getInstance()).skullCategoryManager;
	}

	public static SkullPlayerManager getSkullPlayerManager() {
		return ((Skulls) TweetyPlugin.getInstance()).skullPlayerManager;
	}
}
