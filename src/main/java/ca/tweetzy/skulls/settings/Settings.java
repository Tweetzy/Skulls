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

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.config.ConfigEntry;
import ca.tweetzy.flight.config.tweetzy.TweetzyYamlConfig;
import ca.tweetzy.skulls.Skulls;
import lombok.SneakyThrows;

/**
 * Date Created: April 04 2022
 * Time Created: 9:22 p.m.
 *
 * @author Kiran Hart
 */
public final class Settings {

	static final TweetzyYamlConfig config = Skulls.getCoreConfig();

	public static final ConfigEntry LANG = config.createEntry("language", "english").withComment("Default language file");
	public static final ConfigEntry PREFIX = config.createEntry("prefix", "<GRADIENT:DD5E89>&lSkulls</GRADIENT:fbc7d4>&r &8»").withComment("Prefix to be used in chat");
	public static final ConfigEntry CHARGE_FOR_HEADS = config.createEntry("charge for heads", true).withComment("Should skulls charge users without permission for heads?");
	public static final ConfigEntry ECONOMY = config.createEntry("economy", "Vault").withComment("You can use Vault or Item");
	public static final ConfigEntry ITEM_ECONOMY_ITEM = config.createEntry("item economy item", CompMaterial.GOLD_INGOT.name());
	public static final ConfigEntry MAIN_MENU_REQUIRES_NO_PERM = config.createEntry("main menu requires no permission", true);

	public static final ConfigEntry CATEGORIES_ALPHABET_ENABLED = config.createEntry("enabled categories.alphabet", true);
	public static final ConfigEntry CATEGORIES_ANIMALS_ENABLED = config.createEntry("enabled categories.animals", true);
	public static final ConfigEntry CATEGORIES_BLOCKS_ENABLED = config.createEntry("enabled categories.blocks", true);
	public static final ConfigEntry CATEGORIES_DECORATION_ENABLED = config.createEntry("enabled categories.decoration", true);
	public static final ConfigEntry CATEGORIES_FOOD_AND_DRINKS_ENABLED = config.createEntry("enabled categories.food and drinks", true);
	public static final ConfigEntry CATEGORIES_HUMANS_ENABLED = config.createEntry("enabled categories.humans", true);
	public static final ConfigEntry CATEGORIES_HUMANOID_ENABLED = config.createEntry("enabled categories.humanoids", true);
	public static final ConfigEntry CATEGORIES_MISC_ENABLED = config.createEntry("enabled categories.misc", true);
	public static final ConfigEntry CATEGORIES_MONSTERS_ENABLED = config.createEntry("enabled categories.monsters", true);
	public static final ConfigEntry CATEGORIES_PLANTS_ENABLED = config.createEntry("enabled categories.plants", true);

	public static final ConfigEntry PLAYER_HEAD_NAME = config.createEntry("player head.name", "&e%player_name%");
	public static final ConfigEntry PLAYER_HEAD_DROP = config.createEntry("player head.drop enabled", true);
	public static final ConfigEntry PLAYER_HEAD_DROP_CHANCE = config.createEntry("player head.drop chance", 50);

	/*
	==================== GUI END ====================
	 */
	public static final ConfigEntry GUI_MAIN_ITEMS_ALPHABET_SLOT = config.createEntry("gui.main.items.alphabet.slot", 11);
	public static final ConfigEntry GUI_MAIN_ITEMS_ANIMALS_SLOT = config.createEntry("gui.main.items.animals.slot", 12);
	public static final ConfigEntry GUI_MAIN_ITEMS_BLOCKS_SLOT = config.createEntry("gui.main.items.blocks.slot", 13);
	public static final ConfigEntry GUI_MAIN_ITEMS_DECORATION_SLOT = config.createEntry("gui.main.items.decoration.slot", 14);
	public static final ConfigEntry GUI_MAIN_ITEMS_FOOD_AND_DRINKS_SLOT = config.createEntry("gui.main.items.food and drinks.slot", 15);
	public static final ConfigEntry GUI_MAIN_ITEMS_HUMANS_SLOT = config.createEntry("gui.main.items.humans.slot", 20);
	public static final ConfigEntry GUI_MAIN_ITEMS_HUMANOID_SLOT = config.createEntry("gui.main.items.humanoids.slot", 21);
	public static final ConfigEntry GUI_MAIN_ITEMS_MISC_SLOT = config.createEntry("gui.main.items.misc.slot", 22);
	public static final ConfigEntry GUI_MAIN_ITEMS_MONSTERS_SLOT = config.createEntry("gui.main.items.monsters.slot", 23);
	public static final ConfigEntry GUI_MAIN_ITEMS_PLANTS_SLOT = config.createEntry("gui.main.items.plants.slot", 24);


	public static final ConfigEntry DEFAULT_PRICES_ALPHABET = config.createEntry("default prices.alphabet", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_ANIMALS = config.createEntry("default prices.animals", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_BLOCKS = config.createEntry("default prices.blocks", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_DECORATION = config.createEntry("default prices.decoration", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_FOOD_AND_DRINKS = config.createEntry("default prices.food and drinks", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_HUMANS = config.createEntry("default prices.humans", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_HUMANOID = config.createEntry("default prices.humanoids", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_MISC = config.createEntry("default prices.misc", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_MONSTERS = config.createEntry("default prices.monsters", 1.0);
	public static final ConfigEntry DEFAULT_PRICES_PLANTS = config.createEntry("default prices.plants", 1.0);

	@SneakyThrows
	public static void setup() {
		config.init();
	}
}
