package ca.tweetzy.skulls.api.enums;

import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translation;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date Created: April 04 2022
 * Time Created: 9:03 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
@Getter
public enum BaseCategory {

	ALPHABET("alphabet", Translation.ALPHABET.getString(), Settings.DEFAULT_PRICES_ALPHABET.getDouble(), 164, Settings.GUI_MAIN_ITEMS_ALPHABET_SLOT.getInt(), Settings.CATEGORIES_ALPHABET_ENABLED.getBoolean()),
	ANIMALS("animals", Translation.ANIMALS.getString(), Settings.DEFAULT_PRICES_ANIMALS.getDouble(), 26960, Settings.GUI_MAIN_ITEMS_ANIMALS_SLOT.getInt(), Settings.CATEGORIES_ANIMALS_ENABLED.getBoolean()),
	BLOCKS("blocks", Translation.BLOCKS.getString(), Settings.DEFAULT_PRICES_BLOCKS.getDouble(), 24064, Settings.GUI_MAIN_ITEMS_BLOCKS_SLOT.getInt(), Settings.CATEGORIES_BLOCKS_ENABLED.getBoolean()),
	DECORATION("decoration", Translation.DECORATION.getString(), Settings.DEFAULT_PRICES_DECORATION.getDouble(), 46908, Settings.GUI_MAIN_ITEMS_DECORATION_SLOT.getInt(), Settings.CATEGORIES_DECORATION_ENABLED.getBoolean()),
	FOOD_AND_DRINKS("food & drinks", Translation.FOOD_AND_DRINKS.getString(), Settings.DEFAULT_PRICES_FOOD_AND_DRINKS.getDouble(), 2686, Settings.GUI_MAIN_ITEMS_FOOD_AND_DRINKS_SLOT.getInt(), Settings.CATEGORIES_FOOD_AND_DRINKS_ENABLED.getBoolean()),
	HUMANS("humans", Translation.HUMANS.getString(), Settings.DEFAULT_PRICES_HUMANS.getDouble(), 47833, Settings.GUI_MAIN_ITEMS_HUMANS_SLOT.getInt(), Settings.CATEGORIES_HUMANS_ENABLED.getBoolean()),
	HUMANOID("humanoid", Translation.HUMANOID.getString(), Settings.DEFAULT_PRICES_HUMANOID.getDouble(), 46664, Settings.GUI_MAIN_ITEMS_HUMANOID_SLOT.getInt(), Settings.CATEGORIES_HUMANOID_ENABLED.getBoolean()),
	MISCELLANEOUS("miscellaneous", Translation.MISC.getString(), Settings.DEFAULT_PRICES_MISC.getDouble(), 45534, Settings.GUI_MAIN_ITEMS_MISC_SLOT.getInt(), Settings.CATEGORIES_MISC_ENABLED.getBoolean()),
	MONSTERS("monsters", Translation.MONSTERS.getString(), Settings.DEFAULT_PRICES_MONSTERS.getDouble(), 47778, Settings.GUI_MAIN_ITEMS_MONSTERS_SLOT.getInt(), Settings.CATEGORIES_MONSTERS_ENABLED.getBoolean()),
	PLANTS("plants", Translation.PLANTS.getString(), Settings.DEFAULT_PRICES_PLANTS.getDouble(), 44334, Settings.GUI_MAIN_ITEMS_PLANTS_SLOT.getInt(), Settings.CATEGORIES_PLANTS_ENABLED.getBoolean());


	private final String id;
	private final String name;
	private final double defaultPrice;
	private final int texture;
	private final int slot;
	private final boolean enabled;


	public static BaseCategory getById(String id) {
		BaseCategory category = null;

		for (BaseCategory value : values()) {
			if (!value.getId().equalsIgnoreCase(id)) continue;
			category = value;
			break;
		}
		
		return category;
	}
}
