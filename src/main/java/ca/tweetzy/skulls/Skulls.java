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
import ca.tweetzy.skulls.commands.CommandSkulls;
import ca.tweetzy.skulls.downloader.HeadDownloader;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.Skull;
import ca.tweetzy.skulls.skull.SkullCategory;
import ca.tweetzy.skulls.skull.SkullManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 10 2021
 * Time Created: 4:58 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Skulls extends TweetyPlugin {

    private static Skulls instance;
    private final Config data = new Config(this, "data.yml");
    private final GuiManager guiManager = new GuiManager(this);

    private boolean headsDownloading = false;
    private int headDLTracker = 0;

    private CommandManager commandManager;
    private SkullManager skullManager;
    Metrics metrics;


    @Override
    public void onPluginLoad() {
        instance = this;
    }

    @Override
    public void onPluginEnable() {
        TweetyCore.registerPlugin(this, 7, XMaterial.PLAYER_HEAD.name());
        TweetyCore.initEvents(this);

        // Shutdown plugin if server version is not 1.8+
        if (ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_7)) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Settings setup
        Settings.setup();

        // Locale
        setLocale(Settings.LANG.getString(), false);

        // Data File
        this.data.load();

        // Commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addCommand(new CommandSkulls());

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

        // Metrics
        if (Settings.METRICS.getBoolean()) {
            this.metrics = new Metrics(this, 10616);
        }
    }

    @Override
    public void onPluginDisable() {
        instance = null;
    }

    @Override
    public void onConfigReload() {

    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
    }

    public static Skulls getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Config getData() {
        return data;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public SkullManager getSkullManager() {
        return skullManager;
    }

    public int getHeadDLTracker() {
        return headDLTracker;
    }

    public boolean isHeadsDownloading() {
        return headsDownloading;
    }

    public void setHeadsDownloading(boolean headsDownloading) {
        this.headsDownloading = headsDownloading;
    }

    public void setHeadDLTracker(int headDLTracker) {
        this.headDLTracker = headDLTracker;
    }

    public void loadHeads() {
        JsonParser parser = new JsonParser();
        try {
            this.skullManager.clearTemporaryStorage();
            Arrays.stream(SkullCategory.BaseCategory.values()).forEach(base -> this.skullManager.addSkullCategory(new SkullCategory(base)));

            for (SkullCategory.BaseCategory value : SkullCategory.BaseCategory.values()) {
                JsonArray jsonArray = (JsonArray) parser.parse(new FileReader(getDataFolder() + "/heads/" + value.getName() + " Heads.json"));
                Bukkit.getConsoleSender().sendMessage(value.getName() + " size: " + jsonArray.size());
                getLocale().newMessage(TextUtils.formatText("&aBegan loading heads for the category: &6" + value.getName())).sendPrefixedMessage(Bukkit.getConsoleSender());
                jsonArray.getAsJsonArray().forEach(element -> {
                    JsonObject jsonObject = (JsonObject) element;
                    Skull skull = new Skull(
                            replace(jsonObject.get("name").toString()),
                            UUID.fromString(replace(jsonObject.get("uuid").toString())),
                            replace(jsonObject.get("value").toString()),
                            replace(parser.parse(new String(Base64.getDecoder().decode(replace(jsonObject.get("value").toString())))).getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").toString()),
                            this.skullManager.getCategory(value, false),
                            replace(jsonObject.get("tags").toString()).split(","),
                            false
                    );

                    this.skullManager.addSkull(skull);
                });
                getLocale().newMessage(TextUtils.formatText("&aFinished loading heads for the category: &6" + value.getName())).sendPrefixedMessage(Bukkit.getConsoleSender());
            }
        } catch (FileNotFoundException e) {
            getLocale().newMessage(TextUtils.formatText("&4An error has occurred while loading the heads.")).sendPrefixedMessage(Bukkit.getConsoleSender());
            e.printStackTrace();
        }
    }

    public void downloadHeads() {
        getLocale().newMessage(TextUtils.formatText("&4[!] --- &eHeads could not be found --- &4[!]")).sendPrefixedMessage(Bukkit.getConsoleSender());
        getLocale().newMessage(TextUtils.formatText("&4[!] --- &eAttempting to download them (this may take some time) --- &4[!]")).sendPrefixedMessage(Bukkit.getConsoleSender());
        setHeadsDownloading(true);
        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            Arrays.asList(SkullCategory.BaseCategory.values()).forEach(HeadDownloader::download);
        }, 1L);

        getServer().getScheduler().runTaskTimer(this, (task) -> {
            if (!isHeadsDownloading()) {
                loadHeads();
                task.cancel();
            }
        }, 20L, 20L);
    }

    private static String replace(String in) {
        return in.replace("\"", "");
    }
}
