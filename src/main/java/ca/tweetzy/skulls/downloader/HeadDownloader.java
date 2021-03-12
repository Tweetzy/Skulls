package ca.tweetzy.skulls.downloader;

import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.skull.SkullCategory;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 11 2021
 * Time Created: 1:43 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class HeadDownloader {

    public static void download(SkullCategory.BaseCategory baseCategory) {
        String url = null;
        switch(baseCategory) {
            case ALPHABET:
                url = MinecraftHeadsLinks.ALPHABET_HEADS;
                break;
            case ANIMALS:
                url = MinecraftHeadsLinks.ANIMAL_HEADS;
                break;
            case BLOCKS:
                url = MinecraftHeadsLinks.BLOCK_HEADS;
                break;
            case DECORATION:
                url = MinecraftHeadsLinks.DECORATION_HEADS;
                break;
            case FOOD_AND_DRINKS:
                url = MinecraftHeadsLinks.FOOD_AND_DRINK_HEADS;
                break;
            case HUMANS:
                url = MinecraftHeadsLinks.HUMAN_HEADS;
                break;
            case HUMANOID:
                url = MinecraftHeadsLinks.HUMANOID_HEADS;
                break;
            case MISCELLANEOUS:
                url = MinecraftHeadsLinks.MISCELLANEOUS_HEADS;
                break;
            case MONSTERS:
                url = MinecraftHeadsLinks.MONSTER_HEADS;
                break;
            case PLANTS:
                url = MinecraftHeadsLinks.PLANT_HEADS;
                break;
        }

        try {
            File dir = new File(Skulls.getInstance().getDataFolder() + "/heads");
            if (!dir.exists()) {
                dir.mkdir();
            }

            InputStream inputStream = new URL(url).openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder builder = new StringBuilder();

            int character;
            while ((character = bufferedReader.read()) != -1) {
                builder.append((char) character);
            }

            JsonParser parser = new JsonParser();
            JsonArray json = (JsonArray) parser.parse(builder.toString());

            try (FileWriter fileWriter = new FileWriter(Skulls.getInstance().getDataFolder() + "/heads/" + baseCategory.getName() + " Heads.json")) {
                fileWriter.write(json.toString());
                Skulls.getInstance().getLocale().newMessage(TextUtils.formatText("&aDownloaded heads for &6" + baseCategory.getName())).sendPrefixedMessage(Bukkit.getConsoleSender());
                Skulls.getInstance().setHeadDLTracker(Skulls.getInstance().getHeadDLTracker() + 1);
                if (Skulls.getInstance().getHeadDLTracker() == SkullCategory.BaseCategory.values().length) {
                    Skulls.getInstance().setHeadsDownloading(false);
                    Skulls.getInstance().setHeadDLTracker(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            Skulls.getInstance().getLocale().newMessage(TextUtils.formatText("&4An error has occurred while trying to download heads for category: &6" + baseCategory.getName())).sendPrefixedMessage(Bukkit.getConsoleSender());
        }
    }
}
