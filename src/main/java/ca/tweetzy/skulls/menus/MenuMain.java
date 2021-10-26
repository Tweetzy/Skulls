package ca.tweetzy.skulls.menus;

import ca.tweetzy.skulls.api.SkullsAPI;
import ca.tweetzy.skulls.api.enums.SkullsDefaultCategory;
import ca.tweetzy.skulls.api.enums.SkullsMenuListingType;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.model.SkullMaterial;
import ca.tweetzy.skulls.settings.Localization;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.conversation.SimplePrompt;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.button.ButtonConversation;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.model.Replacer;
import lombok.NonNull;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 17 2021
 * Time Created: 9:28 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuMain extends Menu {

	private final Button alphabetButton;
	private final Button animalsButton;
	private final Button blocksButton;
	private final Button decorationButton;
	private final Button foodAndDrinksButton;
	private final Button humansButton;
	private final Button humanoidsButton;
	private final Button miscellaneousButton;
	private final Button monstersButton;
	private final Button plantsButton;

	private final Button customCategoryButton;
	private final Button favouritesButton;
	private final Button searchButton;

	private final SkullPlayer skullPlayer;


	public MenuMain(@NonNull final SkullPlayer skullPlayer) {
		this.skullPlayer = skullPlayer;
		setTitle(Settings.MainMenu.TITLE);
		setSize(9 * 6);

		this.alphabetButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.ALPHABET_ITEM))
				.name(Settings.MainMenu.Items.ALPHABET_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.ALPHABET_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.ALPHABET.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.ALPHABET.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.animalsButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.ANIMALS_ITEM))
				.name(Settings.MainMenu.Items.ANIMALS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.ANIMALS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.ANIMALS.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.ANIMALS.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.blocksButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.BLOCKS_ITEM))
				.name(Settings.MainMenu.Items.BLOCKS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.BLOCKS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.BLOCKS.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.BLOCKS.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});


		this.decorationButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.DECORATION_ITEM))
				.name(Settings.MainMenu.Items.DECORATION_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.DECORATION_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.DECORATION.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.DECORATION.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.foodAndDrinksButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.FOOD_AND_DRINKS_ITEM))
				.name(Settings.MainMenu.Items.FOOD_AND_DRINKS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.FOOD_AND_DRINKS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.FOOD_AND_DRINKS.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.FOOD_AND_DRINKS.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.humansButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.HUMANS_ITEM))
				.name(Settings.MainMenu.Items.HUMANS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.HUMANS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.HUMANS.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.HUMANS.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.humanoidsButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.HUMANOIDS_ITEM))
				.name(Settings.MainMenu.Items.HUMANOIDS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.HUMANOIDS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.HUMANOID.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.HUMANOID.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.miscellaneousButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.MISCELLANEOUS_ITEM))
				.name(Settings.MainMenu.Items.MISCELLANEOUS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.MISCELLANEOUS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.MISCELLANEOUS.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.MISCELLANEOUS.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.monstersButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.MONSTERS_ITEM))
				.name(Settings.MainMenu.Items.MONSTERS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.MONSTERS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.MONSTERS.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.MONSTERS.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.plantsButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.PLANTS_ITEM))
				.name(Settings.MainMenu.Items.PLANTS_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.PLANTS_LORE, "category_head_count", SkullsAPI.getSkullsByCategory(SkullsDefaultCategory.PLANTS.getId()).size())), player -> {

			new MenuList(player, SkullsAPI.getCategory(SkullsDefaultCategory.PLANTS.getId()), SkullsMenuListingType.CATEGORY).displayTo(player);
		});

		this.customCategoryButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.CUSTOM_CATEGORY_ITEM))
				.name(Settings.MainMenu.Items.CUSTOM_CATEGORY_NAME)
				.lores(Settings.MainMenu.Items.CUSTOM_CATEGORY_LORE), player -> {

			new MenuCategoryList(SkullsAPI.getPlayer(player.getUniqueId()), false).displayTo(player);

		});

		this.favouritesButton = Button.makeSimple(ItemCreator
				.of(SkullMaterial.get(Settings.MainMenu.Items.FAVOURITES_ITEM))
				.name(Settings.MainMenu.Items.FAVOURITES_NAME)
				.lores(Replacer.replaceArray(Settings.MainMenu.Items.FAVOURITES_LORE, "category_head_count", skullPlayer.favouriteSkulls().size())), player -> {

			new MenuList(SkullsAPI.getPlayer(player.getUniqueId())).displayTo(player);
		});

		this.searchButton = new ButtonConversation(new SimplePrompt() {
			@Override
			protected String getPrompt(ConversationContext context) {
				return Localization.SEARCH;
			}

			@Nullable
			@Override
			protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String input) {
				Common.runLater(() -> new MenuList(getViewer(), SkullsAPI.getSkullsByTerm(SkullsAPI.cleanSearch(input)), SkullsAPI.cleanSearch(input)).displayTo(getViewer()));
				return END_OF_CONVERSATION;
			}

		}, ItemCreator.of(SkullMaterial.get(Settings.MainMenu.Items.SEARCH_ITEM)).name(Settings.MainMenu.Items.SEARCH_NAME).lores(Settings.MainMenu.Items.SEARCH_LORE));
	}

	@Override
	public ItemStack getItemAt(int slot) {
		switch (slot) {
			case (9 * 1) - 1 + 3:
				return this.alphabetButton.getItem();
			case (9 * 1) - 1 + 4:
				return this.animalsButton.getItem();
			case (9 * 1) - 1 + 5:
				return this.blocksButton.getItem();
			case (9 * 1) - 1 + 6:
				return this.decorationButton.getItem();
			case (9 * 1) - 1 + 7:
				return this.foodAndDrinksButton.getItem();
			case (9 * 2) - 1 + 3:
				return this.humansButton.getItem();
			case (9 * 2) - 1 + 4:
				return this.humanoidsButton.getItem();
			case (9 * 2) - 1 + 5:
				return this.miscellaneousButton.getItem();
			case (9 * 2) - 1 + 6:
				return this.monstersButton.getItem();
			case (9 * 2) - 1 + 7:
				return this.plantsButton.getItem();
			case (9 * 4) - 1 + 3:
				return this.customCategoryButton.getItem();
			case (9 * 4) - 1 + 5:
				return this.favouritesButton.getItem();
			case (9 * 4) - 1 + 7:
				return this.searchButton.getItem();
			default:
				return Settings.MainMenu.BACKGROUND.toItem();
		}
	}

	@Override
	public Menu newInstance() {
		return new MenuMain(this.skullPlayer);
	}
}
