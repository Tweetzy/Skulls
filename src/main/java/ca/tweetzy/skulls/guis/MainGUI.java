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
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.guis.abstraction.SkullsBaseGUI;
import ca.tweetzy.skulls.model.SkullItem;
import ca.tweetzy.skulls.model.StringHelper;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Date Created: April 20 2022
 * Time Created: 9:47 p.m.
 *
 * @author Kiran Hart
 */
public final class MainGUI extends SkullsBaseGUI {

	private final Player player;

	public MainGUI(@NonNull final Player player) {
		super(null, player, TranslationManager.string(Translations.GUI_MAIN_TITLE), 6);
		this.player = player;
		draw();
	}

	@Override
	protected void draw() {

		for (BaseCategory baseCategory : BaseCategory.values()) {
			if (!baseCategory.isEnabled()) continue;
			setButton(baseCategory.getSlot(), QuickItem
					.of(Skulls.getSkullManager().getSkullItem(baseCategory.getTexture()))
					.name(TranslationManager.string(Translations.GUI_MAIN_ITEMS_CATEGORY_NAME, "category_name", baseCategory.getName()))
					.lore(TranslationManager.list(Translations.GUI_MAIN_ITEMS_CATEGORY_LORE, "category_size", Skulls.getSkullManager().getSkullCount(baseCategory.getId())))
					.make(), click -> {

				if (!Settings.GENERAL_USAGE_REQUIRES_NO_PERM.getBoolean())
					if (!click.player.hasPermission("skulls.category." + baseCategory.getId().toLowerCase().replace(" ", "").replace("&", ""))) {
						Common.tell(click.player, TranslationManager.string(Translations.CATEGORY_PERMISSION));
						return;
					}

				click.manager.showGUI(click.player, new SkullsViewGUI(this, Skulls.getPlayerManager().findOrCreate(click.player), baseCategory.getId(), ViewMode.LIST));
			});
		}

		setButton(Settings.GUI_MAIN_ITEMS_SEARCH_SLOT.getInt(), QuickItem.of(SkullItem.get("skulls:5650"))
				.name(TranslationManager.string(Translations.GUI_MAIN_ITEMS_SEARCH_NAME))
				.lore(TranslationManager.list(Translations.GUI_MAIN_ITEMS_SEARCH_LORE))
				.make(), click -> {

			if (!Settings.GENERAL_USAGE_REQUIRES_NO_PERM.getBoolean())
				if (!click.player.hasPermission("skulls.search")) {
					Common.tell(click.player, TranslationManager.string(Translations.NO_PERMISSION));
					return;
				}

			new TitleInput(Skulls.getInstance(), click.player, TranslationManager.string(Translations.INPUT_SKULL_SEARCH_TITLE), TranslationManager.string(Translations.INPUT_SKULL_SEARCH_SUBTITLE)) {

				@Override
				public boolean onResult(String string) {
					string = StringHelper.escapeRegex(string).trim();
					Skulls.getGuiManager().showGUI(click.player, new SkullsViewGUI(MainGUI.this, Skulls.getPlayerManager().findOrCreate(click.player), string, ViewMode.SEARCH));
					return true;
				}
			};

		});

		setButton(Settings.GUI_MAIN_ITEMS_CUSTOM_CATEGORIES_SLOT.getInt(), QuickItem.of(SkullItem.get("skulls:25001"))
				.name(TranslationManager.string(Translations.GUI_MAIN_ITEMS_CUSTOM_CATEGORIES_NAME))
				.lore(TranslationManager.list(Translations.GUI_MAIN_ITEMS_CUSTOM_CATEGORIES_LORE))
				.make(), click -> click.manager.showGUI(click.player, new CustomCategoryListGUI(click.player, this)));

		if (Settings.CATEGORIES_PLAYER_HEADS_ENABLED.getBoolean())
			setButton(Settings.GUI_MAIN_ITEMS_PLAYER_HEADS_SLOT.getInt(), QuickItem.of(this.player)
					.name(TranslationManager.string(Translations.GUI_MAIN_ITEMS_PLAYERS_NAME))
					.lore(TranslationManager.list(Translations.GUI_MAIN_ITEMS_PLAYERS_LORE, "category_size", Skulls.getSkullManager().getOnlineOfflinePlayers().size()))
					.make(), click -> {

				if (!Settings.GENERAL_USAGE_REQUIRES_NO_PERM.getBoolean())
					if (!click.player.hasPermission("skulls.category.playerheads")) {
						Common.tell(click.player, TranslationManager.string(Translations.NO_PERMISSION));
						return;
					}

				click.manager.showGUI(click.player, new PlayerHeadGUI(this, Skulls.getPlayerManager().findOrCreate(click.player)));
			});

		setButton(Settings.GUI_MAIN_ITEMS_FAVOURITES_SLOT.getInt(), QuickItem.of(SkullItem.get("skulls:39696"))
				.name(TranslationManager.string(Translations.GUI_MAIN_ITEMS_FAVOURITES_NAME))
				.lore(TranslationManager.list(Translations.GUI_MAIN_ITEMS_FAVOURITES_LORE))
				.make(), click -> {

			if (!Settings.GENERAL_USAGE_REQUIRES_NO_PERM.getBoolean())
				if (!click.player.hasPermission("skulls.favourites")) {
					Common.tell(click.player, TranslationManager.string(Translations.NO_PERMISSION));
					return;
				}

			click.manager.showGUI(click.player, new SkullsViewGUI(this, Skulls.getPlayerManager().findOrCreate(click.player), "", ViewMode.FAVOURITE));
		});


		if (this.player.hasPermission("skulls.admin") || this.player.isOp()) {
			setButton(5, 0, QuickItem.of(CompMaterial.REPEATER)
					.name("&e&lSettings")
					.lore(
							"&8Skull Settings",
							"&7A few quick setting options for skulls",
							"",
							"&e&lClick &8» &7To view settings"
					)
					.make(), click -> click.manager.showGUI(click.player, new SettingsGUI(this, click.player)));

			setButton(5, 8, QuickItem.of(CompMaterial.DIAMOND)
					.name("&e&lPatreon")
					.lore(
							"&8Support me on Patreon",
							"&7By supporting me on Patreon you will",
							"&7be helping me be able to continue updating",
							"&7and creating free plugins.",
							"",
							"&8&oDon't worry, normal players can't see this",
							"",
							"&e&lClick &8» &7To view Patreon"
					)
					.glow(true)
					.make(), click -> {

				click.gui.close();
				Common.tellNoPrefix(click.player,
						"&8&m-----------------------------------------------------",
						"",
						ChatUtil.centerMessage("&E&lTweetzy Patreon"),
						ChatUtil.centerMessage("&bhttps://patreon.com/kiranhart"),
						"&8&m-----------------------------------------------------"
				);
			});
		}
	}
}
