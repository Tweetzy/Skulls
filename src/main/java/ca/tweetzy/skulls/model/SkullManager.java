package ca.tweetzy.skulls.model;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.SkullsDefaultCategory;
import ca.tweetzy.skulls.impl.Skull;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.FileUtil;
import ca.tweetzy.tweety.RandomUtil;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.remain.Remain;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 10:12 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class SkullManager {

	@Getter
	@Setter
	private boolean downloading = false;

	@Getter
	private boolean loading = false;

	@Getter
	private final StrictList<Skull> skulls = new StrictList<>();

	public void addSkull(@NonNull final Skull skull) {
		this.skulls.addIfNotExist(skull);
	}

	public void removeSkull(@NonNull final Skull skull) {
		this.skulls.remove(skull);
	}

	public Skull getSkull(final int id) {
		return this.skulls.getSource().stream().filter(all -> all.getId() == id).findFirst().orElse(null);
	}

	public Skull getRandomSkull() {
		return RandomUtil.nextItem(this.skulls);
	}

	public ItemStack getSkullItemStack(final int id) {
		return this.getSkull(id).getItemStack();
	}

	public List<Skull> getSkullsByCategory(@NonNull final String category) {
		return this.skulls.getSource().stream().filter(skull -> skull.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
	}

	public List<Skull> getSkullsByCategory(@NonNull final SkullCategory category) {
		return this.getSkullsByCategory(category.getId());
	}

	public List<Skull> getSkullsByTerm(@NonNull final String keywords) {
		if (keywords.startsWith("id:")) {
			final int id = Integer.parseInt(keywords.split(":")[1]);
			return new ArrayList<>(Collections.singleton(this.getSkull(id)));
		}

		final List<Skull> locatedSkulls = new ArrayList<>();

		for (Skull skull : this.skulls) {
			if (match(keywords, skull.getName()) || match(keywords, skull.getCategory()) || skull.getTags().getSource().stream().anyMatch(tag -> match(keywords, tag)))
				locatedSkulls.add(skull);
		}

		return locatedSkulls;
	}

	public List<Skull> getSkullsByIds(@NonNull final List<Integer> ids) {
		final List<Skull> locatedSkulls = new ArrayList<>();
		for (final int id : ids) {
				locatedSkulls.add(getSkull(id));
		}
		return locatedSkulls;
	}

	/**
	 * For internal use only
	 */
	public void downloadHeadCategory(@NonNull final SkullsDefaultCategory category) {
		try {
			final File dir = new File(Skulls.getInstance().getDataFolder() + "/heads");
			if (!dir.exists()) {
				//noinspection ResultOfMethodCallIgnored
				dir.mkdir();
			}

			final JsonArray json;
			switch (category) {
				case ALPHABET:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=alphabet");
					break;
				case ANIMALS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=animals");
					;
					break;
				case BLOCKS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=blocks");
					break;
				case DECORATION:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=decoration");
					break;
				case FOOD_AND_DRINKS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=food%20&%20drinks");
					break;
				case HUMANS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=humans");
					break;
				case HUMANOIDS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=humanoid");
					break;
				case MISCELLANEOUS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=miscellaneous");
					break;
				case MONSTERS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=monsters");
					break;
				case PLANTS:
					json = getSkullsJson("https://rose.tweetzy.ca/minecraft/skulls?category=plants");
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + category);
			}

			final FileWriter fileWriter = new FileWriter(Skulls.getInstance().getDataFolder() + "/heads/" + category.getId() + ".json");
			fileWriter.write(json.toString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For internal use only
	 */
	public void downloadHeads(final boolean redownload) {
		if (redownload) {
			final File directory = FileUtil.getFile("/heads");
			if (directory.exists())
				directory.delete();
		}

		final File[] knownFiles = FileUtil.getFiles("/heads", "json");
		if (knownFiles.length != 10) {
			for (SkullsDefaultCategory category : SkullsDefaultCategory.values()) {
				final File headFile = FileUtil.getFile("/heads/" + category.getId() + ".json");
				if (!headFile.exists()) {
					Common.tell(Bukkit.getConsoleSender(), "&eDownloading heads for category&f: &6" + category.getId());
					downloadHeadCategory(category);
				}
			}
		}

		loadHeads();
	}

	/**
	 * For internal use only
	 */
	private void loadHeads() {
		final File[] headFiles = FileUtil.getFiles("/heads", "json");
		final JsonParser parser = new JsonParser();
		this.loading = true;

		try {
			for (final File headFile : headFiles) {
				final JsonArray jsonArray = (JsonArray) parser.parse(new FileReader(headFile));

				jsonArray.forEach(jsonElement -> {
					final JsonObject jsonObject = jsonElement.getAsJsonObject();
					addSkull(new Skull(
							Integer.parseInt(replace(jsonObject.get("id").toString())),
							replace(jsonObject.get("name").toString()),
							replace(jsonObject.get("category").toString()),
							new StrictList<>(replace(jsonObject.get("tags").toString()).split(",")),
							replace(jsonObject.get("texture").toString())
					));
				});
			}

			this.loading = false;
			Common.tell(Bukkit.getConsoleSender(), "&eLoaded " + this.skulls.size() + " skulls");
			Remain.getOnlinePlayers().stream().filter(Player::isOp).sequential().forEach(player -> {
				Common.tell(player, "&aSkulls is ready for usage, loaded " +this.skulls.size() + " skulls.");
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For internal use only
	 */
	private JsonArray getSkullsJson(final String url) throws IOException {
		final InputStream inputStream = new URL(url).openStream();
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		final StringBuilder builder = new StringBuilder();

		int character;
		while ((character = bufferedReader.read()) != -1) {
			builder.append((char) character);
		}

		final JsonParser parser = new JsonParser();
		return (JsonArray) parser.parse(builder.toString());
	}

	/**
	 * For internal use only
	 */
	private String replace(String in) {
		return in.replace("\"", "");
	}

	/**
	 * For internal use only
	 */
	private boolean match(String pattern, String sentence) {
		Pattern patt = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patt.matcher(sentence);
		return matcher.find();
	}
}
