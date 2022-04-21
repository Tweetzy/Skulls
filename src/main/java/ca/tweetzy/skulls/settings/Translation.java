package ca.tweetzy.skulls.settings;

import ca.tweetzy.rose.utils.Replacer;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

	GUI_MAIN_TITLE("gui.main.title", "&eSkulls"),

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
