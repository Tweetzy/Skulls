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

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.template.PagedGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.api.interfaces.Category;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
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

	private final Player viewer;

	public CustomCategoryListGUI(Player viewer, Gui parent) {
		super(parent, TranslationManager.string(Translations.GUI_CUSTOM_CATEGORY_LIST_TITLE), 6, Skulls.getCategoryManager().getCustomCategories());
		this.viewer = viewer;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Category category) {
		return QuickItem.of(CompMaterial.WRITTEN_BOOK)
				.name(TranslationManager.string(Translations.GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_NAME, "category_name", category.getName()))
				.lore(TranslationManager.list(Translations.GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_LORE, "category_size", category.getSkulls().size()))
				.hideTags(true)
				.make();
	}

	@Override
	protected void drawAdditional() {
		if (this.viewer.hasPermission("skulls.admin"))
			setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL)
					.name(TranslationManager.string(Translations.GUI_CUSTOM_CATEGORY_LIST_ITEMS_NEW_NAME))
					.lore(TranslationManager.list(Translations.GUI_CUSTOM_CATEGORY_LIST_ITEMS_NEW_LORE))
					.make(), click -> new TitleInput(Skulls.getInstance(), click.player, TranslationManager.string(Translations.INPUT_CATEGORY_CREATE_TITLE), TranslationManager.string(Translations.INPUT_CATEGORY_CREATE_SUBTITLE)) {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(player, CustomCategoryListGUI.this);
				}

				@Override
				public boolean onResult(String string) {
					string = ChatColor.stripColor(string.trim());

					if (Skulls.getCategoryManager().findCategory(string) != null) {
						Common.tell(click.player, TranslationManager.string(Translations.ID_TAKEN));
						return false;
					}

					final Category toCreate = new SkullCategory(string.toLowerCase(), string, true, Collections.emptyList());

					Skulls.getDataManager().insertCategory(toCreate, (error, created) -> {
						if (error != null) return;
						Skulls.getCategoryManager().addCategory(created);

						click.manager.showGUI(click.player, new CustomCategoryListGUI(click.player, new MainGUI(click.player)));
					});

					return true;
				}
			});
	}

	@Override
	protected void onClick(Category category, GuiClickEvent clickEvent) {
		if (!Settings.GENERAL_USAGE_REQUIRES_NO_PERM.getBoolean())
			if (!clickEvent.player.hasPermission("skulls.customcategory." + category.getId().toLowerCase())) {
				Common.tell(clickEvent.player, TranslationManager.string(Translations.CATEGORY_PERMISSION));
				return;
			}

		clickEvent.manager.showGUI(clickEvent.player, new SkullsViewGUI(this, Skulls.getPlayerManager().findPlayer(clickEvent.player), category.getId(), ViewMode.LIST));
	}
}
