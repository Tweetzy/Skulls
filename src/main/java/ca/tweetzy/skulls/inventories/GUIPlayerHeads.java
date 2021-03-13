package ca.tweetzy.skulls.inventories;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.core.utils.items.TItemBuilder;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 12 2021
 * Time Created: 9:38 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUIPlayerHeads extends Gui {

    public GUIPlayerHeads() {
        setTitle(TextUtils.formatText(Settings.GUI_PLAYERS_TITLE.getString()));
        setRows(6);
        setAcceptsItems(false);
        draw();
    }

    private void draw() {
        reset();
        pages = (int) Math.max(1, Math.ceil(Bukkit.getOnlinePlayers().size() / (double) 45));

        setTitle(TextUtils.formatText(Settings.GUI_PLAYERS_TITLE.getString().replace("%current_page%", String.valueOf(page)).replace("%max_pages%", String.valueOf(pages))));
        handleNavigation();

        int slot = 0;
        List<Player> data = Bukkit.getOnlinePlayers().stream().sorted(Comparator.comparing(Player::getName)).skip((page - 1) * 45L).limit(45).collect(Collectors.toList());
        for (Player p : data) {
            setItem(slot, SkullAPI.getInstance().getPlayerHead(p.getName(), Settings.GUI_PLAYERS_HEAD_TITLE.getString(), Settings.GUI_PLAYERS_HEAD_LORE.getStringList(), new HashMap<String, Object>(){{
                put("%head_name%", p.getName());
            }}));

            setAction(slot, ClickType.RIGHT, e -> SkullAPI.getInstance().checkPermissionsBeforeGive(e.player, SkullAPI.getInstance().getPlayerHead(p.getName()), "skulls.takefromgui"));
            setAction(slot, ClickType.LEFT, e -> SkullAPI.getInstance().checkPermissionsBeforeGive(e.player, SkullAPI.getInstance().getPlayerHead(p.getName()), "skulls.takefromgui"));
            slot++;
        }
    }

    private void handleNavigation() {
        setItems(45, 53, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        setPrevPage(5, 3, new TItemBuilder(Objects.requireNonNull(Settings.GUI_BACK_BTN_ITEM.getMaterial().parseMaterial())).setName(Settings.GUI_BACK_BTN_NAME.getString()).setLore(Settings.GUI_BACK_BTN_LORE.getStringList()).toItemStack());
        setButton(5, 4, new TItemBuilder(Objects.requireNonNull(Settings.GUI_CLOSE_BTN_ITEM.getMaterial().parseMaterial())).setName(Settings.GUI_CLOSE_BTN_NAME.getString()).setLore(Settings.GUI_CLOSE_BTN_LORE.getStringList()).toItemStack(), e -> e.manager.showGUI(e.player, new GUIMain(e.player)));
        setNextPage(5, 5, new TItemBuilder(Objects.requireNonNull(Settings.GUI_NEXT_BTN_ITEM.getMaterial().parseMaterial())).setName(Settings.GUI_NEXT_BTN_NAME.getString()).setLore(Settings.GUI_NEXT_BTN_LORE.getStringList()).toItemStack());
        setAction(5, 3, ClickType.SHIFT_LEFT, e -> changePage(-Settings.GUI_BACK_SHIFT_CLICK_AMT.getInt()));
        setAction(5, 5, ClickType.SHIFT_LEFT, e -> changePage(Settings.GUI_NEXT_SHIFT_CLICK_AMT.getInt()));
        setOnPage(e -> draw());
    }
}
