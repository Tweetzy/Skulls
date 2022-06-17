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

package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.comp.enums.CompMaterial;
import ca.tweetzy.rose.gui.events.GuiClickEvent;
import ca.tweetzy.rose.gui.template.PagedGUI;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Category;
import ca.tweetzy.skulls.settings.Translation;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Date Created: May 02 2022
 * Time Created: 1:49 p.m.
 *
 * @author Kiran Hart
 */
public final class CategorySelectorGUI extends PagedGUI<Category> {

	private final Consumer<Category> selected;

	public CategorySelectorGUI(Consumer<Category> selected) {
		super(null, Translation.GUI_CUSTOM_CATEGORY_SELECTOR_TITLE.getString(), 6, Skulls.getCategoryManager().getCustomCategories());
		this.selected = selected;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Category category) {
		return QuickItem.of(CompMaterial.WRITTEN_BOOK)
				.lore(Translation.GUI_CUSTOM_CATEGORY_SELECTOR_ITEMS_CATEGORY_LORE.getList("category_size", category.getSkulls().size()))
				.name(Translation.GUI_CUSTOM_CATEGORY_SELECTOR_ITEMS_CATEGORY_NAME.getString("category_name", category.getName()))
				.hideTags(true)
				.make();
	}

	@Override
	protected void onClick(Category category, GuiClickEvent clickEvent) {
		this.selected.accept(category);
	}
}
