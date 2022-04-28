package ca.tweetzy.skulls.settings;

import ca.tweetzy.rose.utils.Replacer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Date Created: April 18 2022
 * Time Created: 3:18 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
@Getter
public enum Translation {

	/*
	============= misc =============
	 */
	MISC_IS_TRUE("misc.is true", "&ATrue"),
	MISC_IS_FALSE("misc.is false", "&cFalse"),
	MISC_IS_ALLOWED("misc.is allowed", "&aAllowed"),
	MISC_IS_DISALLOWED("misc.is disallowed", "&cDisallowed"),
	NOT_ENOUGH_MONEY("misc.not enough money", "&cYou do not have enough money!"),

	ALPHABET("categories.alphabet", "Alphabet"),
	ANIMALS("categories.animals", "Animals"),
	BLOCKS("categories.blocks", "Blocks"),
	DECORATION("categories.decoration", "Decoration"),
	FOOD_AND_DRINKS("categories.food and drinks", "Food & Drinks"),
	HUMANS("categories.humans", "Humans"),
	HUMANOID("categories.humanoids", "Humanoids"),
	MISC("categories.misc", "Miscellaneous"),
	MONSTERS("categories.monsters", "Monsters"),
	PLANTS("categories.plants", "Plants"),

	// GUIS

	GUI_MAIN_TITLE("gui.main.title", "&eSkulls"),
	GUI_MAIN_ITEMS_CATEGORY_NAME("gui.main.items.category.name", "&e%category_name%"),
	GUI_MAIN_ITEMS_CATEGORY_LORE("gui.main.items.category.lore", Arrays.asList(
			"&8View heads by this category",
			"&7Total Heads&F: &b%category_size%",
			"",
			"&e&lClick &8» &7To view category"
	)),

	GUI_SKULLS_LIST_TITLE_CATEGORY("gui.skulls list.title.category", "&eSkulls &8> &7%category_name% &8> &7%current_page%"),
	GUI_SKULLS_LIST_TITLE_FAVOURITES("gui.skulls list.title.favourites", "&eSkulls &8> &7Favourites &8> &7%current_page%"),
	GUI_SKULLS_LIST_TITLE_SEARCH("gui.skulls list.title.search", "&eSkulls &8> &7%search_phrase% &8> &7%current_page%"),
	GUI_SKULLS_LIST_TITLE_ALL("gui.skulls list.title.all", "&eSkulls &8> &7All &8> &7%current_page%"),

	GUI_SKULLS_LIST_ITEMS_SKULL_NAME("gui.skulls list.items.skull.name", "&e%skull_name%"),

	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_ID("gui.skulls list.items.skull.lore.id", "&7Id: &e%skull_id%"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAGS("gui.skulls list.items.skull.lore.tags", "&7Tags: &e%skull_tags%"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_PRICE("gui.skulls list.items.skull.lore.price", "&7Cost: &a$%skull_price%"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAKE("gui.skulls list.items.skull.lore.take", "&e&lLeft Click &8» &7To take one"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_FAVOURITE("gui.skulls list.items.skull.lore.favourite", "&e&lRight Click &8» &7To favourite head"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_UN_FAVOURITE("gui.skulls list.items.skull.lore.un favourite", "&e&lRight Click &8» &7To unfavourite head"),

	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_FAVOURITED("gui.skulls list.items.skull.lore.favourited", "&e&lFAVOURITED"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_BLOCKED("gui.skulls list.items.skull.lore.blocked", "&c&oPurchase is blocked"),


	;

	final String key;
	final Object value;

	public String getString(Object... replacements) {
		return Replacer.replaceVariables(Locale.getString(this.key), replacements);
	}

	public List<String> getList(Object... replacements) {
		return Replacer.replaceVariables(Locale.getList(this.key), replacements);
	}
}
