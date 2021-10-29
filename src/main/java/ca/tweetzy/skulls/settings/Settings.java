package ca.tweetzy.skulls.settings;

import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.settings.SimpleSettings;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: August 23 2021
 * Time Created: 12:10 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Settings extends SimpleSettings {

	public static String PREFIX;
	public static String ECONOMY_PROVIDER;
	public static CompMaterial ITEM_ECONOMY_MATERIAL;
	public static Boolean CHARGE_FOR_HEADS;
	public static Boolean ALLOW_NON_PERM_USE;

	private static void init() {
		PREFIX = getString("Prefix");
		ECONOMY_PROVIDER = getString("Economy Provider");
		ITEM_ECONOMY_MATERIAL = getMaterial("Item Economy Material");
		CHARGE_FOR_HEADS = getBoolean("Charge For Heads");
		ALLOW_NON_PERM_USE = getBoolean("Allow Non Permission Access");
	}

	public static final class ListingMenu {

		public static String SEARCH_TITLE;
		public static String CATEGORY_TITLE;
		public static String FAVOURITES_TITLE;

		public static final class Format {

			public static String NAME;
			public static String ID;
			public static String CATEGORY;
			public static String TAGS;
			public static String TAKE;
			public static String ADD_TO_CATEGORY;
			public static String REMOVE_FROM_CATEGORY;
			public static String EDIT_PRICE;
			public static String FAVOURITE;
			public static String UN_FAVOURITE;
			public static String FAVOURITED;
			public static String PRICE;

			private static void init() {
				pathPrefix("Gui.Listing.Skull Format");

				NAME = getString("Name");
				ID = getString("Id");
				CATEGORY = getString("Category");
				TAGS = getString("Tags");
				TAKE = getString("Take");
				ADD_TO_CATEGORY = getString("Add To Category");
				REMOVE_FROM_CATEGORY = getString("Remove From Category");
				EDIT_PRICE = getString("Edit Price");
				FAVOURITE = getString("Favourite");
				UN_FAVOURITE = getString("Unfavourite");
				FAVOURITED = getString("Favourited");
				PRICE = getString("Price");
			}
		}

		private static void init() {
			pathPrefix("Gui.Listing");
			SEARCH_TITLE = getString("Search Title");
			CATEGORY_TITLE = getString("Category Title");
			FAVOURITES_TITLE = getString("Favourite Title");
		}
	}

	public static final class CategoryListMenu {

		public static String TITLE;
		public static CompMaterial BACKGROUND;

		public static final class Items {

			public static String NEW_ITEM;
			public static String NEW_NAME;
			public static List<String> NEW_LORE;

			public static String CATEGORY_ITEM;
			public static String CATEGORY_NAME;
			public static String CATEGORY_LORE_DELETE;
			public static String CATEGORY_LORE_VIEW;

			private static void init() {
				pathPrefix("Gui.Category List.Items");

				NEW_ITEM = getString("New.Material");
				NEW_NAME = getString("New.Name");
				NEW_LORE = getStringList("New.Lore");

				CATEGORY_ITEM = getString("Category.Material");
				CATEGORY_NAME = getString("Category.Name");
				CATEGORY_LORE_DELETE = getString("Category.Lore Format.Delete");
				CATEGORY_LORE_VIEW = getString("Category.Lore Format.View");
			}
		}

		private static void init() {
			pathPrefix("Gui.Category List");
			TITLE = getString("Title");
			BACKGROUND = getMaterial("Background");
		}
	}

	public static final class MainMenu {

		public static String TITLE;
		public static CompMaterial BACKGROUND;

		private static void init() {
			pathPrefix("Gui.Main");
			TITLE = getString("Title");
			BACKGROUND = getMaterial("Background");
		}

		public static final class Items {

			public static String ALPHABET_ITEM;
			public static String ALPHABET_NAME;
			public static List<String> ALPHABET_LORE;

			public static String ANIMALS_ITEM;
			public static String ANIMALS_NAME;
			public static List<String> ANIMALS_LORE;

			public static String BLOCKS_ITEM;
			public static String BLOCKS_NAME;
			public static List<String> BLOCKS_LORE;

			public static String DECORATION_ITEM;
			public static String DECORATION_NAME;
			public static List<String> DECORATION_LORE;

			public static String FOOD_AND_DRINKS_ITEM;
			public static String FOOD_AND_DRINKS_NAME;
			public static List<String> FOOD_AND_DRINKS_LORE;

			public static String HUMANS_ITEM;
			public static String HUMANS_NAME;
			public static List<String> HUMANS_LORE;

			public static String HUMANOIDS_ITEM;
			public static String HUMANOIDS_NAME;
			public static List<String> HUMANOIDS_LORE;

			public static String MISCELLANEOUS_ITEM;
			public static String MISCELLANEOUS_NAME;
			public static List<String> MISCELLANEOUS_LORE;

			public static String MONSTERS_ITEM;
			public static String MONSTERS_NAME;
			public static List<String> MONSTERS_LORE;

			public static String PLANTS_ITEM;
			public static String PLANTS_NAME;
			public static List<String> PLANTS_LORE;

			public static String FAVOURITES_ITEM;
			public static String FAVOURITES_NAME;
			public static List<String> FAVOURITES_LORE;

			public static String SEARCH_ITEM;
			public static String SEARCH_NAME;
			public static List<String> SEARCH_LORE;

			public static String CUSTOM_CATEGORY_ITEM;
			public static String CUSTOM_CATEGORY_NAME;
			public static List<String> CUSTOM_CATEGORY_LORE;

			private static void init() {
				pathPrefix("Gui.Main.Items");

				ALPHABET_ITEM = getString("Alphabet.Material");
				ALPHABET_NAME = getString("Alphabet.Name");
				ALPHABET_LORE = getStringList("Alphabet.Lore");

				ANIMALS_ITEM = getString("Animals.Material");
				ANIMALS_NAME = getString("Animals.Name");
				ANIMALS_LORE = getStringList("Animals.Lore");

				BLOCKS_ITEM = getString("Blocks.Material");
				BLOCKS_NAME = getString("Blocks.Name");
				BLOCKS_LORE = getStringList("Blocks.Lore");

				DECORATION_ITEM = getString("Decoration.Material");
				DECORATION_NAME = getString("Decoration.Name");
				DECORATION_LORE = getStringList("Decoration.Lore");

				FOOD_AND_DRINKS_ITEM = getString("Food And Drinks.Material");
				FOOD_AND_DRINKS_NAME = getString("Food And Drinks.Name");
				FOOD_AND_DRINKS_LORE = getStringList("Food And Drinks.Lore");

				HUMANS_ITEM = getString("Humans.Material");
				HUMANS_NAME = getString("Humans.Name");
				HUMANS_LORE = getStringList("Humans.Lore");

				HUMANOIDS_ITEM = getString("Humanoids.Material");
				HUMANOIDS_NAME = getString("Humanoids.Name");
				HUMANOIDS_LORE = getStringList("Humanoids.Lore");

				MISCELLANEOUS_ITEM = getString("Miscellaneous.Material");
				MISCELLANEOUS_NAME = getString("Miscellaneous.Name");
				MISCELLANEOUS_LORE = getStringList("Miscellaneous.Lore");

				MONSTERS_ITEM = getString("Monsters.Material");
				MONSTERS_NAME = getString("Monsters.Name");
				MONSTERS_LORE = getStringList("Monsters.Lore");

				PLANTS_ITEM = getString("Plants.Material");
				PLANTS_NAME = getString("Plants.Name");
				PLANTS_LORE = getStringList("Plants.Lore");

				FAVOURITES_ITEM = getString("Favourites.Material");
				FAVOURITES_NAME = getString("Favourites.Name");
				FAVOURITES_LORE = getStringList("Favourites.Lore");

				SEARCH_ITEM = getString("Search.Material");
				SEARCH_NAME = getString("Search.Name");
				SEARCH_LORE = getStringList("Search.Lore");

				CUSTOM_CATEGORY_ITEM = getString("Custom Category.Material");
				CUSTOM_CATEGORY_NAME = getString("Custom Category.Name");
				CUSTOM_CATEGORY_LORE = getStringList("Custom Category.Lore");
			}
		}
	}

	public static final class Categories {

		public static final class Alphabet {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Alphabet");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Animals {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Animals");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Blocks {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Blocks");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Decoration {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Decoration");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class FoodAndDrinks {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Food And Drinks");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Humans {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Humans");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Humanoids {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Humanoids");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Miscellaneous {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Miscellaneous");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Monsters {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Monsters");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}

		public static final class Plants {

			public static String NAME;
			public static Double PRICE;

			private static void init() {
				pathPrefix("Categories.Plants");
				NAME = getString("Name");
				PRICE = getDouble("Price");
			}
		}
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}
}
