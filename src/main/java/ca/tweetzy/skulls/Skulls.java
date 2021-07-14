package ca.tweetzy.skulls;

import ca.tweetzy.core.TweetyCore;
import ca.tweetzy.core.TweetyPlugin;
import ca.tweetzy.core.commands.CommandManager;
import ca.tweetzy.core.compatibility.ServerVersion;
import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.core.gui.GuiManager;
import ca.tweetzy.core.utils.Metrics;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.api.UpdateChecker;
import ca.tweetzy.skulls.commands.*;
import ca.tweetzy.skulls.downloader.HeadDownloader;
import ca.tweetzy.skulls.economy.EconomyManager;
import ca.tweetzy.skulls.settings.LocaleSettings;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.Skull;
import ca.tweetzy.skulls.skull.SkullCategory;
import ca.tweetzy.skulls.skull.SkullManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 10 2021
 * Time Created: 4:58 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Skulls extends TweetyPlugin {

    private static Skulls instance;

    @Getter
    private final Config data = new Config(this, "data.yml");

    @Getter
    private final GuiManager guiManager = new GuiManager(this);

    @Getter
    private final HashMap<UUID, SkullCategory> changingCustomCategoryIcon = new HashMap<>();

    @Getter
    @Setter
    private HashMap<UUID, Skull> addingToCategory = new HashMap<>();

    @Getter
    @Setter
    private boolean headsDownloading = false;

    @Getter
    @Setter
    private int headDLTracker = 0;

    @Getter
    private CommandManager commandManager;

    @Getter
    private SkullManager skullManager;

    @Getter
    private EconomyManager economyManager;

    @Getter
    private Economy economy;

    @SuppressWarnings("unused")
    Metrics metrics;

    @Override
    public void onPluginLoad() {
        instance = this;
    }

    @Override
    public void onPluginEnable() {
        TweetyCore.registerPlugin(this, 7, XMaterial.PLAYER_HEAD.name());

        // Shutdown plugin if server version is not 1.8+
        if (ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_7)) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Vault check
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) this.economy = rsp.getProvider();
        }

        // Settings setup
        Settings.setup();

        // Locale
        setLocale(Settings.LANG.getString());
        LocaleSettings.setup();

        // Economy Manager
        this.economyManager = new EconomyManager();

        // Data File
        this.data.load();

        // Commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addCommand(new CommandSkulls()).addSubCommands(
                new CommandSearch(),
                new CommandSettings(),
                new CommandDownload(),
                new CommandReload(),
                new CommandGiveRandom()
        );

        // Managers
        this.guiManager.init();
        this.skullManager = new SkullManager();

        if ((!this.data.contains("downloaded") || (this.data.isBoolean("downloaded") && !this.data.getBoolean("downloaded"))) || Settings.OPTION_DOWNLOAD_HEADS_ON_LOAD.getBoolean()) {
            downloadHeads();
            this.data.set("downloaded", true);
            this.data.save();
        } else {
            loadHeads();
        }

        // Perform the update check
        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> new UpdateChecker(this, 90098, getConsole()).check(), 1L);

        // Metrics
        this.metrics = new Metrics(this, 10616);
    }

    @Override
    public void onPluginDisable() {
        instance = null;
    }

    @Override
    public void onConfigReload() {
        Settings.setup();
        setLocale(Settings.LANG.getString());
        this.economyManager = new EconomyManager();
    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
    }

    public static Skulls getInstance() {
        return instance;
    }

    public void loadHeads() {
        JsonParser parser = new JsonParser();
        try {
            this.skullManager.clearTemporaryStorage();
            Arrays.stream(SkullCategory.BaseCategory.values()).forEach(base -> this.skullManager.addSkullCategory(new SkullCategory(base)));
            // load custom created categories
            if (SkullAPI.getInstance().anyCustomCategories()) {
                this.data.getConfigurationSection("custom category").getKeys(false).forEach(category -> this.skullManager.addSkullCategory(new SkullCategory(category)));
            }

            for (SkullCategory.BaseCategory value : SkullCategory.BaseCategory.values()) {
                JsonArray jsonArray = (JsonArray) parser.parse(new FileReader(getDataFolder() + "/heads/" + value.getName() + " Heads.json"));
                jsonArray.getAsJsonArray().forEach(element -> {
                    JsonObject jsonObject = (JsonObject) element;
                    Skull skull = new Skull(
                            replace(jsonObject.get("name").toString()),
                            UUID.fromString(replace(jsonObject.get("uuid").toString())),
                            replace(jsonObject.get("value").toString()),
                            replace(parser.parse(new String(Base64.getDecoder().decode(replace(jsonObject.get("value").toString())))).getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").toString()),
                            this.skullManager.getCategory(value, false),
                            replace(jsonObject.get("tags").toString()).split(","),
                            this.skullManager.isSkullConfigFavourite(UUID.fromString(replace(jsonObject.get("uuid").toString())))
                    );

                    this.skullManager.addSkull(skull);
                });
                getLocale().newMessage(TextUtils.formatText("&aFinished loading heads for the category: &6" + value.getName() + " &a(" + jsonArray.size() + "&a)")).sendPrefixedMessage(Bukkit.getConsoleSender());
            }
        } catch (FileNotFoundException e) {
            getLocale().newMessage(TextUtils.formatText("&4An error has occurred while loading the heads.")).sendPrefixedMessage(Bukkit.getConsoleSender());
            e.printStackTrace();
        }
    }

    protected int taskId;

    public void downloadHeads(CommandSender... downloader) {
        getLocale().newMessage(TextUtils.formatText("&4[!] --- &eHeads could not be found --- &4[!]")).sendPrefixedMessage(Bukkit.getConsoleSender());
        getLocale().newMessage(TextUtils.formatText("&4[!] --- &eAttempting to download them (this may take some time) --- &4[!]")).sendPrefixedMessage(Bukkit.getConsoleSender());
        setHeadsDownloading(true);
        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> Arrays.asList(SkullCategory.BaseCategory.values()).forEach(HeadDownloader::download), 1L);

        taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (!isHeadsDownloading()) {
                loadHeads();
                if (downloader != null) {
                    for (CommandSender sender : downloader) {
                        getLocale().getMessage("skull.download_finished").sendPrefixedMessage(sender);
                    }
                }
                cancelDownloadTask();
            }
        }, 20L, 20L);
    }

    private void cancelDownloadTask() {
        getServer().getScheduler().cancelTask(taskId);
        getLocale().newMessage(TextUtils.formatText("&fDownload task was killed")).sendPrefixedMessage(Bukkit.getConsoleSender());
    }

    private static String replace(String in) {
        return in.replace("\"", "");
    }
}
