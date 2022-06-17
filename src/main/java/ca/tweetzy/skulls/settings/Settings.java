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

import ca.tweetzy.rose.comp.enums.CompMaterial;
import ca.tweetzy.rose.files.ConfigSetting;
import ca.tweetzy.rose.files.file.YamlFile;
import ca.tweetzy.skulls.Skulls;
import lombok.SneakyThrows;

/**
 * Date Created: April 04 2022
 * Time Created: 9:22 p.m.
 *
 * @author Kiran Hart
 */
public final class Settings {

	static final YamlFile config = Skulls.getInstance().getCoreConfig();

	public static final ConfigSetting LANG = new ConfigSetting(config, "language", "english", "Default language file");
	public static final ConfigSetting PREFIX = new ConfigSetting(config, "prefix", "<GRADIENT:DD5E89>&lSkulls</GRADIENT:fbc7d4>&r", "Prefix to be used in chat");
	public static final ConfigSetting CHARGE_FOR_HEADS = new ConfigSetting(config, "charge for heads", true, "Should skulls charge users without permission for heads?");
	public static final ConfigSetting ECONOMY = new ConfigSetting(config, "economy", "Vault", "You can use Vault or Item");
	public static final ConfigSetting ITEM_ECONOMY_ITEM = new ConfigSetting(config, "item economy item", CompMaterial.GOLD_INGOT.name());

	public static final ConfigSetting CATEGORIES_ALPHABET_ENABLED = new ConfigSetting(config, "enabled categories.alphabet", true);
	public static final ConfigSetting CATEGORIES_ANIMALS_ENABLED = new ConfigSetting(config, "enabled categories.animals", true);
	public static final ConfigSetting CATEGORIES_BLOCKS_ENABLED = new ConfigSetting(config, "enabled categories.blocks", true);
	public static final ConfigSetting CATEGORIES_DECORATION_ENABLED = new ConfigSetting(config, "enabled categories.decoration", true);
	public static final ConfigSetting CATEGORIES_FOOD_AND_DRINKS_ENABLED = new ConfigSetting(config, "enabled categories.food and drinks", true);
	public static final ConfigSetting CATEGORIES_HUMANS_ENABLED = new ConfigSetting(config, "enabled categories.humans", true);
	public static final ConfigSetting CATEGORIES_HUMANOID_ENABLED = new ConfigSetting(config, "enabled categories.humanoids", true);
	public static final ConfigSetting CATEGORIES_MISC_ENABLED = new ConfigSetting(config, "enabled categories.misc", true);
	public static final ConfigSetting CATEGORIES_MONSTERS_ENABLED = new ConfigSetting(config, "enabled categories.monsters", true);
	public static final ConfigSetting CATEGORIES_PLANTS_ENABLED = new ConfigSetting(config, "enabled categories.plants", true);

	public static final ConfigSetting PLAYER_HEAD_NAME = new ConfigSetting(config, "player head name", "&e%player_name%");

	/*
	==================== GUI END ====================
	 */
	public static final ConfigSetting GUI_MAIN_ITEMS_ALPHABET_SLOT = new ConfigSetting(config, "gui.main.items.alphabet.slot", 11);
	public static final ConfigSetting GUI_MAIN_ITEMS_ANIMALS_SLOT = new ConfigSetting(config, "gui.main.items.animals.slot", 12);
	public static final ConfigSetting GUI_MAIN_ITEMS_BLOCKS_SLOT = new ConfigSetting(config, "gui.main.items.blocks.slot", 13);
	public static final ConfigSetting GUI_MAIN_ITEMS_DECORATION_SLOT = new ConfigSetting(config, "gui.main.items.decoration.slot", 14);
	public static final ConfigSetting GUI_MAIN_ITEMS_FOOD_AND_DRINKS_SLOT = new ConfigSetting(config, "gui.main.items.food and drinks.slot", 15);
	public static final ConfigSetting GUI_MAIN_ITEMS_HUMANS_SLOT = new ConfigSetting(config, "gui.main.items.humans.slot", 20);
	public static final ConfigSetting GUI_MAIN_ITEMS_HUMANOID_SLOT = new ConfigSetting(config, "gui.main.items.humanoids.slot", 21);
	public static final ConfigSetting GUI_MAIN_ITEMS_MISC_SLOT = new ConfigSetting(config, "gui.main.items.misc.slot", 22);
	public static final ConfigSetting GUI_MAIN_ITEMS_MONSTERS_SLOT = new ConfigSetting(config, "gui.main.items.monsters.slot", 23);
	public static final ConfigSetting GUI_MAIN_ITEMS_PLANTS_SLOT = new ConfigSetting(config, "gui.main.items.plants.slot", 24);


	public static final ConfigSetting DEFAULT_PRICES_ALPHABET = new ConfigSetting(config, "default prices.alphabet", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_ANIMALS = new ConfigSetting(config, "default prices.animals", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_BLOCKS = new ConfigSetting(config, "default prices.blocks", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_DECORATION = new ConfigSetting(config, "default prices.decoration", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_FOOD_AND_DRINKS = new ConfigSetting(config, "default prices.food and drinks", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_HUMANS = new ConfigSetting(config, "default prices.humans", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_HUMANOID = new ConfigSetting(config, "default prices.humanoids", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_MISC = new ConfigSetting(config, "default prices.misc", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_MONSTERS = new ConfigSetting(config, "default prices.monsters", 1.0);
	public static final ConfigSetting DEFAULT_PRICES_PLANTS = new ConfigSetting(config, "default prices.plants", 1.0);

	@SneakyThrows
	public static void setup() {
		config.applySettings();
		config.save();
	}
}
