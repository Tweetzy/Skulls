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
