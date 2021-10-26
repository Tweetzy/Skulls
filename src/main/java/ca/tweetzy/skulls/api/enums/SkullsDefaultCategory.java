package ca.tweetzy.skulls.api.enums;

import ca.tweetzy.skulls.settings.Settings;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 10:00 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
@Getter
public enum SkullsDefaultCategory {

	ALPHABET("alphabet", Settings.Categories.Alphabet.NAME, Settings.Categories.Alphabet.PRICE),
	ANIMALS("animals", Settings.Categories.Animals.NAME, Settings.Categories.Animals.PRICE),
	BLOCKS("blocks", Settings.Categories.Blocks.NAME, Settings.Categories.Blocks.PRICE),
	DECORATION("decoration", Settings.Categories.Decoration.NAME, Settings.Categories.Decoration.PRICE),
	FOOD_AND_DRINKS("food & drinks", Settings.Categories.FoodAndDrinks.NAME, Settings.Categories.FoodAndDrinks.PRICE),
	HUMANS("humans", Settings.Categories.Humans.NAME, Settings.Categories.Humans.PRICE),
	HUMANOID("humanoid", Settings.Categories.Humanoids.NAME, Settings.Categories.Humanoids.PRICE),
	MISCELLANEOUS("miscellaneous", Settings.Categories.Miscellaneous.NAME, Settings.Categories.Miscellaneous.PRICE),
	MONSTERS("monsters", Settings.Categories.Monsters.NAME, Settings.Categories.Monsters.PRICE),
	PLANTS("plants", Settings.Categories.Plants.NAME, Settings.Categories.Plants.PRICE);

	private final String id;
	private final String name;
	private final double defaultPrice;
}
