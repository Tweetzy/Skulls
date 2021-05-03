package ca.tweetzy.skulls.skull;

import ca.tweetzy.skulls.settings.Settings;
import lombok.Getter;
import lombok.Setter;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 10 2021
 * Time Created: 6:35 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */

@Getter
@Setter
public class SkullCategory {

    public enum BaseCategory {

        ALPHABET("Alphabet", Settings.BASE_PRICE_ALPHABET.getDouble()),
        ANIMALS("Animals", Settings.BASE_PRICE_ANIMALS.getDouble()),
        BLOCKS("Blocks", Settings.BASE_PRICE_BLOCKS.getDouble()),
        DECORATION("Decoration", Settings.BASE_PRICE_DECORATION.getDouble()),
        FOOD_AND_DRINKS("Food and Drinks", Settings.BASE_PRICE_FOOD_AND_DRINK.getDouble()),
        HUMANS("Humans", Settings.BASE_PRICE_HUMANS.getDouble()),
        HUMANOID("Humanoid", Settings.BASE_PRICE_HUMANOID.getDouble()),
        MISCELLANEOUS("Miscellaneous", Settings.BASE_PRICE_MISCELLANEOUS.getDouble()),
        MONSTERS("Monsters", Settings.BASE_PRICE_MONSTERS.getDouble()),
        PLANTS("Plants", Settings.BASE_PRICE_PLANTS.getDouble());

        @Getter
        final String name;

        @Getter
        final double price;


        BaseCategory(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    private String name;
    private BaseCategory baseCategory;
    private boolean isCustom;

    public SkullCategory(String name, BaseCategory baseCategory, boolean isCustom) {
        this.name = name;
        this.baseCategory = baseCategory;
        this.isCustom = isCustom;
    }

    public SkullCategory(BaseCategory baseCategory) {
        this(baseCategory.getName(), baseCategory, false);
    }

    @SuppressWarnings("unused")
    public SkullCategory(String name) {
        this(name, null, true);
    }
}
