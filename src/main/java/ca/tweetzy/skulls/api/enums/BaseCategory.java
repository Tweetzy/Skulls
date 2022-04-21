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

	ALPHABET("alphabet", Translation.ALPHABET.getString(), Settings.DEFAULT_PRICES_ALPHABET.getDouble(), 164),
	ANIMALS("animals", Translation.ANIMALS.getString(), Settings.DEFAULT_PRICES_ANIMALS.getDouble(), 26960),
	BLOCKS("blocks", Translation.BLOCKS.getString(), Settings.DEFAULT_PRICES_BLOCKS.getDouble(),24064),
	DECORATION("decoration", Translation.DECORATION.getString(), Settings.DEFAULT_PRICES_DECORATION.getDouble(),46908),
	FOOD_AND_DRINKS("food & drinks", Translation.FOOD_AND_DRINKS.getString(), Settings.DEFAULT_PRICES_FOOD_AND_DRINKS.getDouble(),2686),
	HUMANS("humans", Translation.HUMANS.getString(), Settings.DEFAULT_PRICES_HUMANS.getDouble(),47833),
	HUMANOID("humanoid", Translation.HUMANOID.getString(), Settings.DEFAULT_PRICES_HUMANOID.getDouble(),46664),
	MISCELLANEOUS("miscellaneous", Translation.MISC.getString(), Settings.DEFAULT_PRICES_MISC.getDouble(),45534),
	MONSTERS("monsters", Translation.MONSTERS.getString(), Settings.DEFAULT_PRICES_MONSTERS.getDouble(),47778),
	PLANTS("plants", Translation.PLANTS.getString(), Settings.DEFAULT_PRICES_PLANTS.getDouble(),44334);

	private final String id;
	private final String name;
	private final double defaultPrice;
	private final int texture;
}
