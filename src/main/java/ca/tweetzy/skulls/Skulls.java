package ca.tweetzy.skulls;

import ca.tweetzy.skulls.api.DataFile;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.api.enums.SkullsDefaultCategory;
import ca.tweetzy.skulls.commands.SkullsCommandGroup;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.listeners.PlayerJoinLeaveListener;
import ca.tweetzy.skulls.model.SkullCategoryManager;
import ca.tweetzy.skulls.model.SkullManager;
import ca.tweetzy.skulls.model.SkullPlayerManager;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.Messenger;
import ca.tweetzy.tweety.MinecraftVersion;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.command.SimpleCommandGroup;
import ca.tweetzy.tweety.model.SpigotUpdater;
import ca.tweetzy.tweety.plugin.SimplePlugin;
import ca.tweetzy.tweety.remain.Remain;
import lombok.Getter;

/**
 * The current file has been created by Kiran Hart
 * Date Created: August 22 2021
 * Time Created: 11:02 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Skulls extends SimplePlugin {

	@Getter
	private final SimpleCommandGroup mainCommand = SkullsCommandGroup.getInstance();

	@Getter
	private final DataFile dataFile = new DataFile("data", this);

	private final SkullManager skullManager = new SkullManager();
	private final SkullCategoryManager skullCategoryManager = new SkullCategoryManager();
	private final SkullPlayerManager skullPlayerManager = new SkullPlayerManager();

	@Override
	protected void onPluginStart() {
		Common.ADD_TELL_PREFIX = true;
		Common.ADD_LOG_PREFIX = true;
		Common.setLogPrefix(Settings.PREFIX + " ");
		Common.setTellPrefix(Settings.PREFIX);
		Messenger.setInfoPrefix(Settings.PREFIX + " ");
		Messenger.setAnnouncePrefix(Settings.PREFIX + " ");
		Messenger.setErrorPrefix(Settings.PREFIX + " ");
		Messenger.setQuestionPrefix(Settings.PREFIX + " ");
		Messenger.setSuccessPrefix(Settings.PREFIX + " ");
		Messenger.setWarnPrefix(Settings.PREFIX + " ");

		Common.runAsync(() -> {
			for (SkullsDefaultCategory value : SkullsDefaultCategory.values()) {
				SkullsAPI.addCategory(new SkullCategory(value.getId(), value.getName()));
			}

			skullManager.downloadHeads(false);
		});

		registerEvents(new PlayerJoinLeaveListener());
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
		return (Skulls) SimplePlugin.getInstance();
	}

	public static SkullManager getSkullManager() {
		return ((Skulls) SimplePlugin.getInstance()).skullManager;
	}

	public static SkullCategoryManager getSkullCategoryManager() {
		return ((Skulls) SimplePlugin.getInstance()).skullCategoryManager;
	}

	public static SkullPlayerManager getSkullPlayerManager() {
		return ((Skulls) SimplePlugin.getInstance()).skullPlayerManager;
	}
}
