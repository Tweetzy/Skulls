package ca.tweetzy.skulls.manager;

import ca.tweetzy.rose.comp.enums.CompMaterial;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.History;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.impl.InsertHistory;
import ca.tweetzy.skulls.impl.TexturedSkull;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date Created: April 04 2022
 * Time Created: 8:48 p.m.
 *
 * @author Kiran Hart
 */
public final class SkullManager implements Manager {

	@Getter
	@Setter
	private boolean downloading = false;

	@Getter
	@Setter
	private boolean loading = false;

	@Getter
	private final List<Skull> skulls = Collections.synchronizedList(new ArrayList<>());

	@Getter
	private final List<History> histories = Collections.synchronizedList(new ArrayList<>());

	public Skull getSkull(final int id) {
		synchronized (this.skulls) {
			return this.skulls.stream().filter(skull -> skull.getId() == id).findFirst().orElse(null);
		}
	}

	public List<Skull> getSkulls(BaseCategory category) {
		synchronized (this.skulls) {
			return this.skulls.stream().filter(skull -> skull.getCategory().equalsIgnoreCase(category.getId())).collect(Collectors.toList());
		}
	}

	public List<Skull> getSkulls(String category) {
		synchronized (this.skulls) {
			if (BaseCategory.getById(category) != null)

				return this.skulls.stream().filter(skull -> skull.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());

			return this.skulls.stream().filter(skull -> Skulls.getCategoryManager().findCategory(category).getSkulls().contains(skull.getId())).collect(Collectors.toList());
		}
	}

	public List<Skull> getSkullsBySearch(String phrase) {
		synchronized (this.skulls) {
			int id = -1;
			if (phrase.startsWith("id:")) {
				if (NumberUtils.isNumber(phrase.split(":")[1])) {
					id = Integer.parseInt(phrase.split(":")[1]);
				}
			}

			if (id != -1)
				return Collections.singletonList(getSkull(id));

			return this.skulls.stream().filter(skull -> BaseCategory.getById(skull.getCategory()).isEnabled() && (Common.match(phrase, skull.getName()) || Common.match(phrase, skull.getCategory()) || skull.getTags().stream().anyMatch(tag -> Common.match(phrase, tag)))).collect(Collectors.toList());
		}
	}

	public List<Skull> getSkulls(List<Integer> ids) {
		synchronized (this.skulls) {
			return this.skulls.stream().filter(skull -> ids.contains(skull.getId())).collect(Collectors.toList());
		}
	}

	public long getSkullCount(String category) {
		synchronized (this.skulls) {
			return this.skulls.stream().filter(skull -> skull.getCategory().equalsIgnoreCase(category)).count();
		}
	}

	public ItemStack getSkullItem(final int id) {
		synchronized (this.skulls) {
			final Skull skull = getSkull(id);
			return skull == null ? QuickItem.of(CompMaterial.PLAYER_HEAD).make() : skull.getItemStack();
		}
	}

	/**
	 * For internal use only
	 */

	public void downloadHeads() {
		setDownloading(true);
		Common.runAsync(() -> {
			final List<Skull> heads = new ArrayList<>();

			Common.broadcast("&r&aBeginning initial download, it may take some time to insert all the skulls into the data file!");

			for (BaseCategory value : BaseCategory.values()) {
				heads.addAll(downloadHeadCategory(value));
			}

			this.skulls.addAll(heads);
			Skulls.getDataManager().insertSkulls(heads);
		});
	}

	public List<History> downloadHistories() {
		final List<History> histories = new ArrayList<>();
		try {
			final JsonArray json = getJsonFromUrl("https://rose.tweetzy.ca/minecraft/skulls/inserts");
			json.forEach(jsonElement -> {
				final JsonObject jsonObject = jsonElement.getAsJsonObject();
				histories.add(new InsertHistory(
						Integer.parseInt(replace(jsonObject.get("id").toString())),
						Instant.parse(replace(jsonObject.get("date").toString())).toEpochMilli(),
						Arrays.stream(replace(jsonObject.get("heads").toString()).split(",")).map(Integer::parseInt).collect(Collectors.toList())
				));
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return histories;
	}

	private List<Skull> downloadHeadCategory(@NonNull final BaseCategory category) {
		final List<Skull> heads = new ArrayList<>();
		try {
			long start = System.currentTimeMillis();
			final JsonArray json = getJsonFromUrl(String.format("https://rose.tweetzy.ca/minecraft/skulls?category=%s", category.name().replace("AND", "&").toUpperCase().replace("_", "%20")));
			json.forEach(jsonElement -> {
				final JsonObject jsonObject = jsonElement.getAsJsonObject();
				heads.add(new TexturedSkull(
						Integer.parseInt(replace(jsonObject.get("id").toString())),
						replace(jsonObject.get("name").toString()),
						replace(jsonObject.get("category").toString()),
						Arrays.asList(replace(jsonObject.get("tags").toString()).split(",")),
						replace(jsonObject.get("texture").toString()),
						category.getDefaultPrice(),
						false
				));
			});

			Common.broadcast("&aDownloaded &e" + heads.size() + " &askulls for category &e" + category.getName() + "&a in &f" + (System.currentTimeMillis() / start) / 1000 + "&ems");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return heads;
	}

	private JsonArray getJsonFromUrl(final String url) throws IOException {
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

	private String replace(String in) {
		return in.replace("\"", "");
	}

	@Override
	public void load() {
		setLoading(true);
		long start = System.currentTimeMillis();

		Skulls.getDataManager().getSkulls((error, all) -> {
			if (error != null) {
				error.printStackTrace();
				return;
			}

			this.skulls.addAll(all);

			if (this.skulls.isEmpty()) {
				Common.log("&cCould not find any skulls, attempting to redownload them!");
				downloadHeads();
			} else {
				long ms = (System.currentTimeMillis() - start) / 1000;
				Common.log("&aLoaded &e" + this.skulls.size() + " &askulls in &f" + ms + "&ams");
			}
		});

		Skulls.getDataManager().getHistories((error, all) -> {
			if (error != null) {
				error.printStackTrace();
				return;
			}

			this.histories.addAll(all);

			if (this.histories.isEmpty()) {
				Common.log("&cCould not find any inserts, attempting to redownload them!");
				Common.runAsync(() -> {
					final List<History> dlHistory = downloadHistories();
					Skulls.getDataManager().insertHistories(dlHistory);
					this.histories.addAll(dlHistory);
				});
			} else {

				Common.runAsync(() -> {
					// check for new inserts
					final List<History> dlHistory = downloadHistories();

					if (dlHistory.size() != this.histories.size()) {
						List<History> toInsert = new ArrayList<>();
						dlHistory.forEach(downloadedHistory -> {
							if (this.histories.stream().noneMatch(history -> history.getID() == downloadedHistory.getID())) {
								toInsert.add(downloadedHistory);
							}
						});


						Skulls.getDataManager().insertHistories(toInsert);
						Common.log("&aInserts were found, saving the following: &e" + toInsert.stream().map(h -> String.valueOf(h.getID())).collect(Collectors.joining(", ")));
						return;
					}

					Common.log("&aLoaded &e" + this.histories.size() + "&a history inserts");
				});
			}
		});
	}
}
