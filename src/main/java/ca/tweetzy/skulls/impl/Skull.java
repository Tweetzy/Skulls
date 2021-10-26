package ca.tweetzy.skulls.impl;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.SkullsDefaultCategory;
import ca.tweetzy.skulls.api.interfaces.ISkull;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.menu.model.SkullCreator;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.security.auth.login.Configuration;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 27 2021
 * Time Created: 9:54 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public final class Skull implements ISkull {

	private final int id;
	private final String name;
	private final String category;
	private final StrictList<String> tags;
	private final String texture;

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getCategory() {
		return this.category;
	}

	@Override
	public StrictList<String> getTags() {
		return this.tags;
	}

	@Override
	public String getTexture() {
		return this.texture;
	}

	@Override
	public double getPrice() {
		final ConfigurationSection prices = Skulls.getInstance().getDataFile().getConfigField("Prices");
		if (prices != null && prices.contains(String.valueOf(this.id))) {
			return prices.getDouble(String.valueOf(this.id));
		}

		return SkullsDefaultCategory.valueOf(getCategory().replace("&", "and").replace(" ", "_").toUpperCase()).getDefaultPrice();
	}

	@Override
	public ItemStack getItemStack() {
		return ItemCreator
				.of(SkullCreator.itemFromUrl(this.texture))
				.name(this.name)
				.tag("Skulls:ID", String.valueOf(this.id))
				.tag("Skulls:Texture", this.texture)
				.build().make();
	}
}
