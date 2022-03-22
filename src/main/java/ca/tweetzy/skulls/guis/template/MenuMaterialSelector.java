package ca.tweetzy.skulls.guis.template;


import ca.tweetzy.tweety.gui.GuiUtils;
import ca.tweetzy.tweety.gui.helper.InventoryBorder;
import ca.tweetzy.tweety.gui.helper.InventorySafeMaterials;
import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.model.chat.Gradient;
import ca.tweetzy.tweety.remain.comp.CompMaterial;
import ca.tweetzy.tweety.util.ItemUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Date Created: April 02 2022
 * Time Created: 12:21 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuMaterialSelector extends View {

	private final Consumer<CompMaterial> selected;

	public MenuMaterialSelector(Consumer<CompMaterial> selected) {
		super(Common.gradient(Gradient.ORANGE, "Select a Material"), 6);
		setDefaultItem(GuiUtils.createButtonItem(CompMaterial.BLACK_STAINED_GLASS_PANE, Collections.emptyList()));
		this.selected = selected;
		draw();
	}

	private void draw() {
		reset();

		final List<CompMaterial> itemsToFill = InventorySafeMaterials.get().stream().skip((page - 1) * (long) this.fillSlots().size()).limit(this.fillSlots().size()).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(InventorySafeMaterials.get().size() / (double) this.fillSlots().size()));

		setPrevPage(5, 3, ItemCreator.of(CompMaterial.ARROW, "&ePrevious").make());
		setNextPage(5, 5, ItemCreator.of(CompMaterial.ARROW, "&eNext").make());
		setOnPage(e -> draw());

		for (int i = 0; i < this.rows * 9; i++) {
			if (this.fillSlots().contains(i) && this.fillSlots().indexOf(i) < itemsToFill.size()) {
				final CompMaterial material = itemsToFill.get(this.fillSlots().indexOf(i));

				setButton(i, GuiUtils.createButtonItem(material, 1, Common.colorize("&e" + ItemUtil.bountifyCapitalized(material)), Common.colorize("&eClick &7to select")), e -> this.selected.accept(material));
			}
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(this.rows);
	}
}
