package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.gui.template.BaseGUI;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.rose.utils.input.TitleInput;
import ca.tweetzy.skulls.SkullItem;
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
			if (!baseCategory.isEnabled()) continue;
			setButton(baseCategory.getSlot(), QuickItem
					.of(Skulls.getSkullManager().getSkullItem(baseCategory.getTexture()))
					.name(Translation.GUI_MAIN_ITEMS_CATEGORY_NAME.getString("category_name", baseCategory.getName()))
					.lore(Translation.GUI_MAIN_ITEMS_CATEGORY_LORE.getList("category_size", Skulls.getSkullManager().getSkullCount(baseCategory.getId())))
					.make(), click -> {

				if (!click.player.hasPermission("skulls.category." + baseCategory.getId().toLowerCase())) {
					Common.tell(click.player, Translation.CATEGORY_PERMISSION.getKey());
					return;
				}

				click.manager.showGUI(click.player, new SkullsViewGUI(this, Skulls.getPlayerManager().findPlayer(click.player), baseCategory.getId(), ViewMode.LIST));
			});
		}

		setButton(4, 4, QuickItem.of(SkullItem.get("skulls:5650"))
				.name(Translation.GUI_MAIN_ITEMS_SEARCH_NAME.getString())
				.lore(Translation.GUI_MAIN_ITEMS_SEARCH_LORE.getList())
				.make(), click -> new TitleInput(click.player, Translation.INPUT_SKULL_SEARCH_TITLE.getString(), Translation.INPUT_SKULL_SEARCH_SUBTITLE.getString()) {

			@Override
			public boolean onResult(String string) {
				if (string.matches("[\\\\^$.|?*+(){}]")) return false;
				Skulls.getGuiManager().showGUI(click.player, new SkullsViewGUI(MainGUI.this, Skulls.getPlayerManager().findPlayer(click.player), string.trim(), ViewMode.SEARCH));
				return true;
			}
		});

		setButton(4, 6, QuickItem.of(SkullItem.get("skulls:39696"))
				.name(Translation.GUI_MAIN_ITEMS_FAVOURITES_NAME.getString())
				.lore(Translation.GUI_MAIN_ITEMS_FAVOURITES_LORE.getList())
				.make(), click -> click.manager.showGUI(click.player, new SkullsViewGUI(this, Skulls.getPlayerManager().findPlayer(click.player), "", ViewMode.FAVOURITE)));
	}
}
