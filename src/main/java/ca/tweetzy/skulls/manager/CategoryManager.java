package ca.tweetzy.skulls.manager;

import ca.tweetzy.skulls.api.interfaces.Category;
import lombok.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date Created: April 04 2022
 * Time Created: 8:49 p.m.
 *
 * @author Kiran Hart
 */
public final class CategoryManager implements Manager {

	private final Map<String, Category> categories = new ConcurrentHashMap<>();

	public void addCategory(@NonNull final Category category) {
		this.categories.put(category.getId(), category);
	}

	public void removeCategory(@NonNull final String id) {
		this.categories.remove(id);
	}

	public Category findCategory(@NonNull final String id) {
		return this.categories.getOrDefault(id, null);
	}

	@Override
	public void load() {
		this.categories.clear();

	}
}
