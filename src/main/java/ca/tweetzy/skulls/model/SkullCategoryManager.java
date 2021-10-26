package ca.tweetzy.skulls.model;


import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.SerializeUtil;
import ca.tweetzy.tweety.collection.StrictMap;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 10:13 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class SkullCategoryManager {

	@Getter
	private final StrictMap<String, SkullCategory> categories = new StrictMap<>();

	public void addCategory(@NonNull final SkullCategory category) {
		this.categories.put(category.getId(), category);
	}

	public void removeCategory(@NonNull final SkullCategory category) {
		this.categories.remove(category.getId());
		Skulls.getInstance().getDataFile().setField("Categories." + category.getId().toLowerCase(), null);
	}

	public SkullCategory getCategory(@NonNull final String id) {
		return this.categories.getOrDefault(id, null);
	}

	public List<SkullCategory> getCustomCategories() {
		final List<SkullCategory> skullCategories = new ArrayList<>();
		this.categories.forEachIterate((id, cate) -> {
			if (cate.isCustom())
				skullCategories.add(cate);
		});
		return skullCategories;
	}

	public void addSkull(@NonNull final SkullCategory category, final int id) {
		if (!category.isCustom()) return;
		category.getSkulls().addIfNotExist(id);
	}

	public void removeSkull(@NonNull final SkullCategory category, final int id) {
		if (!category.isCustom()) return;
		category.getSkulls().remove(Integer.valueOf(id));
	}

	public void saveCategory(@NonNull final SkullCategory skullCategory) {
		Skulls.getInstance().getDataFile().setField("Categories." + skullCategory.getId().toLowerCase(), SerializeUtil.serialize(skullCategory));
	}

	public void saveCategories() {
		getCustomCategories().forEach(this::saveCategory);
	}

	public void loadCustomCategories() {
		Common.runAsync(() -> {
			if (!Skulls.getInstance().getDataFile().contains("Categories")) {
				return;
			}

			final ConfigurationSection section = Skulls.getInstance().getDataFile().getConfigField("Categories");
			if (section == null && section.getKeys(false).size() == 0) {
				return;
			}

			section.getKeys(false).forEach(categoryId -> {
				final SkullCategory skullCategory = SerializeUtil.deserialize(SkullCategory.class, Skulls.getInstance().getDataFile().getConfigField("Categories." + categoryId.toLowerCase()));
				this.categories.put(skullCategory.getId(), skullCategory);
			});
		});
	}
}
