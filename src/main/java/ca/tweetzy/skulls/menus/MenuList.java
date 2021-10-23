package ca.tweetzy.skulls.menus;

import ca.tweetzy.skulls.RowByContentSize;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.api.enums.SkullsMenuListingType;
import ca.tweetzy.skulls.impl.Skull;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.model.EconomyManager;
import ca.tweetzy.skulls.model.Permissions;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.MathUtil;
import ca.tweetzy.tweety.PlayerUtil;
import ca.tweetzy.tweety.Valid;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 19 2021
 * Time Created: 4:18 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuList extends MenuPagged<Skull> {

	private final boolean fromMain;
	private final Player player;
	private final SkullPlayer skullPlayer;
	private final SkullsMenuListingType listingType;

	public MenuList(@NonNull final Player player, @NonNull final SkullCategory category, @NonNull final SkullsMenuListingType listingType) {
		super(6, Skulls.getSkullManager().getSkullsByCategory(category));
		setTitle(Settings.ListingMenu.CATEGORY_TITLE.replace("{category_name}", category.getName()));
		this.fromMain = true;
		this.player = player;
		this.skullPlayer = SkullsAPI.getPlayer(player.getUniqueId());
		this.listingType = listingType;
	}

	public MenuList(@NonNull final Player player, @NonNull final List<Skull> skulls, @NonNull final String keywords) {
		super(RowByContentSize.get(skulls.size()), SkullsAPI.getSkullsByTerm(keywords));
		setTitle(Settings.ListingMenu.SEARCH_TITLE.replace("{search_term}", keywords.replace("id:", "")));
		this.fromMain = true;
		this.player = player;
		this.skullPlayer = SkullsAPI.getPlayer(player.getUniqueId());
		this.listingType = SkullsMenuListingType.SEARCH;
	}

	@Override
	protected ItemStack convertToItemStack(Skull item) {
		final List<String> lore = new ArrayList<>();

		lore.add(Settings.ListingMenu.Format.ID.replace("{skull_id}", String.valueOf(item.getId())));

		if (this.listingType == SkullsMenuListingType.SEARCH)
			lore.add(Settings.ListingMenu.Format.CATEGORY.replace("{skull_category}", item.getCategory()));

		lore.add(Settings.ListingMenu.Format.TAGS.replace("{skull_tags}", String.join(", ", item.getTags())));

		if (Settings.CHARGE_FOR_HEADS)
			lore.add(Settings.ListingMenu.Format.PRICE.replace("{skull_price}", item.getPrice() + ""));

		lore.add("");
		lore.add(Settings.ListingMenu.Format.TAKE);

		if (Valid.checkPermission(player, Permissions.ADD_TO_CATEGORY))
			lore.add(Settings.ListingMenu.Format.ADD_TO_CATEGORY);

		if (player.isOp() || Valid.checkPermission(player, Permissions.FAVOURITE)) {
			if (this.skullPlayer.favouriteSkulls().contains(item.getId()))
				lore.add(Settings.ListingMenu.Format.UN_FAVOURITE);
			else
				lore.add(Settings.ListingMenu.Format.FAVOURITE);
		}

		if (this.skullPlayer.favouriteSkulls().contains(item.getId())) {
			lore.add("");
			lore.add(Settings.ListingMenu.Format.FAVOURITED);
		}

		return ItemCreator.of(item.getItemStack()).name(Settings.ListingMenu.Format.NAME.replace("{skull_name}", item.getName())).lores(lore).build().make();
	}

	@Override
	protected void onPageClick(Player player, Skull item, ClickType click) {
		switch (click) {
			case LEFT:
				if (Settings.CHARGE_FOR_HEADS && !EconomyManager.getInstance().has(player, item.getPrice()) || !Valid.checkPermission(player, Permissions.FREE_SKULLS)) {
					Common.tell(player, Localization.NO_MONEY);
					return;
				}

				if (Settings.CHARGE_FOR_HEADS) {
					EconomyManager.getInstance().withdraw(player, item.getPrice());
					Common.tell(player, Localization.WITHDRAW.replace("{value}", String.valueOf(MathUtil.formatTwoDigitsD(item.getPrice()))));
				}

				PlayerUtil.addItems(player.getInventory(), item.getItemStack());
				break;
			case RIGHT:
				if (!player.isOp() || !Valid.checkPermission(player, Permissions.FAVOURITE)) return;
				if (!this.skullPlayer.favouriteSkulls().contains(item.getId()))
					this.skullPlayer.favouriteSkulls().add(item.getId());
				else
					this.skullPlayer.favouriteSkulls().remove(Integer.valueOf(item.getId()));
				redraw();
				break;
			case MIDDLE:
				break;
		}
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		if (this.fromMain)
			new MenuMain(SkullsAPI.getPlayer(player.getUniqueId())).displayTo(player);
	}

	@Override
	protected boolean addPageNumbers() {
		return false;
	}
}
