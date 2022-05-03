package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.comp.enums.CompMaterial;
import ca.tweetzy.rose.gui.template.BaseGUI;
import ca.tweetzy.rose.utils.ChatUtil;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.rose.utils.input.TitleInput;
import ca.tweetzy.skulls.SkullItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.BaseCategory;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.settings.Translation;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Date Created: April 20 2022
 * Time Created: 9:47 p.m.
 *
 * @author Kiran Hart
 */
public final class MainGUI extends BaseGUI {

	private final Player player;

	public MainGUI(@NonNull final Player player) {
		super(null, Translation.GUI_MAIN_TITLE.getString(), 6);
		this.player = player;
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

		setButton(4, 2, QuickItem.of(SkullItem.get("skulls:25001"))
				.name(Translation.GUI_MAIN_ITEMS_CUSTOM_CATEGORIES_NAME.getString())
				.lore(Translation.GUI_MAIN_ITEMS_CUSTOM_CATEGORIES_LORE.getList())
				.make(), click -> click.manager.showGUI(click.player, new CustomCategoryListGUI(this)));

		setButton(4, 6, QuickItem.of(SkullItem.get("skulls:39696"))
				.name(Translation.GUI_MAIN_ITEMS_FAVOURITES_NAME.getString())
				.lore(Translation.GUI_MAIN_ITEMS_FAVOURITES_LORE.getList())
				.make(), click -> click.manager.showGUI(click.player, new SkullsViewGUI(this, Skulls.getPlayerManager().findPlayer(click.player), "", ViewMode.FAVOURITE)));


		if (this.player.hasPermission("skulls.admin") || this.player.isOp()) {
			setButton(5, 0, QuickItem.of(CompMaterial.REPEATER)
					.name("&e&lSettings")
					.lore(
							"&8Skull Settings",
							"&7You can view skulls that are new here",
							"&7and then download them if you want.",
							"",
							"&e&lClick &8» &7To view settings"
					)
					.make(), click -> click.manager.showGUI(click.player, new HistoryViewGUI(this)));

			setButton(5, 8, QuickItem.of(CompMaterial.DIAMOND)
					.name("&e&lPatreon")
					.lore(
							"&8Support me on Patreon",
							"&7By supporting me on Patreon you will",
							"&7be helping me be able to continue updating",
							"&7and creating free plugins.",
							"",
							"&8&oDon't worry, normal players can't see this",
							"",
							"&e&lClick &8» &7To view Patreon"
					)
					.glow(true)
					.make(), click -> {

				click.gui.close();
				Common.tellNoPrefix(click.player,
						"&8&m-----------------------------------------------------",
						"",
						ChatUtil.centerMessage("&E&lTweetzy Patreon"),
						ChatUtil.centerMessage("&bhttps://patreon.tweetzy.ca"),
						"&8&m-----------------------------------------------------"
				);
			});
		}
	}
}
