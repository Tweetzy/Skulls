/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.settings;

import ca.tweetzy.rose.files.comments.format.YamlCommentFormat;
import ca.tweetzy.rose.files.file.YamlFile;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.skulls.Skulls;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date Created: April 15 2022
 * Time Created: 11:39 a.m.
 *
 * @author Kiran Hart
 */
@SuppressWarnings("all")
public final class Locale {

	private static final Map<String, YamlFile> LOCALES = new HashMap<>();
	private static final Map<String, Object> PHRASES = new HashMap<>();

	private static String defaultLanguage = "english";

	@SneakyThrows
	public static void setup() {
		for (Translation translation : Translation.values())
			PHRASES.put(translation.getKey(), translation.getValue());

		defaultLanguage = Settings.LANG.getString();

		setupDefaults("english"); // we always want to have english ready
		setupDefaults(defaultLanguage);

		final File[] files = getFiles("locales", "yml");
		for (File file : files) {
			if (file.getName().equalsIgnoreCase("english.yml")) continue;
			if (file.getName().equalsIgnoreCase(defaultLanguage + ".yml")) continue;

			final YamlFile yamlFile = new YamlFile(Skulls.getInstance().getDataFolder() + "/locales/" + file.getName());
			yamlFile.createOrLoadWithComments();
			yamlFile.setCommentFormat(YamlCommentFormat.PRETTY);

			PHRASES.forEach((key, value) -> {
				if (!yamlFile.isSet(key))
					yamlFile.set(key, value);
			});

			yamlFile.path("file language").set(file.getName().replace(".yml", "")).comment("For internal use, this is auto generated based on file name");

			yamlFile.save();
			LOCALES.put(file.getName().replace(".yml", ""), yamlFile);
		}
	}

	@SneakyThrows
	private static void setupDefaults(String name) {
		final YamlFile yamlFile = new YamlFile(Skulls.getInstance().getDataFolder() + "/locales/" + name + ".yml");
		yamlFile.createOrLoadWithComments();
		yamlFile.setCommentFormat(YamlCommentFormat.PRETTY);

		PHRASES.forEach((key, value) -> {
			if (!yamlFile.isSet(key))
				yamlFile.set(key, value);
		});

		yamlFile.path("file language")
				.set(name)
				.comment("This is the default language for Skulls to use another language" +
						"\nchange the default language in the config.yml" +
						"\nif the file does not exists, it will generate using the default english" +
						"\ntranslations, you can then make edits from there.");

		yamlFile.save();

		LOCALES.put(name, yamlFile);
	}

	public static String getString(String key) {
		return (String) getPhrase(key, defaultLanguage);
	}

	public static List<String> getList(String key) {
		return (List<String>) getPhrase(key, defaultLanguage);
	}

	public static String getString(String key, String language) {
		return (String) getPhrase(key, language);
	}

	public static List<String> getList(String key, String language) {
		return (List<String>) getPhrase(key, language);
	}

	private static Object getPhraseEnglish(String key) {
		return LOCALES.get("english").get(key);
	}

	private static Object getPhrase(String key, String language) {
		YamlFile file = LOCALES.get(language);

		if (file == null)
			return getPhraseEnglish(key);

		return file.get(key, getPhraseEnglish(key));
	}

	public static void tell(CommandSender sender, String key) {

		tell(sender, defaultLanguage, key);
	}

	public static void tell(CommandSender sender, String language, String key) {
		Object phrase = getPhrase(key, language);

		if (phrase instanceof String)
			Common.tell(sender, (String) phrase);

		if (phrase instanceof List)
			Common.tell(sender, ((List<String>) phrase).toArray(new String[0]));
	}

	private static File[] getFiles(@NonNull String directory, @NonNull String extension) {
		// Remove initial dot, if any
		if (extension.startsWith("."))
			extension = extension.substring(1);

		final File dataFolder = new File(Skulls.getInstance().getDataFolder(), directory);

		if (!dataFolder.exists())
			dataFolder.mkdirs();

		final String finalExtension = extension;

		return dataFolder.listFiles((FileFilter) file -> !file.isDirectory() && file.getName().endsWith("." + finalExtension));
	}
}
