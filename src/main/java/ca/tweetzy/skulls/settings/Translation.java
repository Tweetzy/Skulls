/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.settings;

import ca.tweetzy.rose.utils.Replacer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
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
	NOT_A_NUMBER("misc.not a number", "&cThat is not a valid number"),
	CATEGORY_PERMISSION("misc.category permission", "&cYou do not have permission to access that category!"),
	NO_PERMISSIONS("misc.no permission", "&cYou do not have permission to use that!"),
	PLAYER_OFFLINE("misc.player not found", "&cThe player &4%player% &cis not online!"),
	ID_TAKEN("misc.player not found", "&cThat category id is already in use!"),
	SKULL_NOT_FOUND("misc.skull not found", "&cNo skull by that id could be found"),
	NO_SKULL_INFO("misc.no skull info", "&cCould not determine ID of that skull"),
	SKULL_TITLE("skull.name", "&e%skull_name%"),

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

	INPUT_SKULL_EDIT_TITLE("input.skull edit.title", "&eEditing Skull"),
	INPUT_SKULL_EDIT_NAME("input.skull edit.name", "&FEnter the new name skull name"),
	INPUT_SKULL_EDIT_PRICE("input.skull edit.price", "&fEnter the new skull price"),

	INPUT_SKULL_SEARCH_TITLE("input.skull search.title", "&eSkull Search"),
	INPUT_SKULL_SEARCH_SUBTITLE("input.skull search.subtitle", "&fEnter search terms into chat"),

	INPUT_CATEGORY_CREATE_TITLE("input.category create.title", "&eCategory Creation"),
	INPUT_CATEGORY_CREATE_SUBTITLE("input.category create.subtitle", "&fEnter id for new category"),

	INSPECT_INFO("inspect info", Arrays.asList(
			"&8&m-----------------------------------------------------",
			"",
			"&eSkull ID&f: &a%skull_id%",
			"",
			"&8&m-----------------------------------------------------"
	)),

	// GUIS
	GUI_HISTORIES_TITLE("gui.histories.title", "&eSkulls &8> &7Histories"),
	GUI_HISTORIES_ITEMS_HISTORY_NAME("gui.histories.items.history.name", "&e%history_id%"),
	GUI_HISTORIES_ITEMS_HISTORY_LORE("gui.histories.items.history.lore", Arrays.asList(
			"",
			"&7Total Heads&f: &a%history_size%",
			"&7Downloaded&f: %is_true%",
			"&7Time&f: &a%history_time%",
			"",
			"&e&lClick &8» &7To download heads"
	)),

	GUI_CUSTOM_CATEGORY_SELECTOR_TITLE("gui.custom category selector.title", "&eSkulls &8> &7Select Category"),
	GUI_CUSTOM_CATEGORY_SELECTOR_ITEMS_CATEGORY_NAME("gui.custom category selector.items.category.name", "%category_name%"),
	GUI_CUSTOM_CATEGORY_SELECTOR_ITEMS_CATEGORY_LORE("gui.custom category selector.items.category.lore", Collections.singletonList(
			"&e&lClick &8» &7To select category"
	)),

	GUI_CUSTOM_CATEGORY_LIST_TITLE("gui.custom category list.title", "&eSkulls &8> &7Custom Categories"),
	GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_NAME("gui.custom category list.items.category.name", "%category_name%"),
	GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_LORE("gui.custom category list.items.category.lore", Arrays.asList(
			"&8View heads by this category",
			"&7Total Heads&F: &b%category_size%",
			"",
			"&e&lClick &8» &7To view category"
	)),

	GUI_CUSTOM_CATEGORY_LIST_ITEMS_NEW_NAME("gui.custom category list.items.new.name", "<GRADIENT:DD5E89>&lNew Category</GRADIENT:fbc7d4>"),
	GUI_CUSTOM_CATEGORY_LIST_ITEMS_NEW_LORE("gui.custom category list.items.new.lore", Arrays.asList("", "&e&lClick &8» &7To create new category")),


	GUI_EDIT_TITLE("gui.edit.title", "&eSkulls &8> &7Edit &8> &7%skull_id%"),
	GUI_EDIT_ITEMS_NAME_NAME("gui.edit.items.name.name", "<GRADIENT:DD5E89>&lSkull Name</GRADIENT:fbc7d4>"),
	GUI_EDIT_ITEMS_NAME_LORE("gui.edit.items.name.lore", Arrays.asList("", "&e&lClick &8» &7To change skull name")),

	GUI_EDIT_ITEMS_PRICE_NAME("gui.edit.items.price.name", "<GRADIENT:DD5E89>&lSkull Price</GRADIENT:fbc7d4>"),
	GUI_EDIT_ITEMS_PRICE_LORE("gui.edit.items.price.lore", Arrays.asList("", "&e&lClick &8» &7To edit skull price")),

	GUI_EDIT_ITEMS_ADD_CATEGORY_NAME("gui.edit.items.add category.name", "<GRADIENT:DD5E89>&lAdd To Category</GRADIENT:fbc7d4>"),
	GUI_EDIT_ITEMS_ADD_CATEGORY_LORE("gui.edit.items.add category.lore", Arrays.asList("", "&e&lClick &8» &7To add to another category")),

	GUI_EDIT_ITEMS_BLOCKED_NAME("gui.edit.items.blocked.name", "<GRADIENT:DD5E89>&lSkull Blocked</GRADIENT:fbc7d4>"),
	GUI_EDIT_ITEMS_BLOCKED_LORE("gui.edit.items.blocked.lore", Arrays.asList("", "&7Blocked&f: %is_true%", "", "&e&lClick &8» &7To toggle block status")),

	GUI_MAIN_TITLE("gui.main.title", "&eSkulls"),
	GUI_MAIN_ITEMS_CATEGORY_NAME("gui.main.items.category.name", "&e%category_name%"),
	GUI_MAIN_ITEMS_CATEGORY_LORE("gui.main.items.category.lore", Arrays.asList(
			"&8View heads by this category",
			"&7Total Heads&F: &b%category_size%",
			"",
			"&e&lClick &8» &7To view category"
	)),

	GUI_MAIN_ITEMS_FAVOURITES_NAME("gui.main.items.favourites.name", "<GRADIENT:DD5E89>&lFavourites</GRADIENT:fbc7d4>"),
	GUI_MAIN_ITEMS_FAVOURITES_LORE("gui.main.items.favourites.lore", Arrays.asList(
			"&7A collection of every single head",
			"&7that you have favourited.",
			"",
			"&e&lClick &8» &7To view your favourites"
	)),

	GUI_MAIN_ITEMS_SEARCH_NAME("gui.main.items.search.name", "<GRADIENT:DD5E89>&lSearch</GRADIENT:fbc7d4>"),
	GUI_MAIN_ITEMS_SEARCH_LORE("gui.main.items.search.lore", Arrays.asList(
			"",
			"&e&lClick &8» &7To search for heads"
	)),

	GUI_MAIN_ITEMS_CUSTOM_CATEGORIES_NAME("gui.main.items.custom categories.name", "<GRADIENT:DD5E89>&lMore Categories</GRADIENT:fbc7d4>"),
	GUI_MAIN_ITEMS_CUSTOM_CATEGORIES_LORE("gui.main.items.custom categories.lore", Arrays.asList(
			"",
			"&e&lClick &8» &7To view more categories"
	)),


	GUI_SKULLS_LIST_TITLE_CATEGORY("gui.skulls list.title.category", "&eSkulls &8> &7%category_name%"),
	GUI_SKULLS_LIST_TITLE_FAVOURITES("gui.skulls list.title.favourites", "&eSkulls &8> &7Favourites"),
	GUI_SKULLS_LIST_TITLE_SEARCH("gui.skulls list.title.search", "&eSkulls &8> &7%search_phrase%"),
	GUI_SKULLS_LIST_TITLE_ALL("gui.skulls list.title.all", "&eSkulls &8> &7All"),

	GUI_SKULLS_LIST_ITEMS_SKULL_NAME("gui.skulls list.items.skull.name", "&e%skull_name%"),

	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_ID("gui.skulls list.items.skull.lore.id", "&7Id: &e%skull_id%"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAGS("gui.skulls list.items.skull.lore.tags", "&7Tags: &e%skull_tags%"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_PRICE("gui.skulls list.items.skull.lore.price", "&7Cost: &a$%skull_price%"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAKE("gui.skulls list.items.skull.lore.take", "&e&lLeft Click &8» &7To take one"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_FAVOURITE("gui.skulls list.items.skull.lore.favourite", "&e&lRight Click &8» &7To favourite head"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_UN_FAVOURITE("gui.skulls list.items.skull.lore.un favourite", "&e&lRight Click &8» &7To unfavourite head"),
	GUI_SKULLS_LIST_ITEMS_SKULL_LORE_EDIT("gui.skulls list.items.skull.lore.edit", "&b&lPress 1 &8» &7To edit this skull"),


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
