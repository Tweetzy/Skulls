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
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.interfaces.History;
import ca.tweetzy.skulls.guis.abstraction.SkullsPagedGUI;
import ca.tweetzy.skulls.settings.Translations;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date Created: May 03 2022
 * Time Created: 12:51 p.m.
 *
 * @author Kiran Hart
 */
public final class HistoryViewGUI extends SkullsPagedGUI<History> {

	public HistoryViewGUI(Gui parent) {
		super(parent, TranslationManager.string(Translations.GUI_HISTORIES_TITLE), 6, Skulls.getSkullManager().getHistories());
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(History history) {
		final boolean downloaded = Skulls.getSkullManager().getIdList().contains(history.getSkulls().get(0));

		final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
		final Date resultDate = new Date(history.getTime());

		return QuickItem
				.of(downloaded ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name(TranslationManager.string(Translations.GUI_HISTORIES_ITEMS_HISTORY_NAME, "history_id", history.getID()))
				.lore(TranslationManager.list(Translations.GUI_HISTORIES_ITEMS_HISTORY_LORE,
						"history_size", history.getSkulls().size(),
						"is_true", TranslationManager.string(downloaded ? Translations.MISC_IS_TRUE : Translations.MISC_IS_FALSE),
						"history_time", sdf.format(resultDate)
				))
				.make();
	}

	@Override
	protected void drawAdditional() {
		setButton(5, 8, QuickItem.of(CompMaterial.GOLD_NUGGET).name("&e&lForce Sync Prices").lore("&7Clicking this will force update all the prices", "&7for all skulls to the default category price", "&7set within the configuration file.").make(), click -> {
//			Skulls.getDataManager().syncSkullPricesByCategory(null);

			AtomicInteger total = new AtomicInteger(0);

			Skulls.getSkullManager().getSkulls().forEach(skull -> {
				final BaseCategory category = BaseCategory.getById(skull.getCategory());
				if (skull.getPrice() != category.getDefaultPrice()) {
					skull.setPrice(category.getDefaultPrice());
					skull.sync();
					total.incrementAndGet();
				}
			});

			Common.tell(click.player, "&aUpdated a total of &e" + total.get() + " &askulls");
		});
	}

	@Override
	protected void onClick(History history, GuiClickEvent clickEvent) {
		final boolean downloaded = Skulls.getSkullManager().getIdList().contains(history.getSkulls().get(0));
		if (downloaded) return;

		Bukkit.getServer().getScheduler().runTaskAsynchronously(Skulls.getInstance(), () -> {
			Skulls.getSkullManager().downloadHistorySkulls(history, finished -> clickEvent.manager.showGUI(clickEvent.player, new HistoryViewGUI(new MainGUI(clickEvent.player))));
		});
	}
}
