package ca.tweetzy.skulls.api;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.impl.Skull;
import ca.tweetzy.skulls.impl.SkullCategory;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.model.SkullCategoryManager;
import ca.tweetzy.skulls.model.SkullManager;
import ca.tweetzy.skulls.model.SkullPlayerManager;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:40 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class SkullsAPI {

	private final SkullManager SKULL_MANAGER = Skulls.getSkullManager();
	private final SkullCategoryManager SKULL_CATEGORY_MANAGER = Skulls.getSkullCategoryManager();
	private final SkullPlayerManager SKULL_PLAYER_MANAGER = Skulls.getSkullPlayerManager();

	/**
	 * Used to add a new {@link Skull} to the list
	 *
	 * @param skull is the {@link Skull} being added
	 */
	public void addSkull(@NonNull final Skull skull) {
		SKULL_MANAGER.addSkull(skull);
	}

	/**
	 * Used to remove an existing {@link Skull} from the list
	 *
	 * @param skull is the {@link Skull} being removed
	 */
	public void removeSkull(@NonNull final Skull skull) {
		SKULL_MANAGER.removeSkull(skull);
	}

	/**
	 * Used to get an instance of a particular {@link Skull}
	 *
	 * @param id is the id of the skull being looked up
	 * @return the found {@link Skull} or null
	 */
	public Skull getSkull(final int id) {
		return SKULL_MANAGER.getSkull(id);
	}

	/**
	 * Get a random skull from the list
	 *
	 * @return a random {@link Skull}
	 */
	public Skull getRandomSkull() {
		return SKULL_MANAGER.getRandomSkull();
	}

	/**
	 * Used to quickly get the textured skull, this doesn't
	 * have tags / nbt on it
	 *
	 * @param id is the id of the {@link Skull}
	 * @return the textured {@link ItemStack}
	 */
	public ItemStack getSkullItemStack(final int id) {
		return SKULL_MANAGER.getSkullItemStack(id);
	}

	/**
	 * Used to get all the skulls by a particular category
	 *
	 * @param category is the id of the {@link SkullCategory}
	 * @return a list of skulls with that category
	 */
	public List<Skull> getSkullsByCategory(@NonNull final String category) {
		return SKULL_MANAGER.getSkullsByCategory(category);
	}

	/**
	 * Used to get all categories that are custom-made
	 *
	 * @return a list of custom-made categories
	 */
	public List<SkullCategory> getCustomCategories() {
		return SKULL_CATEGORY_MANAGER.getCustomCategories();
	}

	/**
	 * Used to get all the skulls by a particular category
	 *
	 * @param category is the {@link SkullCategory}
	 * @return a list of skulls with that category
	 */
	public List<Skull> getSkullsByCategory(@NonNull final SkullCategory category) {
		return getSkullsByCategory(category.getId());
	}

	/**
	 * Add a skull to a custom category
	 *
	 * @param category is the {@link SkullCategory}
	 * @param id       is the id of the {@link Skull}
	 */
	public void addSkull(@NonNull final SkullCategory category, final int id) {
		SKULL_CATEGORY_MANAGER.addSkull(category, id);
	}

	/**
	 * Removes a skull from a custom category
	 *
	 * @param category is the {@link SkullCategory}
	 * @param id       is the id of the {@link Skull}
	 */
	public void removeSkull(@NonNull final SkullCategory category, final int id) {
		SKULL_CATEGORY_MANAGER.removeSkull(category, id);
	}

	/**
	 * Used to search for skulls that match a certain keyword / phrase
	 *
	 * @param keywords is the keyword / phrase
	 * @return a list of skulls that match
	 */
	public List<Skull> getSkullsByTerm(@NonNull final String keywords) {
		return SKULL_MANAGER.getSkullsByTerm(keywords);
	}

	/**
	 * Used to add new a category to the list
	 *
	 * @param category is the {@link SkullCategory} being added
	 */
	public void addCategory(@NonNull final SkullCategory category) {
		SKULL_CATEGORY_MANAGER.addCategory(category);
	}

	/**
	 * Used to remove an existing category from the list
	 *
	 * @param category is the {@link SkullCategory} being removed
	 */
	public void removeCategory(@NonNull final SkullCategory category) {
		SKULL_CATEGORY_MANAGER.removeCategory(category);
	}

	/**
	 * Get a skull category or return null if it can't be found
	 *
	 * @param id is the id of the {@link SkullCategory}
	 */
	public SkullCategory getCategory(@NonNull final String id) {
		return SKULL_CATEGORY_MANAGER.getCategory(id);
	}

	/**
	 * Add a new skull player to the list
	 *
	 * @param skullPlayer is the {@link SkullPlayer} object
	 */
	public void addPlayer(@NonNull final SkullPlayer skullPlayer) {
		SKULL_PLAYER_MANAGER.addPlayer(skullPlayer);
	}

	/**
	 * Used to remove an existing skull player
	 *
	 * @param skullPlayer is the instance of {@link SkullPlayer}
	 */
	public void removePlayer(@NonNull final SkullPlayer skullPlayer) {
		SKULL_PLAYER_MANAGER.removePlayer(skullPlayer);
	}

	/**
	 * Used to remove an existing skull player
	 *
	 * @param uuid is the id of the {@link SkullPlayer}
	 */
	public void removePlayer(@NonNull final UUID uuid) {
		SKULL_PLAYER_MANAGER.removePlayer(uuid);
	}

	/**
	 * Get a skull player from the list
	 *
	 * @param uuid is the {@link UUID} of the player
	 * @return the {@link SkullPlayer} or null
	 */
	public SkullPlayer getPlayer(@NonNull final UUID uuid) {
		return SKULL_PLAYER_MANAGER.getPlayer(uuid);
	}

	/**
	 * Used to get a list of heads based on the provided ids
	 *
	 * @param ids the ids of the skulls
	 * @return a list of skulls from the ids
	 */
	public List<Skull> getSkullsByIds(@NonNull final List<Integer> ids) {
		return SKULL_MANAGER.getSkullsByIds(ids);
	}

	/**
	 * Used to set the price of a skull quickly
	 *
	 * @param skullId is the id of the skull
	 * @param price the new price
	 */
	public void updateSkullPrice(final int skullId, final double price) {
		SKULL_MANAGER.updateSkullPrice(skullId, price);
	}

	/**
	 * For internal use
	 */
	public String cleanSearch(@NonNull String keywords) {
		if (keywords.length() > 0 && keywords.charAt(keywords.length() - 1) == '\\') {
			keywords = keywords.substring(0, keywords.length() - 1);
		}
		return keywords;
	}

	/**
	 * Get the total amount of an item in the player's inventory
	 *
	 * @param player is the player being checked
	 * @param stack  is the item you want to find
	 * @return the total count of the item(s)
	 */
	public int getItemCountInPlayerInventory(@NonNull final Player player, ItemStack stack) {
		int total = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null || !item.isSimilar(stack)) continue;
			total += item.getAmount();
		}
		return total;
	}

	/**
	 * Removes a set amount of a specific item from the player inventory
	 *
	 * @param player is the player you want to remove the item from
	 * @param stack  is the item that you want to remove
	 * @param amount is the amount of items you want to remove.
	 */
	public void removeSpecificItemQuantityFromPlayer(@NonNull final Player player, @NonNull final ItemStack stack, int amount) {
		int i = amount;
		for (int j = 0; j < player.getInventory().getSize(); j++) {
			ItemStack item = player.getInventory().getItem(j);
			if (item == null) continue;
			if (!item.isSimilar(stack)) continue;

			if (i >= item.getAmount()) {
				player.getInventory().clear(j);
				i -= item.getAmount();
			} else if (i > 0) {
				item.setAmount(item.getAmount() - i);
				i = 0;
			} else {
				break;
			}
		}
	}
}
