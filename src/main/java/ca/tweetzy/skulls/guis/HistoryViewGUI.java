package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.comp.enums.CompMaterial;
import ca.tweetzy.rose.gui.Gui;
import ca.tweetzy.rose.gui.events.GuiClickEvent;
import ca.tweetzy.rose.gui.template.PagedGUI;
import ca.tweetzy.rose.utils.Common;
import ca.tweetzy.rose.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.History;
import ca.tweetzy.skulls.settings.Translation;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date Created: May 03 2022
 * Time Created: 12:51 p.m.
 *
 * @author Kiran Hart
 */
public final class HistoryViewGUI extends PagedGUI<History> {

	public HistoryViewGUI(Gui parent) {
		super(parent, Translation.GUI_HISTORIES_TITLE.getString(), 6, Skulls.getSkullManager().getHistories());
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(History history) {
		final boolean downloaded = Skulls.getSkullManager().getIdList().contains(history.getSkulls().get(0));

		final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
		final Date resultDate = new Date(history.getTime());

		return QuickItem
				.of(downloaded ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name(Translation.GUI_HISTORIES_ITEMS_HISTORY_NAME.getString("history_id", history.getID()))
				.lore(Translation.GUI_HISTORIES_ITEMS_HISTORY_LORE.getList(
						"history_size", history.getSkulls().size(),
						"is_true", downloaded ? Translation.MISC_IS_TRUE.getString() : Translation.MISC_IS_FALSE.getString(),
						"history_time", sdf.format(resultDate)
				))
				.make();
	}

	@Override
	protected void onClick(History history, GuiClickEvent clickEvent) {
		final boolean downloaded = Skulls.getSkullManager().getIdList().contains(history.getSkulls().get(0));
		if (downloaded) return;

		Common.runAsync(() -> {
			Skulls.getSkullManager().downloadHistorySkulls(history, finished -> clickEvent.manager.showGUI(clickEvent.player, new HistoryViewGUI(new MainGUI(clickEvent.player))));
		});
	}
}
