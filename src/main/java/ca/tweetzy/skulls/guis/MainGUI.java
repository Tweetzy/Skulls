package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.gui.template.BaseGUI;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.settings.Translation;

/**
 * Date Created: April 20 2022
 * Time Created: 9:47 p.m.
 *
 * @author Kiran Hart
 */
public final class MainGUI extends BaseGUI {

	public MainGUI() {
		super(null, Translation.GUI_MAIN_TITLE.getString(), 6);
		draw();
	}

	@Override
	protected void draw() {

		for (BaseCategory baseCategory : BaseCategory.values()) {
			setButton(baseCategory.getSlot(), QuickItem
					.of(Skulls.getSkullManager().getSkullItem(baseCategory.getTexture()))
					.name(Translation.GUI_MAIN_ITEMS_CATEGORY_NAME.getString("category_name", baseCategory.getName()))
					.lore(Translation.GUI_MAIN_ITEMS_CATEGORY_LORE.getList("category_size", Skulls.getSkullManager().getSkullCount(baseCategory.getId())))
					.make(), click -> click.manager.showGUI(click.player, new SkullsViewGUI(this, null, baseCategory.getId(), ViewMode.LIST)));
		}
	}
}
