package ca.tweetzy.skulls.impl;

import ca.tweetzy.rose.comp.NBTEditor;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Date Created: April 05 2022
 * Time Created: 1:51 p.m.
 *
 * @author Kiran Hart
 */
@AllArgsConstructor
public final class TexturedSkull implements Skull {

	private final int id;
	private String name;
	private final String category;
	private final List<String> tags;
	private final String texture;
	private double price;
	private boolean blocked;

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
	public List<String> getTags() {
		return this.tags;
	}

	@Override
	public String getTexture() {
		return this.texture;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

	@Override
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public boolean isBlocked() {
		return this.blocked;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public ItemStack getItemStack() {
		return QuickItem.of(NBTEditor.getHead(this.texture)).name(this.name).make();
	}

	@Override
	public void sync() {
		Skulls.getDataManager().updateSkull(this, null);
	}
}
