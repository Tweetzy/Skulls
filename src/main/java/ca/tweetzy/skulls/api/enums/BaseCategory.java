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

	ALPHABET("alphabet", Translation.ALPHABET.getString(), Settings.DEFAULT_PRICES_ALPHABET.getDouble()),
	ANIMALS("animals", Translation.ANIMALS.getString(), Settings.DEFAULT_PRICES_ANIMALS.getDouble()),
	BLOCKS("blocks", Translation.BLOCKS.getString(), Settings.DEFAULT_PRICES_BLOCKS.getDouble()),
	DECORATION("decoration", Translation.DECORATION.getString(), Settings.DEFAULT_PRICES_DECORATION.getDouble()),
	FOOD_AND_DRINKS("food & drinks", Translation.FOOD_AND_DRINKS.getString(), Settings.DEFAULT_PRICES_FOOD_AND_DRINKS.getDouble()),
	HUMANS("humans", Translation.HUMANS.getString(), Settings.DEFAULT_PRICES_HUMANS.getDouble()),
	HUMANOID("humanoid", Translation.HUMANOID.getString(), Settings.DEFAULT_PRICES_HUMANOID.getDouble()),
	MISCELLANEOUS("miscellaneous", Translation.MISC.getString(), Settings.DEFAULT_PRICES_MISC.getDouble()),
	MONSTERS("monsters", Translation.MONSTERS.getString(), Settings.DEFAULT_PRICES_MONSTERS.getDouble()),
	PLANTS("plants", Translation.PLANTS.getString(), Settings.DEFAULT_PRICES_PLANTS.getDouble());

	private final String id;
	private final String name;
	private final double defaultPrice;
}
