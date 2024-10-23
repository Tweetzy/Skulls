/*
 * Skulls
 * Copyright 2024 Kiran Hart
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
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.guis.abstraction.SkullsBaseGUI;
import ca.tweetzy.skulls.settings.Translations;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public final class SettingsGUI extends SkullsBaseGUI {

	public SettingsGUI(Gui parent, @NonNull Player player) {
		super(parent, player, TranslationManager.string(Translations.GUI_SETTINGS_TITLE), 4);
		draw();
	}

	@Override
	protected void draw() {
		applyBackExit();

		setButton(1, 2, QuickItem.of(CompMaterial.GOLD_NUGGET).name("&e&lForce Sync Prices").lore("&7Clicking this will force update all the prices", "&7for all skulls to the default category price", "&7set within the configuration file.").make(), click -> {
			AtomicInteger total = new AtomicInteger(0);

			Skulls.getSkullManager().getSkulls().values().forEach(skull -> {
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
}
