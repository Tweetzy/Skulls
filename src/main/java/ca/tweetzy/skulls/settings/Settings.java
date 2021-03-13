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

    public static final ConfigSetting GUI_BACK_SHIFT_CLICK_AMT = new ConfigSetting(config, "gui.shift click back button pages", 5, "How many pages should be navigated when", "you shift click the back button in a gui");
    public static final ConfigSetting GUI_NEXT_SHIFT_CLICK_AMT = new ConfigSetting(config, "gui.shift click next button pages", 5, "How many pages should be navigated when", "you shift click the next button in a gui");

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
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_ITEM = new ConfigSetting(config, "gui.main menu.items.animal.item", "CREEPER_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_TEXTURE = new ConfigSetting(config, "gui.main menu.items.animal.texture", MinecraftHeadsLinks.ALPHABET);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_NAME = new ConfigSetting(config, "gui.main menu.items.animal.name", "&e&lAnimals");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_ANIMAL_LORE = new ConfigSetting(config, "gui.main menu.items.animal.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_ITEM = new ConfigSetting(config, "gui.main menu.items.block.item", "GRASS_BLOCK");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_TEXTURE = new ConfigSetting(config, "gui.main menu.items.block.texture", MinecraftHeadsLinks.BLOCK);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_NAME = new ConfigSetting(config, "gui.main menu.items.block.name", "&e&lBlocks");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_BLOCK_LORE = new ConfigSetting(config, "gui.main menu.items.block.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_ITEM = new ConfigSetting(config, "gui.main menu.items.decoration.item", "POPPY");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_TEXTURE = new ConfigSetting(config, "gui.main menu.items.decoration.texture", MinecraftHeadsLinks.DECORATION);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_NAME = new ConfigSetting(config, "gui.main menu.items.decoration.name", "&e&LDecorations");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_DECORATION_LORE = new ConfigSetting(config, "gui.main menu.items.decoration.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_ITEM = new ConfigSetting(config, "gui.main menu.items.food.item", "COOKED_BEEF");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.food.texture", MinecraftHeadsLinks.FOOD_AND_DRINK);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_NAME = new ConfigSetting(config, "gui.main menu.items.food.name", "&e&lFood & Drinks");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FOODS_LORE = new ConfigSetting(config, "gui.main menu.items.food.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_ITEM = new ConfigSetting(config, "gui.main menu.items.human.item", "PLAYER_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.human.texture", MinecraftHeadsLinks.HUMAN);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_NAME = new ConfigSetting(config, "gui.main menu.items.human.name", "&e&lHumans");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANS_LORE = new ConfigSetting(config, "gui.main menu.items.human.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_ITEM = new ConfigSetting(config, "gui.main menu.items.humanoid.item", "ZOMBIE_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_TEXTURE = new ConfigSetting(config, "gui.main menu.items.humanoid.texture", MinecraftHeadsLinks.HUMANOID);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_NAME = new ConfigSetting(config, "gui.main menu.items.humanoid.name", "&e&lHumanoids");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_HUMANOID_LORE = new ConfigSetting(config, "gui.main menu.items.humanoid.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_ITEM = new ConfigSetting(config, "gui.main menu.items.miscellaneous.item", "NETHER_STAR");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.miscellaneous.texture", MinecraftHeadsLinks.MISCELLANEOUS);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_NAME = new ConfigSetting(config, "gui.main menu.items.miscellaneous.name", "&e&lMiscellaneous");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_LORE = new ConfigSetting(config, "gui.main menu.items.miscellaneous.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_ITEM = new ConfigSetting(config, "gui.main menu.items.monster.item", "DRAGON_HEAD");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.monster.texture", MinecraftHeadsLinks.MONSTER);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_NAME = new ConfigSetting(config, "gui.main menu.items.monster.name", "&e&lMonsters");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_MONSTERS_LORE = new ConfigSetting(config, "gui.main menu.items.monster.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_ITEM = new ConfigSetting(config, "gui.main menu.items.plants.item", "SUNFLOWER");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_TEXTURE = new ConfigSetting(config, "gui.main menu.items.plants.texture", MinecraftHeadsLinks.PLANT);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_NAME = new ConfigSetting(config, "gui.main menu.items.plants.name", "&e&lPlants");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLANTS_LORE = new ConfigSetting(config, "gui.main menu.items.plants.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FAVOURITES_NAME = new ConfigSetting(config, "gui.main menu.items.favourites.name", "&e&lFavourite Heads");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FAVOURITES_TEXTURE = new ConfigSetting(config, "gui.main menu.items.favourites.texture", MinecraftHeadsLinks.STAR_HEAD);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_FAVOURITES_LORE = new ConfigSetting(config, "gui.main menu.items.favourites.lore", Collections.singletonList(
            "&7Click to view favorite heads"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLAYERS_NAME = new ConfigSetting(config, "gui.main menu.items.player heads.name", "&e&lPlayer Heads");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_PLAYERS_LORE = new ConfigSetting(config, "gui.main menu.items.player heads.lore", Collections.singletonList(
            "&7Click to view player heads"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_SEARCH_NAME = new ConfigSetting(config, "gui.main menu.items.search.name", "&e&lSearch Heads");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_SEARCH_TEXTURE = new ConfigSetting(config, "gui.main menu.items.search.texture", MinecraftHeadsLinks.QUESTION_MARK);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_SEARCH_LORE = new ConfigSetting(config, "gui.main menu.items.search.lore", Collections.singletonList(
            "&7Click to search by a keyword"
    ));

    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_CUSTOM_CATEGORIES_NAME = new ConfigSetting(config, "gui.main menu.items.custom categories.name", "&e&lCustom Categories");
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_CUSTOM_CATEGORIES_TEXTURE = new ConfigSetting(config, "gui.main menu.items.custom categories.texture", MinecraftHeadsLinks.BOOK_STACK);
    public static final ConfigSetting GUI_MAIN_GUI_ITEMS_CUSTOM_CATEGORIES_LORE = new ConfigSetting(config, "gui.main menu.items.custom categories.lore", Collections.singletonList(
            "&7Click to view custom made categories"
    ));

    public static final ConfigSetting GUI_CATEGORY_GUI_TITLE = new ConfigSetting(config, "gui.category menu.title", "&e&l%category% &8(%current_page%/%max_pages%)", "Valid Placeholders", "%current_page%", "%max_pages%");
    public static final ConfigSetting GUI_CATEGORY_HEAD_TITLE = new ConfigSetting(config, "gui.category menu.head title", "&e%head_name%", "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");
    public static final ConfigSetting GUI_CATEGORY_HEAD_LORE = new ConfigSetting(config, "gui.category menu.head lore", Arrays.asList(
            "&7Tags: &e%head_tags%",
            "",
            "&7Left-Click to take 1",
            "&7Middle-Click to add to category",
            "&7Right-Click to favourite"
    ), "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");

    public static final ConfigSetting GUI_CATEGORY_HEAD_LORE_FAV = new ConfigSetting(config, "gui.category menu.head lore favourite", Arrays.asList(
            "&7Tags: &e%head_tags%",
            "",
            "&7Left-Click to take 1",
            "&7Middle-Click to add to category",
            "&7Right-Click to un-favourite",
            "",
            "&e&lFAVOURITED"
    ), "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");

    public static final ConfigSetting GUI_FAVOURITES_TITLE = new ConfigSetting(config, "gui.favourites menu.title", "&e&lFavourite Heads &8(%current_page%/%max_pages%)", "Valid Placeholders", "%current_page%", "%max_pages%");
    public static final ConfigSetting GUI_FAVOURITES_HEAD_TITLE = new ConfigSetting(config, "gui.favourites menu.head title", "&e%head_name%", "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");
    public static final ConfigSetting GUI_FAVOURITES_HEAD_LORE = new ConfigSetting(config, "gui.favourites menu.head lore", Arrays.asList(
            "&7Tags: &e%head_tags%",
            "",
            "&7Left-Click to take 1",
            "&7Middle-Click to add to category",
            "&7Right-Click to un-favourite"
    ), "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");

    public static final ConfigSetting GUI_SEARCH_TITLE = new ConfigSetting(config, "gui.search menu.title", "&e&lKeyword&f: &c%keyword%  &8(%current_page%/%max_pages%)", "Valid Placeholders", "%current_page%", "%max_pages%");
    public static final ConfigSetting GUI_SEARCH_HEAD_TITLE = new ConfigSetting(config, "gui.search menu.head title", "&e%head_name%", "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");
    public static final ConfigSetting GUI_SEARCH_HEAD_LORE = new ConfigSetting(config, "gui.search menu.head lore", Arrays.asList(
            "&7Category: &e%head_category%",
            "&7Tags: &e%head_tags%",
            "",
            "&7Left-Click to take 1",
            "&7Middle-Click to add to category",
            "&7Right-Click to favourite"
    ), "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");

    public static final ConfigSetting GUI_SEARCH_HEAD_LORE_FAV = new ConfigSetting(config, "gui.search menu.head lore favourite", Arrays.asList(
            "&7Category: &e%head_category%",
            "&7Tags: &e%head_tags%",
            "",
            "&7Left-Click to take 1",
            "&7Middle-Click to add to category",
            "&7Right-Click to un-favourite",
            "",
            "&e&lFAVOURITED"
    ), "Valid Placeholders", "%head_id%", "%head_name%", "%head_category%", "%head_tags%");

    public static final ConfigSetting GUI_PLAYERS_TITLE = new ConfigSetting(config, "gui.players menu.title", "&e&lOnline Players &8(%current_page%/%max_pages%)", "Valid Placeholders", "%current_page%", "%max_pages%");
    public static final ConfigSetting GUI_PLAYERS_HEAD_TITLE = new ConfigSetting(config, "gui.players menu.head title", "&e%head_name%", "Valid Placeholders:", "%head_name%");
    public static final ConfigSetting GUI_PLAYERS_HEAD_LORE = new ConfigSetting(config, "gui.players menu.head lore", Collections.singletonList(
            "&7Click to take 1"
    ));

    public static final ConfigSetting GUI_CUSTOM_CATEGORY_LIST_TITLE = new ConfigSetting(config, "gui.custom category list.title", "&e&lCustom Categories &8(%current_page%/%max_pages%)", "Valid Placeholders", "%current_page%", "%max_pages%");
    public static final ConfigSetting GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_TITLE = new ConfigSetting(config, "gui.custom category list.items.category.title", "&e%category_name%", "Valid Placeholders", "%category_id%", "%category_name%");
    public static final ConfigSetting GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_LORE = new ConfigSetting(config, "gui.custom category list.items.category.lore", Arrays.asList(
            "&7Click to view heads in this category",
            "&7Total Heads: &e%head_count%",
            "",
            "&7Middle-Click to change head/icon",
            "&7Shift Right-Click to delete",
            "&7Right-Click to rename"
    ), "Valid Placeholders", "%head_count%");

    public static final ConfigSetting GUI_CUSTOM_CATEGORY_LIST_ITEMS_ADD_TITLE = new ConfigSetting(config, "gui.custom category list.items.add.title", "&a&LNew Category");
    public static final ConfigSetting GUI_CUSTOM_CATEGORY_LIST_ITEMS_ADD_TEXTURE = new ConfigSetting(config, "gui.custom category list.items.add.texture", MinecraftHeadsLinks.PLUS_SIGN);
    public static final ConfigSetting GUI_CUSTOM_CATEGORY_LIST_ITEMS_ADD_LORE = new ConfigSetting(config, "gui.custom category list.items.add.lore", Arrays.asList(
            "&7Click to create a new category",
            "&6to store select heads."
    ));

    public static void setup() {
        config.load();
        config.setAutoremove(true).setAutosave(true);
        config.saveChanges();
    }
}
