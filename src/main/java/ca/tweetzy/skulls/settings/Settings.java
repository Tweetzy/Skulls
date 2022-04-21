package ca.tweetzy.skulls.settings;

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
	public static final ConfigSetting PREFIX = new ConfigSetting(config, "prefix", "<GRADIENT:DD5E89>&lSkulls</GRADIENT:fbc7d4>", "Prefix to be used in chat");

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
