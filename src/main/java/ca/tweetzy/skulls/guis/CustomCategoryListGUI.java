package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.comp.enums.CompMaterial;
import ca.tweetzy.rose.gui.Gui;
import ca.tweetzy.rose.gui.events.GuiClickEvent;
import ca.tweetzy.rose.gui.template.PagedGUI;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.rose.utils.input.TitleInput;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.api.interfaces.Category;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.settings.Translation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * Date Created: May 02 2022
 * Time Created: 1:19 p.m.
 *
 * @author Kiran Hart
 */
public final class CustomCategoryListGUI extends PagedGUI<Category> {

	public CustomCategoryListGUI(Gui parent) {
		super(parent, Translation.GUI_CUSTOM_CATEGORY_LIST_TITLE.getString(), 6, Skulls.getCategoryManager().getCustomCategories());
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Category category) {
		return QuickItem.of(CompMaterial.WRITTEN_BOOK)
				.name(Translation.GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_NAME.getString("category_name", category.getName()))
				.lore(Translation.GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_LORE.getList("category_size", category.getSkulls().size()))
				.hideTags(true)
				.make();
	}

	@Override
	protected void drawAdditional() {
		setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL)
				.name(Translation.GUI_CUSTOM_CATEGORY_LIST_ITEMS_NEW_NAME.getString())
				.lore(Translation.GUI_CUSTOM_CATEGORY_LIST_ITEMS_NEW_LORE.getList())
				.make(), click -> new TitleInput(click.player, Translation.INPUT_CATEGORY_CREATE_TITLE.getString(), Translation.INPUT_CATEGORY_CREATE_SUBTITLE.getString()) {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(player, CustomCategoryListGUI.this);
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string.trim());

				if (Skulls.getCategoryManager().findCategory(string) != null)  {
					Common.tell(click.player, Translation.ID_TAKEN.getString());
					return false;
				}

				final Category toCreate = new SkullCategory(string.toLowerCase(), string, true, Collections.emptyList());

				Skulls.getDataManager().insertCategory(toCreate, (error, created) -> {
					if (error != null) return;
					Skulls.getCategoryManager().addCategory(created);

					click.manager.showGUI(click.player, new CustomCategoryListGUI(new MainGUI(click.player)));
				});

				return true;
			}
		});
	}

	@Override
	protected void onClick(Category category, GuiClickEvent clickEvent) {
		clickEvent.manager.showGUI(clickEvent.player, new SkullsViewGUI(this, Skulls.getPlayerManager().findPlayer(clickEvent.player), category.getId(), ViewMode.LIST));
	}
}
