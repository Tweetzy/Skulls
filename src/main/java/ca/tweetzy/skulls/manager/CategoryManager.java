package ca.tweetzy.skulls.manager;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.Category;
import ca.tweetzy.skulls.impl.SkullCategory;
import lombok.NonNull;
import org.bukkit.entity.Cat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

	public List<Category> getCustomCategories() {
		return this.categories.values().stream().filter(Category::isCustom).collect(Collectors.toList());
	}

	@Override
	public void load() {
		this.categories.clear();

		// load default categories
		for (BaseCategory value : BaseCategory.values()) {
			this.categories.put(value.getId(), new SkullCategory(value.getId(), value.getName(), false, Collections.emptyList()));
		}

		Skulls.getDataManager().getCategories((ex, result) -> {
			if (ex == null)
				result.forEach(this::addCategory);
		});
	}
}
