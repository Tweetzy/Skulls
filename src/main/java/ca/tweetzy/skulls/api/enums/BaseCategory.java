package ca.tweetzy.skulls.api.enums;

import ca.tweetzy.skulls.settings.Locale;
import ca.tweetzy.skulls.settings.Settings;
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

	ALPHABET("alphabet", Locale.ALPHABET.getString(), Settings.DEFAULT_PRICES_ALPHABET.getDouble()),
	ANIMALS("animals", Locale.ANIMALS.getString(), Settings.DEFAULT_PRICES_ANIMALS.getDouble()),
	BLOCKS("blocks", Locale.BLOCKS.getString(), Settings.DEFAULT_PRICES_BLOCKS.getDouble()),
	DECORATION("decoration", Locale.DECORATION.getString(), Settings.DEFAULT_PRICES_DECORATION.getDouble()),
	FOOD_AND_DRINKS("food & drinks", Locale.FOOD_AND_DRINKS.getString(), Settings.DEFAULT_PRICES_FOOD_AND_DRINKS.getDouble()),
	HUMANS("humans", Locale.HUMANS.getString(), Settings.DEFAULT_PRICES_HUMANS.getDouble()),
	HUMANOID("humanoid", Locale.HUMANOID.getString(), Settings.DEFAULT_PRICES_HUMANOID.getDouble()),
	MISCELLANEOUS("miscellaneous", Locale.MISC.getString(), Settings.DEFAULT_PRICES_MISC.getDouble()),
	MONSTERS("monsters", Locale.MONSTERS.getString(), Settings.DEFAULT_PRICES_MONSTERS.getDouble()),
	PLANTS("plants", Locale.PLANTS.getString(), Settings.DEFAULT_PRICES_PLANTS.getDouble());

	private final String id;
	private final String name;
	private final double defaultPrice;
}
