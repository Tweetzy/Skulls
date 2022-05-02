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
