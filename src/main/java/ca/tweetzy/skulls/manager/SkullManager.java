package ca.tweetzy.skulls.manager;

import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.History;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.impl.InsertHistory;
import ca.tweetzy.skulls.impl.TexturedSkull;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.embed.HostDragStartListener;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
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

	/**
	 * For internal use only
	 */

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
