package ca.tweetzy.skulls.menus;

import ca.tweetzy.skulls.RowByContentSize;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.api.enums.SkullsMenuListingType;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.model.Permissions;
import ca.tweetzy.skulls.model.SkullMaterial;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.Valid;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.conversation.SimplePrompt;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.button.ButtonConversation;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.remain.Remain;
import lombok.NonNull;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 4:01 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuCategoryList extends MenuPagged<SkullCategory> {

	private final SkullPlayer skullPlayer;
	private final Button newButton;
	private final Player player;
	private final boolean addingSkull;

	public MenuCategoryList(@NonNull final SkullPlayer skullPlayer, final boolean addingSkull) {
		super(Math.min(2, Math.max(6, RowByContentSize.get(SkullsAPI.getCustomCategories().size()) + 1)), SkullsAPI.getCustomCategories());
		setTitle(Settings.CategoryListMenu.TITLE);
		this.skullPlayer = skullPlayer;
		this.player = Remain.getPlayerByUUID(this.skullPlayer.getPlayerId());
		this.addingSkull = addingSkull;

		this.newButton = new ButtonConversation(new SimplePrompt(true) {
			@Override
			protected String getPrompt(ConversationContext context) {
				return Localization.ENTER_CATEGORY_ID;
			}

			@Override
			protected String getFailedValidationText(ConversationContext context, String input) {
				return Localization.CATEGORY_ID_TAKEN.replace("{category_id}", input.trim());
			}

			@Override
			protected boolean isInputValid(ConversationContext context, String input) {
				return !input.isEmpty() && !Skulls.getSkullCategoryManager().getCategories().contains(input.trim());
			}

			@Nullable
			@Override
			protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String input) {
				if (SkullsAPI.getCategory(input.toLowerCase()) != null) {
					Common.tell(getViewer(), Localization.CATEGORY_ID_TAKEN.replace("{category_id}", input.trim()));
				} else {
					SkullsAPI.addCategory(new SkullCategory(input.toLowerCase(), input, true, new StrictList<>()));
					Common.tell(getViewer(), Localization.CATEGORY_CREATED.replace("{category_id}", input.trim()));
				}
				return END_OF_CONVERSATION;
			}
		}, ItemCreator.of(SkullMaterial.get(Settings.CategoryListMenu.Items.NEW_ITEM)).name(Settings.CategoryListMenu.Items.NEW_NAME).lores(Settings.CategoryListMenu.Items.NEW_LORE));
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == getSize() - 1) {
			if (this.player.isOp() || Valid.checkPermission(this.player, Permissions.ADD_NEW_CATEGORY))
				return newButton.getItem();
			else
				return Button.makeDummy(backgroundItem()).getItem();
		}
		return super.getItemAt(slot);
	}

	@Override
	protected ItemStack convertToItemStack(SkullCategory item) {
		final List<String> lore = new ArrayList<>();
		lore.add(Settings.CategoryListMenu.Items.CATEGORY_LORE_VIEW);
		if (this.player.isOp() || Valid.checkPermission(this.player, Permissions.ADD_NEW_CATEGORY))
			lore.add(Settings.CategoryListMenu.Items.CATEGORY_LORE_DELETE);

		return ItemCreator
				.of(SkullMaterial.get(Settings.CategoryListMenu.Items.CATEGORY_ITEM))
				.name(Settings.CategoryListMenu.Items.CATEGORY_NAME.replace("{category_name}", item.getName()))
				.lores(this.addingSkull ? Collections.emptyList() : lore)
				.build().make();
	}

	@Override
	protected void onPageClick(Player player, SkullCategory category, ClickType click) {
		switch (click) {
			case LEFT:
				if (this.addingSkull) {
					final int skullId = player.getMetadata("Skulls:Adding").get(0).asInt();
					SkullsAPI.addSkull(category, skullId);

					final Menu menu = (Menu) player.getMetadata("Skulls:ListMenu").get(0).value();
					if (menu != null)
						menu.displayTo(player);

					return;
				}

				new MenuList(player, category, SkullsMenuListingType.CUSTOM_CATEGORY).displayTo(player);
				break;
			case MIDDLE:
				if (!this.addingSkull && player.isOp() || Valid.checkPermission(player, Permissions.DELETE_CATEGORY)) {
					SkullsAPI.removeCategory(category);
					newInstance().displayTo(player);
				}
				break;
		}
	}

	@Override
	protected ItemStack backgroundItem() {
		return Settings.CategoryListMenu.BACKGROUND.toItem();
	}

	@Override
	public Menu newInstance() {
		return new MenuCategoryList(this.skullPlayer, this.addingSkull);
	}
}
