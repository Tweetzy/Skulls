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
import ca.tweetzy.flight.gui.template.BaseGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.guis.abstraction.SkullsBaseGUI;
import ca.tweetzy.skulls.settings.Translations;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Date Created: April 29 2022
 * Time Created: 10:59 a.m.
 *
 * @author Kiran Hart
 */
public final class SkullEditGUI extends SkullsBaseGUI {

	private final Gui parent;
	private final Skull skull;
	private final int page;

	public SkullEditGUI(final Gui parent, Skull skull, int page) {
		super(parent, TranslationManager.string(Translations.GUI_EDIT_TITLE, "skull_id", skull.getId()), 6);
		this.parent = parent;
		this.skull = skull;
		this.page = page;
		draw();
	}

	@Override
	protected void draw() {
		setItem(1, 4, this.skull.getItemStack());

		setButton(3, 1, QuickItem.of(CompMaterial.NAME_TAG)
				.name(TranslationManager.string(Translations.GUI_EDIT_ITEMS_NAME_NAME))
				.lore(TranslationManager.list(Translations.GUI_EDIT_ITEMS_NAME_LORE))
				.make(), click -> new TitleInput(Skulls.getInstance(), click.player, TranslationManager.string(Translations.INPUT_SKULL_EDIT_TITLE), TranslationManager.string(Translations.INPUT_SKULL_EDIT_NAME)) {

			@Override
			public boolean onResult(String string) {
				if (string.isEmpty()) return false;
				SkullEditGUI.this.skull.setName(string);
				SkullEditGUI.this.skull.sync();
				click.manager.showGUI(click.player, new SkullEditGUI(SkullEditGUI.this.parent, SkullEditGUI.this.skull, SkullEditGUI.this.page));
				return true;
			}
		});

		setButton(3, 3, QuickItem.of(CompMaterial.GOLD_INGOT)
				.name(TranslationManager.string(Translations.GUI_EDIT_ITEMS_PRICE_NAME))
				.lore(TranslationManager.list(Translations.GUI_EDIT_ITEMS_PRICE_LORE))
				.make(), click -> new TitleInput(Skulls.getInstance(), click.player, TranslationManager.string(Translations.INPUT_SKULL_EDIT_TITLE), TranslationManager.string(Translations.INPUT_SKULL_EDIT_PRICE)) {

			@Override
			public boolean onResult(String string) {
				if (string.isEmpty()) return false;
				if (!NumberUtils.isNumber(string.trim())) {
					Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", string));
					return false;
				}

				SkullEditGUI.this.skull.setPrice(Double.parseDouble(string.trim()));
				SkullEditGUI.this.skull.sync();
				click.manager.showGUI(click.player, new SkullEditGUI(SkullEditGUI.this.parent, SkullEditGUI.this.skull, SkullEditGUI.this.page));
				return true;
			}
		});

		setButton(3, 5, QuickItem.of(CompMaterial.BOOKSHELF)
				.name(TranslationManager.string(Translations.GUI_EDIT_ITEMS_ADD_CATEGORY_NAME))
				.lore(TranslationManager.list(Translations.GUI_EDIT_ITEMS_ADD_CATEGORY_LORE))
				.make(), click -> click.manager.showGUI(click.player, new CategorySelectorGUI(selected -> {

			if (!selected.getSkulls().contains(skull.getId())) {
				selected.getSkulls().add(skull.getId());
				selected.sync();
			}

			click.manager.showGUI(click.player, this);
		})));

		setButton(3, 7, QuickItem.of(this.skull.isBlocked() ? CompMaterial.RED_STAINED_GLASS_PANE : CompMaterial.LIME_STAINED_GLASS_PANE)
				.name(TranslationManager.string(Translations.GUI_EDIT_ITEMS_BLOCKED_NAME))
				.lore(TranslationManager.list(Translations.GUI_EDIT_ITEMS_BLOCKED_LORE, "is_true", (this.skull.isBlocked() ? TranslationManager.string(Translations.MISC_IS_TRUE) : TranslationManager.string(Translations.MISC_IS_FALSE))))
				.make(), click -> {


			this.skull.setBlocked(!SkullEditGUI.this.skull.isBlocked());
			this.skull.sync();
			click.manager.showGUI(click.player, new SkullEditGUI(this.parent, this.skull, this.page));
		});

		applyBackExit();
	}
}
