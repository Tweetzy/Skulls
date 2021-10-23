package ca.tweetzy.skulls.model;


import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.tweety.collection.StrictMap;
import lombok.Getter;
import lombok.NonNull;

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
	}

	public SkullCategory getCategory(@NonNull final String id) {
		return this.categories.getOrDefault(id, null);
	}
}
