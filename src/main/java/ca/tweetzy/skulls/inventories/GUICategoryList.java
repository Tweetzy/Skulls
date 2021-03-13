package ca.tweetzy.skulls.inventories;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.utils.PlayerUtils;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.core.utils.items.TItemBuilder;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.Skull;
import ca.tweetzy.skulls.skull.SkullCategory;
import org.bukkit.event.inventory.ClickType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 11 2021
 * Time Created: 11:43 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUICategoryList extends Gui {

    final SkullCategory.BaseCategory category;

    public GUICategoryList(SkullCategory.BaseCategory category) {
        this.category = category;
        setTitle(category.getName());
        setRows(6);
        setAcceptsItems(false);
        draw();
    }

    private void draw() {
        reset();
        pages = (int) Math.max(1, Math.ceil(Skulls.getInstance().getSkullManager().getSkulls(this.category).size() / (double) 45));

        setTitle(TextUtils.formatText(Settings.GUI_CATEGORY_GUI_TITLE.getString().replace("%current_page%", String.valueOf(page)).replace("%max_pages%", String.valueOf(pages)).replace("%category%", category.getName())));
        setItems(45, 53, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        setPrevPage(5, 3, new TItemBuilder(Objects.requireNonNull(Settings.GUI_BACK_BTN_ITEM.getMaterial().parseMaterial())).setName(Settings.GUI_BACK_BTN_NAME.getString()).setLore(Settings.GUI_BACK_BTN_LORE.getStringList()).toItemStack());
        setButton(5, 4, new TItemBuilder(Objects.requireNonNull(Settings.GUI_CLOSE_BTN_ITEM.getMaterial().parseMaterial())).setName(Settings.GUI_CLOSE_BTN_NAME.getString()).setLore(Settings.GUI_CLOSE_BTN_LORE.getStringList()).toItemStack(), e -> e.manager.showGUI(e.player, new GUIMain(e.player)));
        setNextPage(5, 5, new TItemBuilder(Objects.requireNonNull(Settings.GUI_NEXT_BTN_ITEM.getMaterial().parseMaterial())).setName(Settings.GUI_NEXT_BTN_NAME.getString()).setLore(Settings.GUI_NEXT_BTN_LORE.getStringList()).toItemStack());
        setOnPage(e -> draw());

        int slot = 0;
        List<Skull> data = Skulls.getInstance().getSkullManager().getSkulls(this.category).stream().sorted(Comparator.comparing(Skull::getName)).skip((page - 1) * 45L).limit(45).collect(Collectors.toList());
        for (Skull skull : data) {
            setItem(slot, SkullAPI.getInstance().getTexturedHead(skull.getBase64(), Settings.GUI_CATEGORY_HEAD_TITLE.getString(), skull.isFavourite() ? Settings.GUI_CATEGORY_HEAD_LORE_FAV.getStringList() : Settings.GUI_CATEGORY_HEAD_LORE.getStringList(), new HashMap<String, Object>(){{
                put("%head_id%", skull.getUuid().toString());
                put("%head_name%", skull.getName());
                put("%head_tags%", String.join(", ", Arrays.asList(skull.getTags())));
                put("%head_category%", skull.getCategory().getBaseCategory().getName());
            }}));

            setAction(slot, ClickType.MIDDLE, e -> {
                Skulls.getInstance().getSkullManager().toggleFavouriteSkull(skull.getUuid(), !skull.isFavourite());
                draw();
            });

            setAction(slot, ClickType.RIGHT, e -> PlayerUtils.giveItem(e.player, skull.getItem()));
            setAction(slot, ClickType.LEFT, e -> PlayerUtils.giveItem(e.player, skull.getItem()));

            slot++;
        }
    }
}
