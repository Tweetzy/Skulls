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
import ca.tweetzy.tweety.conversation.SimpleConversation;
import ca.tweetzy.tweety.conversation.SimpleDecimalPrompt;
import ca.tweetzy.tweety.conversation.SimplePrompt;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.plugin.SimplePlugin;
import ca.tweetzy.tweety.remain.Remain;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 19 2021
 * Time Created: 4:18 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuList extends MenuPagged<Skull> {

	private boolean fromMain;
	private final Player player;
	private final SkullPlayer skullPlayer;
	private final SkullsMenuListingType listingType;
	private final SkullCategory category;

	public MenuList(@NonNull final Player player, @NonNull final SkullCategory category, @NonNull final SkullsMenuListingType listingType) {
		super(6, listingType == SkullsMenuListingType.CATEGORY ? Skulls.getSkullManager().getSkullsByCategory(category) : SkullsAPI.getSkullsByIds(category.getSkulls().getSource()));
		setTitle(Settings.ListingMenu.CATEGORY_TITLE.replace("{category_name}", category.getName()));
		this.fromMain = true;
		this.player = player;
		this.skullPlayer = SkullsAPI.getPlayer(player.getUniqueId());
		this.listingType = listingType;
		this.category = category;
	}

	public MenuList(@NonNull final Player player, @NonNull final List<Skull> skulls, @NonNull final String keywords) {
		super(RowByContentSize.get(skulls.size()), SkullsAPI.getSkullsByTerm(keywords));
		setTitle(Settings.ListingMenu.SEARCH_TITLE.replace("{search_term}", keywords.replace("id:", "")));
		this.fromMain = true;
		this.player = player;
		this.skullPlayer = SkullsAPI.getPlayer(player.getUniqueId());
		this.listingType = SkullsMenuListingType.SEARCH;
		this.category = null;
	}

	public MenuList(@NonNull final SkullPlayer player) {
		super(RowByContentSize.get(player.favouriteSkulls().size()), SkullsAPI.getSkullsByIds(player.favouriteSkulls().getSource()));
		setTitle(Settings.ListingMenu.FAVOURITES_TITLE);
		this.fromMain = true;
		this.player = Remain.getPlayerByUUID(player.getPlayerId());
		this.skullPlayer = player;
		this.listingType = SkullsMenuListingType.FAVOURITES;
		this.category = null;
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

		if (this.category != null && this.category.isCustom()) {
			if (PlayerUtil.hasPerm(player, Permissions.REMOVE_FROM_CATEGORY) && this.listingType != SkullsMenuListingType.FAVOURITES)
				lore.add(Settings.ListingMenu.Format.REMOVE_FROM_CATEGORY);
		} else {
			if (PlayerUtil.hasPerm(player, Permissions.ADD_TO_CATEGORY) && this.listingType != SkullsMenuListingType.FAVOURITES)
				lore.add(Settings.ListingMenu.Format.ADD_TO_CATEGORY);
		}

		if (player.isOp() || PlayerUtil.hasPerm(player, Permissions.FAVOURITE)) {
			if (this.skullPlayer.favouriteSkulls().contains(item.getId()))
				lore.add(Settings.ListingMenu.Format.UN_FAVOURITE);
			else
				lore.add(Settings.ListingMenu.Format.FAVOURITE);
		}

		if (this.skullPlayer.favouriteSkulls().contains(item.getId())) {
			lore.add("");
			lore.add(Settings.ListingMenu.Format.FAVOURITED);
		}

		if (Settings.CHARGE_FOR_HEADS)
			lore.add(Settings.ListingMenu.Format.EDIT_PRICE);


		return ItemCreator.of(item.getItemStack()).name(Settings.ListingMenu.Format.NAME.replace("{skull_name}", item.getName())).lores(lore).build().make();
	}

	@Override
	protected void onPageClick(Player player, Skull item, ClickType click) {
		switch (click) {
			case RIGHT:
				handleRightClick(player, item);
				break;
			case MIDDLE:
				handleMiddleClick(player, item);
				break;
			case SHIFT_RIGHT:
				handleShiftRightClick(player, item);
				break;
			default:
				handleOtherClick(player, item);
				break;
		}
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		if (this.listingType == SkullsMenuListingType.CUSTOM_CATEGORY) {
			new MenuCategoryList(this.skullPlayer, false).displayTo(player);
			return;
		}

		if (this.fromMain)
			new MenuMain(SkullsAPI.getPlayer(player.getUniqueId())).displayTo(player);
	}

	private void handleRightClick(Player player, Skull item) {
		if (!player.isOp() || !PlayerUtil.hasPerm(player, Permissions.FAVOURITE)) return;
		if (!this.skullPlayer.favouriteSkulls().contains(item.getId()))
			this.skullPlayer.favouriteSkulls().add(item.getId());
		else
			this.skullPlayer.favouriteSkulls().remove(Integer.valueOf(item.getId()));

		if (listingType == SkullsMenuListingType.FAVOURITES)
			new MenuList(this.skullPlayer).displayTo(player);
		else
			redraw();
	}

	private void handleMiddleClick(Player player, Skull item) {
		if (this.category != null &&this.category.isCustom()) {
			SkullsAPI.removeSkull(this.category, item.getId());
			new MenuList(player, this.category, SkullsMenuListingType.CUSTOM_CATEGORY).displayTo(player);
			return;
		}

		player.setMetadata("Skulls:Adding", new FixedMetadataValue(SimplePlugin.getInstance(), item.getId()));
		player.setMetadata("Skulls:ListMenu", new FixedMetadataValue(SimplePlugin.getInstance(), this));
		this.fromMain = false;
		new MenuCategoryList(this.skullPlayer, true).displayTo(player);
	}

	private void handleShiftRightClick(Player player, Skull item) {
		if (Settings.CHARGE_FOR_HEADS && player.isOp() || PlayerUtil.hasPerm(player, Permissions.EDIT_PRICE)) {
			this.fromMain = false;
			player.closeInventory();
			SimpleDecimalPrompt.show(player, Localization.ENTER_SKULL_PRICE, value -> {
				SkullsAPI.updateSkullPrice(item.getId(), value);
				Common.runLater(() -> this.displayTo(player));
			});
		}
	}

	private void handleOtherClick(Player player, Skull item) {
		if (Settings.CHARGE_FOR_HEADS && !EconomyManager.getInstance().has(player, item.getPrice()) || !PlayerUtil.hasPerm(player, Permissions.FREE_SKULLS)) {
			Common.tell(player, Localization.NO_MONEY);
			return;
		}

		if (Settings.CHARGE_FOR_HEADS) {
			EconomyManager.getInstance().withdraw(player, item.getPrice());
			Common.tell(player, Localization.WITHDRAW.replace("{value}", String.valueOf(MathUtil.formatTwoDigitsD(item.getPrice()))));
		}

		PlayerUtil.addItems(player.getInventory(), item.getItemStack());
	}

	@Override
	public boolean allowShiftActions() {
		return true;
	}

	@Override
	protected boolean addPageNumbers() {
		return false;
	}
}
