/*
 * Skulls
 * Copyright 2023 Kiran Hart
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

package ca.tweetzy.skulls.guis.abstraction;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.template.PagedGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.skulls.settings.Translations;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class SkullsPagedGUI<T> extends PagedGUI<T> {

	public SkullsPagedGUI(Gui parent, @NonNull String title, int rows, @NonNull List<T> items) {
		super(parent, title, rows, items);
	}

	public SkullsPagedGUI(@NonNull String title, int rows, @NonNull List<T> items) {
		super(title, rows, items);
	}

	@Override
	protected ItemStack getBackButton() {
		return QuickItem.of(CompMaterial.DARK_OAK_DOOR).name(TranslationManager.string(Translations.GUI_BTN_BACK_NAME)).lore(TranslationManager.list(Translations.GUI_BTN_BACK_LORE)).make();
	}

	@Override
	protected ItemStack getExitButton() {
		return QuickItem.of(CompMaterial.BARRIER).name(TranslationManager.string(Translations.GUI_BTN_EXIT_NAME)).lore(TranslationManager.list(Translations.GUI_BTN_EXIT_LORE)).make();
	}

	@Override
	protected ItemStack getPreviousButton() {
		return QuickItem.of(CompMaterial.ARROW).name(TranslationManager.string(Translations.GUI_BTN_PREV_NAME)).lore(TranslationManager.list(Translations.GUI_BTN_PREV_LORE)).make();
	}

	@Override
	protected ItemStack getNextButton() {
		return QuickItem.of(CompMaterial.ARROW).name(TranslationManager.string(Translations.GUI_BTN_NEXT_NAME)).lore(TranslationManager.list(Translations.GUI_BTN_NEXT_LORE)).make();
	}

	@Override
	protected int getPreviousButtonSlot() {
		return 48;
	}

	@Override
	protected int getNextButtonSlot() {
		return 50;
	}
}
