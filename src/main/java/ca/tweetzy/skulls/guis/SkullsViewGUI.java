package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.gui.Gui;
import ca.tweetzy.rose.gui.events.GuiClickEvent;
import ca.tweetzy.rose.gui.template.PagedGUI;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.api.interfaces.Skull;
import ca.tweetzy.skulls.impl.SkullPlayer;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translation;
import org.bukkit.inventory.ItemStack;

/**
 * Date Created: April 21 2022
 * Time Created: 11:59 a.m.
 *
 * @author Kiran Hart
 */
public final class SkullsViewGUI extends PagedGUI<Skull> {

	private SkullPlayer skullPlayer;
	private final ViewMode viewMode;

	public SkullsViewGUI(Gui parent, SkullPlayer skullPlayer, String category, ViewMode viewMode) {
		super(
				parent,
				viewMode == ViewMode.SEARCH ? Translation.GUI_SKULLS_LIST_TITLE_SEARCH.getString("search_phrase", category) : Translation.GUI_SKULLS_LIST_TITLE_CATEGORY.getString("category_name", category),
				6,
				viewMode == ViewMode.SEARCH ? Skulls.getSkullManager().getSkullsBySearch(category) : Skulls.getSkullManager().getSkulls(category)
		);

		this.viewMode = viewMode;
		this.skullPlayer = skullPlayer;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Skull skull) {
		final QuickItem item = QuickItem.of(skull.getItemStack()).name(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_NAME.getString("skull_name", skull.getName()));

		item.lore(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_ID.getString("skull_id", skull.getId()));
		item.lore(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAGS.getString("skull_tags", String.join(", ", skull.getTags())));

		if (Settings.CHARGE_FOR_HEADS.getBoolean() && skull.getPrice() > 0) {
			item.lore(Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_PRICE.getString("skull_price", String.format("%,.2f", skull.getPrice())));
		}

		item.lore(" ");
		item.lore(skull.isBlocked() ? Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_BLOCKED.getString() : Translation.GUI_SKULLS_LIST_ITEMS_SKULL_LORE_TAKE.getString());

		return item.make();
	}

	@Override
	protected void onClick(Skull skull, GuiClickEvent guiClickEvent) {

	}
}
