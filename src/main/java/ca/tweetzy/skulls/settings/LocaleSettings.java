package ca.tweetzy.skulls.settings;

import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.skulls.Skulls;

import java.util.HashMap;

/**
 * The current file has been created by Kiran Hart
 * Date Created: June 16 2021
 * Time Created: 11:27 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class LocaleSettings {

    static final HashMap<String, String> languageNodes = new HashMap<>();

    static {
        languageNodes.put("general.prefix", "&8[&eSkulls&8]");
        languageNodes.put("general.blocked_world", "&cYou cannot use skulls within this world");
        languageNodes.put("skull.search_ask", "&aPlease enter a keyword to search");
        languageNodes.put("skull.no_results", "&cCould not find any results for the keyword: &6%keyword%");
        languageNodes.put("skull.category_name_ask", "&aPlease enter the category name");
        languageNodes.put("skull.category_name_taken", "&cThat category id is already in use!");
        languageNodes.put("skull.category_created", "&aCreated a new category named &6%category_id%");
        languageNodes.put("skull.category_deleted", "&aRemoved the category named &6%category_id%");
        languageNodes.put("skull.category_display_name_change", "&aChanged category name to &6%category_name%");
        languageNodes.put("skull.category_icon_change", "&aSelect a head from any main category to change the icon");
        languageNodes.put("skull.add_to_category", "&aSelect a category to add the head to");
        languageNodes.put("skull.download_confirm", "&cPlease confirm the re-download!");
        languageNodes.put("skull.download_finished", "&aFinished downloading heads");
        languageNodes.put("skull.download_in_progress", "&cYou must wait for the download to finish!");
        languageNodes.put("skull.unavailable", "&cHead download/load in progress, please wait!");
        languageNodes.put("skull.no_permission", "&cYou don't have permission to do that!");
        languageNodes.put("skull.enter_new_price", "&aEnter the new price for this skull:");
        languageNodes.put("skull.price_updated", "&aUpdated price that skull to &2$%price%");
        languageNodes.put("skull.no_money", "&cYou do not have enough money to buy that skull!");
        languageNodes.put("skull.money_remove", "&c-$%price%");
    }

    public static void setup() {
        Config config = Skulls.getInstance().getLocale().getConfig();

        languageNodes.keySet().forEach(key -> {
            config.setDefault(key, languageNodes.get(key));
        });

        config.setAutoremove(true).setAutosave(true);
        config.saveChanges();
    }
}
