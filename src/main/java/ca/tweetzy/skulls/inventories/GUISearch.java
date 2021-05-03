package ca.tweetzy.skulls.inventories;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.input.ChatPrompt;
import ca.tweetzy.core.utils.NumberUtils;
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
 * Date Created: March 12 2021
 * Time Created: 4:59 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUISearch extends Gui {

    final String keyword;
    final List<Skull> items;

    public GUISearch(String keyword) {
        this.keyword = keyword;
        this.items = Skulls.getInstance().getSkullManager().search(this.keyword, Settings.INCLUDE_TAGS_IN_SEARCH.getBoolean());
        setTitle(TextUtils.formatText(Settings.GUI_SEARCH_TITLE.getString()));
        setRows(6);
        setAcceptsItems(false);
        draw();
    }

    private void draw() {
        reset();
        pages = (int) Math.max(1, Math.ceil(this.items.size() / (double) 45));

        setTitle(TextUtils.formatText(Settings.GUI_SEARCH_TITLE.getString().replace("%current_page%", String.valueOf(page)).replace("%max_pages%", String.valueOf(pages)).replace("%keyword%", keyword)));
        handleNavigation();

        int slot = 0;
        List<Skull> data = this.items.stream().sorted(Comparator.comparing(Skull::getName)).skip((page - 1) * 45L).limit(45).collect(Collectors.toList());
        for (Skull skull : data) {
            setItem(slot, SkullAPI.getInstance().getTexturedHead(skull.getBase64(), true, Settings.GUI_SEARCH_HEAD_TITLE.getString(), skull.isFavourite() ? Settings.GUI_SEARCH_HEAD_LORE_FAV.getStringList() : Settings.GUI_SEARCH_HEAD_LORE.getStringList(), new HashMap<String, Object>() {{
                put("%head_id%", skull.getUuid().toString());
                put("%head_name%", skull.getName());
                put("%head_tags%", String.join(", ", Arrays.asList(skull.getTags())));
                put("%head_category%", skull.getCategory().getBaseCategory().getName());
                put("%head_price%", skull.getPrice());
            }}));

            setAction(slot, ClickType.RIGHT, e -> {
                if (!e.player.hasPermission("skulls.togglefavourite")) {
                    Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(e.player);
                } else {
                    Skulls.getInstance().getSkullManager().toggleFavouriteSkull(skull.getUuid(), !skull.isFavourite());
                    draw();
                }
            });

            setAction(slot, ClickType.MIDDLE, e -> {
                if (!e.player.hasPermission("skulls.addtocategory")) {
                    Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(e.player);
                } else {
                    if (Skulls.getInstance().getSkullManager().getCategories().stream().anyMatch(SkullCategory::isCustom)) {
                        Skulls.getInstance().getChangingCustomCategoryIcon().remove(e.player.getUniqueId());
                        Skulls.getInstance().getAddingToCategory().put(e.player.getUniqueId(), skull);
                        e.manager.showGUI(e.player, new GUICustomCategoriesList());
                        Skulls.getInstance().getLocale().getMessage("skull.add_to_category").sendPrefixedMessage(e.player);
                    }
                }
            });

            setAction(slot, ClickType.LEFT, e -> {
                if (Skulls.getInstance().getChangingCustomCategoryIcon().containsKey(e.player.getUniqueId())) {
                    Skulls.getInstance().getSkullManager().setCustomCategoryIcon(e.player, skull);
                    e.manager.showGUI(e.player, new GUICustomCategoriesList());
                } else {
                    if (Settings.CHARGE_FOR_HEADS.getBoolean()) {
                        if (e.player.hasPermission("skulls.freeheads")) {
                            PlayerUtils.giveItem(e.player, skull.getItem());
                            return;
                        }

                        if (!Skulls.getInstance().getEconomy().has(e.player, skull.getPrice())) {
                            Skulls.getInstance().getLocale().getMessage("skull.no_money").sendPrefixedMessage(e.player);
                            return;
                        }

                        PlayerUtils.giveItem(e.player, skull.getItem());
                        Skulls.getInstance().getEconomy().withdrawPlayer(e.player, skull.getPrice());
                        Skulls.getInstance().getLocale().getMessage("skull.money_remove").processPlaceholder("price", skull.getPrice()).sendPrefixedMessage(e.player);

                    } else {
                        SkullAPI.getInstance().checkPermissionsBeforeGive(e.player, skull.getItem(), "skulls.takefromgui");
                    }
                }
            });

            setAction(slot, ClickType.SHIFT_RIGHT, e -> {
                if (!e.player.hasPermission("skulls.editprice")) {
                    Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(e.player);
                } else {
                    ChatPrompt.showPrompt(Skulls.getInstance(), e.player, TextUtils.formatText(Skulls.getInstance().getLocale().getMessage("skull.enter_new_price").getMessage()), chat -> {
                        if (NumberUtils.isDouble(chat.getMessage().trim())) {
                            skull.setPrice(Double.parseDouble(chat.getMessage().trim()));
                            Skulls.getInstance().getData().set("price overrides." + skull.getUuid().toString(), Double.parseDouble(chat.getMessage().trim()));
                            Skulls.getInstance().getData().save();
                            Skulls.getInstance().getLocale().getMessage("skull.price_updated").processPlaceholder("price", chat.getMessage().trim()).sendPrefixedMessage(e.player);
                        }
                    }).setOnCancel(() -> e.manager.showGUI(e.player, new GUISearch(this.keyword))).setOnClose(() -> e.manager.showGUI(e.player, new GUISearch(this.keyword)));
                }
            });

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
