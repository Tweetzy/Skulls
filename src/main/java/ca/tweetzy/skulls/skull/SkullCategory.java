package ca.tweetzy.skulls.skull;

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

        ALPHABET("Alphabet"),
        ANIMALS("Animals"),
        BLOCKS("Blocks"),
        DECORATION("Decoration"),
        FOOD_AND_DRINKS("Food and Drinks"),
        HUMANS("Humans"),
        HUMANOID("Humanoid"),
        MISCELLANEOUS("Miscellaneous"),
        MONSTERS("Monsters"),
        PLANTS("Plants");

        @Getter
        final String name;

        BaseCategory(String name) {
            this.name = name;
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
}
