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
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.profiles.builder.XSkull;
import ca.tweetzy.flight.utils.profiles.objects.ProfileInputType;
import ca.tweetzy.flight.utils.profiles.objects.Profileable;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.SkullUser;
import ca.tweetzy.skulls.guis.abstraction.SkullsPagedGUI;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public final class PlayerHeadGUI extends SkullsPagedGUI<OfflinePlayer> {

	private final SkullUser player;

	public PlayerHeadGUI(final Gui parent, @NonNull final SkullUser player) {
		super(parent, Bukkit.getPlayer(player.getUUID()), TranslationManager.string(Translations.GUI_PLAYER_HEADS_TITLE), 6, new ArrayList<>());
		setAsync(true);
		this.player = player;
		draw();
	}

	@Override
	protected void prePopulate() {
		this.items.addAll(Skulls.getSkullManager().getOnlineOfflinePlayers());
	}

	@Override
	protected void drawFixed() {
		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(OfflinePlayer target) {
		if (target == null || !target.hasPlayedBefore()) {
			return QuickItem.of("http://textures.minecraft.net/texture/ee7700096b5a2a87386d6205b4ddcc14fd33cf269362fa6893499431ce77bf9").name("&eUnknown Player").make();
		}

		QuickItem item = QuickItem.of(CompMaterial.PLAYER_HEAD).name(TranslationManager.string(Translations.GUI_PLAYER_HEADS_ITEMS_HEAD_NAME, "player_name", target.getName()));

		if (Settings.CHARGE_FOR_HEADS.getBoolean() && Settings.DEFAULT_PRICES_PLAYER_HEADS.getDouble() > 0) {
			item.lore(TranslationManager.string(Translations.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_PRICE, "skull_price", String.format("%,.2f", Settings.DEFAULT_PRICES_PLAYER_HEADS.getDouble())));
		}

		item.lore(TranslationManager.string(Translations.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAKE));

		return XSkull
				.of(item.make())
				.profile(Profileable.of(target))
				.fallback(Profileable.of(
						ProfileInputType.TEXTURE_URL,
						"http://textures.minecraft.net/texture/ee7700096b5a2a87386d6205b4ddcc14fd33cf269362fa6893499431ce77bf9"
				))
				.lenient()
				.apply();
	}

	@Override
	protected void onClick(OfflinePlayer target, GuiClickEvent click) {
		final Player player = click.player;

		if (click.clickType == ClickType.LEFT) {
			if (!Settings.CHARGE_FOR_HEADS.getBoolean()) {
				player.getInventory().addItem(buildHead(target));
				return;
			}

			final double price = player.hasPermission("skulls.freeskulls") ? 0 : Settings.DEFAULT_PRICES_PLAYER_HEADS.getDouble();

			if (price <= 0) {
				player.getInventory().addItem(buildHead(target));
				return;
			}

			if (!Skulls.getEconomyManager().has(player, price)) {
				Common.tell(player, TranslationManager.string(Translations.NO_MONEY));
				return;
			}

			Skulls.getEconomyManager().withdraw(player, price);
			player.getInventory().addItem(buildHead(target));
		}
	}

	private ItemStack buildHead(@NonNull final OfflinePlayer target) {
		return QuickItem.of(target).name(TranslationManager.string(Translations.PLAYER_HEAD, "player_name", target.getName())).make();
	}
}
