package ca.tweetzy.skulls.settings;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.tweety.configuration.Config;
import ca.tweetzy.tweety.configuration.ConfigSetting;

/**
 * Date Created: April 04 2022
 * Time Created: 9:22 p.m.
 *
 * @author Kiran Hart
 */
public final class Locale {

	static final Config config = Skulls.getInstance().getLang();

	public static final ConfigSetting ALPHABET = new ConfigSetting(config, "alphabet", "Alphabet");
	public static final ConfigSetting ANIMALS = new ConfigSetting(config, "animals", "Animals");
	public static final ConfigSetting BLOCKS = new ConfigSetting(config, "blocks", "Blocks");
	public static final ConfigSetting DECORATION = new ConfigSetting(config, "decoration", "Decoration");
	public static final ConfigSetting FOOD_AND_DRINKS = new ConfigSetting(config, "food and drinks", "Food & Drinks");
	public static final ConfigSetting HUMANS = new ConfigSetting(config, "humans", "Humans");
	public static final ConfigSetting HUMANOID = new ConfigSetting(config, "humanoids", "Humanoids");
	public static final ConfigSetting MISC = new ConfigSetting(config, "misc", "Miscellaneous");
	public static final ConfigSetting MONSTERS = new ConfigSetting(config, "monsters", "Monsters");
	public static final ConfigSetting PLANTS = new ConfigSetting(config, "plants", "Plants");

	public static void setup() {
		config.load();
		config.setAutoremove(false).setAutosave(true);
		config.saveChanges();
	}
}
