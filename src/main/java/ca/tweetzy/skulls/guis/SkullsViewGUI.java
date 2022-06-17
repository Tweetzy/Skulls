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

import ca.tweetzy.rose.gui.Gui;
import ca.tweetzy.rose.gui.events.GuiClickEvent;
import ca.tweetzy.rose.gui.template.PagedGUI;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.skulls.settings.Locale;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Date Created: April 21 2022
 * Time Created: 11:59 a.m.
 *
 * @author Kiran Hart
 */
public final class SkullsViewGUI extends PagedGUI<Skull> {

	private final Player player;
	private final SkullUser skullPlayer;
	private final ViewMode viewMode;
	private final String category;

	public SkullsViewGUI(final Gui parent, final SkullUser skullPlayer, final String category, final ViewMode viewMode) {
		super(
				parent,
				viewMode == ViewMode.SEARCH ? Translation.GUI_SKULLS_LIST_TITLE_SEARCH.getString("search_phrase", category) : viewMode == ViewMode.FAVOURITE ? Translation.GUI_SKULLS_LIST_TITLE_FAVOURITES.getString() : Translation.GUI_SKULLS_LIST_TITLE_CATEGORY.getString("category_name", category),
				6,
				viewMode == ViewMode.SEARCH ? Skulls.getSkullManager().getSkullsBySearch(category) : viewMode == ViewMode.FAVOURITE ? Skulls.getSkullManager().getSkulls(skullPlayer.getFavourites()) : Skulls.getSkullManager().getSkulls(category)
		);

		this.category = category;
		this.viewMode = viewMode;
		this.skullPlayer = skullPlayer;
		this.player = Bukkit.getPlayer(this.skullPlayer.getUUID());
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Skull skull) {
		final QuickItem item = QuickItem.of(skull.getItemStack()).name(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_NAME.getString("skull_name", skull.getName()));

		item.lore(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_ID.getString("skull_id", skull.getId()));
		item.lore(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAGS.getString("skull_tags", String.join(", ", skull.getTags())));

		if (Settings.CHARGE_FOR_HEADS.getBoolean() && skull.getPrice() > 0) {
			item.lore(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_PRICE.getString("skull_price", String.format("%,.2f", skull.getPrice())));
		}

		if (!skull.isBlocked() && this.skullPlayer.getFavourites().contains(skull.getId())) {
			item.lore(" ");
			item.lore(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_FAVOURITED.getString());
			item.lore(" ");
		} else {
			item.lore(" ");
		}

		item.lore(skull.isBlocked() ? Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_BLOCKED.getString() : Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAKE.getString());

		if (!skull.isBlocked()) {
			item.lore(this.skullPlayer.getFavourites().contains(skull.getId()) ? Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_UN_FAVOURITE.getString() : Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_FAVOURITE.getString());
		}

		if (this.player.hasPermission("skulls.admin"))
			item.lore(" ", Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_EDIT.getString());

		return item.make();
	}

	@Override
	protected void onClick(Skull skull, GuiClickEvent event) {
		final Player player = event.player;

		if (event.clickType == ClickType.LEFT) {
			if (skull.isBlocked()) {
				if (!player.isOp() || !player.hasPermission("skulls.buyblocked")) return;
			}

			if (!Settings.CHARGE_FOR_HEADS.getBoolean()) {
				player.getInventory().addItem(skull.getItemStack());
				return;
			}

			final double price = player.hasPermission("skulls.freeskulls") ? 0 : skull.getPrice();

			if (price <= 0) {
				player.getInventory().addItem(skull.getItemStack());
				return;
			}

			if (!Skulls.getEconomyManager().has(player, price)) {
				Locale.tell(player, Translation.NOT_ENOUGH_MONEY.getKey());
				return;
			}

			Skulls.getEconomyManager().withdraw(player, price);
			player.getInventory().addItem(skull.getItemStack());
		}

		if (event.clickType == ClickType.RIGHT && player.hasPermission("skulls.favourite")) {
			this.skullPlayer.toggleFavourite(skull.getId());
			this.skullPlayer.sync();

			if (this.viewMode == ViewMode.FAVOURITE)
				event.manager.showGUI(event.player, new SkullsViewGUI(this.parent, this.skullPlayer, "", ViewMode.FAVOURITE));
			else
				draw();
		}

		if (event.clickType == ClickType.NUMBER_KEY && player.hasPermission("skulls.admin")) {
			event.manager.showGUI(player, new SkullEditGUI(this, skull, this.page));
		}
	}

	@Override
	protected void handleTitle() {
//		setTitle(Replacer.replaceVariables(getTitle(this.viewMode, this.category), "current_page", this.page, "total_pages", this.pages));
	}

	private String getTitle(ViewMode viewMode, String category) {
		return viewMode == ViewMode.SEARCH ?
				Translation.GUI_SKULLS_LIST_TITLE_SEARCH.getString("search_phrase", category)
				: viewMode == ViewMode.FAVOURITE ? Translation.GUI_SKULLS_LIST_TITLE_FAVOURITES.getString()
				: Translation.GUI_SKULLS_LIST_TITLE_CATEGORY.getString("category_name", category);
	}
}
