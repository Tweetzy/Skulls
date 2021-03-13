package ca.tweetzy.skulls.settings;

import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.core.configuration.ConfigSetting;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.downloader.MinecraftHeadsLinks;

import java.util.Arrays;
import java.util.Collections;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 10 2021
 * Time Created: 6:08 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Settings {

    static final Config config = Skulls.getInstance().getCoreConfig();

    public static final ConfigSetting LANG = new ConfigSetting(config, "lang", "en_US", "Default language file");
    public static final ConfigSetting METRICS = new ConfigSetting(config, "metrics", true, "Should the plugin use metrics?", "It simply allows me to see how many servers", "are currently using the skulls plugin.");

    public static final ConfigSetting OPTION_DOWNLOAD_HEADS_ON_LOAD = new ConfigSetting(config, "option.download heads on server load", false, "Should Skulls attempt to redownload all the heads", "everytime the server starts/is reload?");
    public static final ConfigSetting INCLUDE_TAGS_IN_SEARCH = new ConfigSetting(config, "option.include tags in search", true, "When searching for a head, should tags be searched as well?");

    public static final ConfigSetting GUI_BACK_BTN_ITEM = new ConfigSetting(config, "gui.global items.back button.item", "ARROW", "Settings for the back button");
    public static final ConfigSetting GUI_BACK_BTN_NAME = new ConfigSetting(config, "gui.global items.back button.name", "&e<< Back");
    public static final ConfigSetting GUI_BACK_BTN_LORE = new ConfigSetting(config, "gui.global items.back button.lore", Arrays.asList("&7Click the button to go", "&7back to the previous page."));

    public static final ConfigSetting GUI_CLOSE_BTN_ITEM = new ConfigSetting(config, "gui.global items.close button.item", "BARRIER", "Settings for the close button");
    public static final ConfigSetting GUI_CLOSE_BTN_NAME = new ConfigSetting(config, "gui.global items.close button.name", "&cClose");
    public static final ConfigSetting GUI_CLOSE_BTN_LORE = new ConfigSetting(config, "gui.global items.close button.lore", Collections.singletonList("&7Click to close this menu."));

    public static final ConfigSetting GUI_NEXT_BTN_ITEM = new ConfigSetting(config, "gui.global items.next button.item", "ARROW", "Settings for the next button");
    public static final ConfigSetting GUI_NEXT_BTN_NAME = new ConfigSetting(config, "gui.global items.next button.name", "&eNext >>");
    public static final ConfigSetting GUI_NEXT_BTN_LORE = new ConfigSetting(config, "gui.global items.next button.lore", Arrays.asList("&7Click the button to go", "&7to the next page."));

    public static final ConfigSetting GUI_MAIN_GUI_TITLE = new ConfigSetting(config, "gui.main menu.title", "&eSkulls Main Menu");
    public static final ConfigSetting GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL = new ConfigSetting(config, "gui.main menu.use custom skulls for categories", true, "Should the inventory use a custom textured", "head for the button/icon");
    public static final ConfigSetting GUI_MAIN_GUI_FILL_BACKGROUND = new ConfigSetting(config, "gui.main menu.fill background", true, "Should the empty slots be filled with an item?");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BG_ITEM = new ConfigSetting(config, "gui.main menu.background item", "BLACK_STAINED_GLASS_PANE");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ALPHABET_ITEM = new ConfigSetting(config, "gui.main menu.items.alphabet.item", "BEACON");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ALPHABET_TEXTURE = new ConfigSetting(config, "gui.main menu.items.alphabet.texture", "11bb4266a22dcbc4607621b9c768932950160c2b96708267d707d44551378cd7");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ALPHABET_NAME = new ConfigSetting(config, "gui.main menu.items.alphabet.name", "&e&lAlphabet");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ALPHABET_LORE = new ConfigSetting(config, "gui.main menu.items.alphabet.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_ITEM = new ConfigSetting(config, "gui.main menu.items.animal.item", "CREEPER_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_TEXTURE = new ConfigSetting(config, "gui.main menu.items.animal.texture", "7f88bda8aa0f94344929710bd7e7d668ade320bbdfb3526d0c19a6ef519702c9");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_NAME = new ConfigSetting(config, "gui.main menu.items.animal.name", "&e&lAnimals");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_LORE = new ConfigSetting(config, "gui.main menu.items.animal.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_ITEM = new ConfigSetting(config, "gui.main menu.items.block.item", "GRASS_BLOCK");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_TEXTURE = new ConfigSetting(config, "gui.main menu.items.block.texture", "5c100dd2e1e2dc293865f8747c904c737baab236e6e54ccc2d3bed6d1fa42d3a");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_NAME = new ConfigSetting(config, "gui.main menu.items.block.name", "&e&lBlocks");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_LORE = new ConfigSetting(config, "gui.main menu.items.block.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_ITEM = new ConfigSetting(config, "gui.main menu.items.decoration.item", "POPPY");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_TEXTURE = new ConfigSetting(config, "gui.main menu.items.decoration.texture", "5b1ef2a4829a11fd903b5e31088662a8c56e471bb48643c0d9f95006d1820210");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_NAME = new ConfigSetting(config, "gui.main menu.items.decoration.name", "&e&LDecorations");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_LORE = new ConfigSetting(config, "gui.main menu.items.decoration.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_ITEM = new ConfigSetting(config, "gui.main menu.items.food.item", "COOKED_BEEF");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.food.texture", "8ad425514ccf8cb9c0e143425d73d9394a50dda41977b2120616c0f9e38920e5");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_NAME = new ConfigSetting(config, "gui.main menu.items.food.name", "&e&lFood & Drinks");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_LORE = new ConfigSetting(config, "gui.main menu.items.food.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_ITEM = new ConfigSetting(config, "gui.main menu.items.human.item", "PLAYER_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.human.texture", "90731a8b61feddf0275338a3b9d38ef0054e72ffc1299be50e6c98c2c0905ffc");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_NAME = new ConfigSetting(config, "gui.main menu.items.human.name", "&e&lHumans");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_LORE = new ConfigSetting(config, "gui.main menu.items.human.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_ITEM = new ConfigSetting(config, "gui.main menu.items.humanoid.item", "ZOMBIE_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_TEXTURE = new ConfigSetting(config, "gui.main menu.items.humanoid.texture", "255a56fbfbf81630579990796a31ddd4c8805bdcceb01837ee00c24585934376");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_NAME = new ConfigSetting(config, "gui.main menu.items.humanoid.name", "&e&lHumanoids");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_LORE = new ConfigSetting(config, "gui.main menu.items.humanoid.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_ITEM = new ConfigSetting(config, "gui.main menu.items.miscellaneous.item", "NETHER_STAR");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.miscellaneous.texture", "884834614c8a1b48f9d479eab8bca3ac743ef1617e566ad1762298ea5f0d1a7f");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_NAME = new ConfigSetting(config, "gui.main menu.items.miscellaneous.name", "&e&lMiscellaneous");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_LORE = new ConfigSetting(config, "gui.main menu.items.miscellaneous.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_ITEM = new ConfigSetting(config, "gui.main menu.items.monster.item", "DRAGON_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.monster.texture", "a137229538d619da70b5fd2ea06a560d9ce50b0e2f92413e6aa73d99f9d7a878");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_NAME = new ConfigSetting(config, "gui.main menu.items.monster.name", "&e&lMonsters");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_LORE = new ConfigSetting(config, "gui.main menu.items.monster.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_ITEM = new ConfigSetting(config, "gui.main menu.items.plants.item", "SUNFLOWER");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.plants.texture", "bd85f8144540eb4688b82ac4be6e6416314ba176da833ea878d70f935d028b76");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_NAME = new ConfigSetting(config, "gui.main menu.items.plants.name", "&e&lPlants");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_LORE = new ConfigSetting(config, "gui.main menu.items.plants.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FAVOURITES_NAME = new ConfigSetting(config, "gui.main menu.items.favourites.name", "&e&lFavourite Heads");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FAVOURITES_LORE = new ConfigSetting(config, "gui.main menu.items.favourites.lore", Collections.singletonList(
            "&7Click to view favorite heads"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLAYERS_NAME = new ConfigSetting(config, "gui.main menu.items.player heads.name", "&e&lPlayer Heads");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLAYERS_LORE = new ConfigSetting(config, "gui.main menu.items.player heads.lore", Collections.singletonList(
            "&7Click to view player heads"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_SEARCH_NAME = new ConfigSetting(config, "gui.main menu.items.search.name", "&e&lSearch Heads");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_SEARCH_LORE = new ConfigSetting(config, "gui.main menu.items.search.lore", Collections.singletonList(
            "&7Click to search by a keyword"
    ));


    public static final ConfigSetting GUI_CATEGORY_GUI_TITLE = new ConfigSetting(config, "gui.category menu.title", "&e&l%category% &8(%current_page%/%max_pages%)");
    public static final ConfigSetting GUI_CATEGORY_HEAD_TITLE = new ConfigSetting(config, "gui.category menu.head title", "&e%head_name%");
    public static final ConfigSetting GUI_CATEGORY_HEAD_LORE = new ConfigSetting(config, "gui.category menu.head lore", Arrays.asList(
            "&7Tags: &e%head_tags%",
            "",
            "&7Middle-Click to favourite",
            "&7Click to take 1"
    ));

    public static final ConfigSetting GUI_CATEGORY_HEAD_LORE_FAV = new ConfigSetting(config, "gui.category menu.head lore favourite", Arrays.asList(
            "&7Tags: &e%head_tags%",
            "",
            "&7Middle-Click to un-favourite",
            "&7Click to take 1",
            "",
            "&e&lFAVOURITED"
    ));

    public static final ConfigSetting GUI_FAVOURITES_TITLE = new ConfigSetting(config, "gui.favourites menu.title", "&e&lFavourite Heads &8(%current_page%/%max_pages%)");
    public static final ConfigSetting GUI_FAVOURITES_HEAD_TITLE = new ConfigSetting(config, "gui.favourites menu.head title", "&e%head_name%");
    public static final ConfigSetting GUI_FAVOURITES_HEAD_LORE = new ConfigSetting(config, "gui.favourites menu.head lore", Arrays.asList(
            "&7Tags: &e%head_tags%",
            "",
            "&7Middle-Click to un-favourite",
            "&7Click to take 1"
    ));

    public static final ConfigSetting GUI_SEARCH_TITLE = new ConfigSetting(config, "gui.search menu.title", "&e&lKeyword&f: &c%keyword%  &8(%current_page%/%max_pages%)");
    public static final ConfigSetting GUI_SEARCH_HEAD_TITLE = new ConfigSetting(config, "gui.search menu.head title", "&e%head_name%");
    public static final ConfigSetting GUI_SEARCH_HEAD_LORE = new ConfigSetting(config, "gui.search menu.head lore", Arrays.asList(
            "&7Category: &e%head_category%",
            "&7Tags: &e%head_tags%",
            "",
            "&7Middle-Click to favourite",
            "&7Click to take 1"
    ));

    public static final ConfigSetting GUI_SEARCH_HEAD_LORE_FAV = new ConfigSetting(config, "gui.search menu.head lore favourite", Arrays.asList(
            "&7Category: &e%head_category%",
            "&7Tags: &e%head_tags%",
            "",
            "&7Middle-Click to un-favourite",
            "&7Click to take 1",
            "",
            "&e&lFAVOURITED"
    ));


    public static void setup() {
        config.load();
        config.setAutoremove(true).setAutosave(true);
        config.saveChanges();
    }
}
