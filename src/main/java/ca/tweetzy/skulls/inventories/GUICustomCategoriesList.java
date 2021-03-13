package ca.tweetzy.skulls.inventories;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.input.ChatPrompt;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.core.utils.items.TItemBuilder;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.downloader.MinecraftHeadsLinks;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.SkullCategory;
import org.bukkit.event.inventory.ClickType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 12 2021
 * Time Created: 11:53 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUICustomCategoriesList extends Gui {

    final List<SkullCategory> categories;

    public GUICustomCategoriesList() {
        this.categories = Skulls.getInstance().getSkullManager().getCategories().stream().filter(SkullCategory::isCustom).collect(Collectors.toList());
        setTitle(Settings.GUI_CUSTOM_CATEGORY_LIST_TITLE.getString());
        setRows(6);
        setAcceptsItems(false);
        draw();
    }

    private void draw() {
        reset();
        pages = (int) Math.max(1, Math.ceil(this.categories.size() / (double) 45));

        setTitle(TextUtils.formatText(Settings.GUI_CUSTOM_CATEGORY_LIST_TITLE.getString().replace("%current_page%", String.valueOf(page)).replace("%max_pages%", String.valueOf(pages))));
        handleNavigation();

        if (this.categories.isEmpty()) {
            setAddButton(0, 0);
        } else {
            setAddButton(7, 5);
            List<SkullCategory> data = this.categories.stream().sorted(Comparator.comparing(SkullCategory::getName)).skip((page - 1) * 45L).limit(45).collect(Collectors.toList());

            int slot = 0;
            for (SkullCategory skullCategory : data) {
                setItem(slot, SkullAPI.getInstance().getTexturedHead(
                        MinecraftHeadsLinks.MC_TEXTURE_URL + Skulls.getInstance().getData().getString("custom category." + skullCategory.getName() + ".texture"),
                        false,
                        Settings.GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_TITLE.getString(),
                        Settings.GUI_CUSTOM_CATEGORY_LIST_ITEMS_CATEGORY_LORE.getStringList(),
                        new HashMap<String, Object>(){{
                            put("%category_id%", skullCategory.getName());
                            put("%category_name%", Skulls.getInstance().getData().getString("custom category." + skullCategory.getName() + ".display name"));
                            put("%head_count%", Skulls.getInstance().getData().getStringList("custom category." + skullCategory.getName()).size());
                        }}
                ));

                setAction(slot, ClickType.LEFT, e -> {
                    if (Skulls.getInstance().getAddingToCategory().containsKey(e.player.getUniqueId())) {
                        Skulls.getInstance().getSkullManager().toggleSkullCustomCategory(skullCategory.getName(), Skulls.getInstance().getAddingToCategory().get(e.player.getUniqueId()), true);
                        Skulls.getInstance().getAddingToCategory().remove(e.player.getUniqueId());
                    }

                    e.manager.showGUI(e.player, new GUICustomCategoryList(skullCategory.getName()));
                });

                setAction(slot, ClickType.MIDDLE, e -> {
                    if (!e.player.hasPermission("skulls.changecategoryicon")) {
                        Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(e.player);
                    } else {
                        Skulls.getInstance().getChangingCustomCategoryIcon().put(e.player.getUniqueId(), skullCategory);
                        Skulls.getInstance().getAddingToCategory().remove(e.player.getUniqueId());
                        Skulls.getInstance().getLocale().getMessage("skull.category_icon_change").sendPrefixedMessage(e.player);
                        e.manager.showGUI(e.player, new GUIMain(e.player));
                    }
                });

                setAction(slot, ClickType.SHIFT_RIGHT, e -> {
                   if (!e.player.hasPermission("skulls.removecategory")) {
                       Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(e.player);
                   } else {
                       SkullAPI.getInstance().removeCustomCategory(skullCategory.getName());
                       e.manager.showGUI(e.player, new GUICustomCategoriesList());
                   }
                });

                setAction(slot, ClickType.RIGHT, e -> {
                    if (!e.player.hasPermission("skulls.renamecategory")) {
                        Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(e.player);
                    } else {
                        ChatPrompt.showPrompt(Skulls.getInstance(), e.player, Skulls.getInstance().getLocale().getMessage("skull.category_name_ask").getMessage(), chat -> {
                            if (!chat.getMessage().isEmpty()) {
                                Skulls.getInstance().getData().set("custom category." + skullCategory.getName().toLowerCase() + ".display name", chat.getMessage());
                                Skulls.getInstance().getData().save();
                                e.manager.showGUI(e.player, new GUICustomCategoriesList());
                            }
                        }).setOnClose(() -> e.manager.showGUI(e.player, this)).setOnCancel(() -> e.manager.showGUI(e.player, this));
                    }
                });

                slot++;
            }
        }
    }

    private void setAddButton(int col, int row) {
        setButton(row, col, SkullAPI.getInstance().getTexturedHead(MinecraftHeadsLinks.MC_TEXTURE_URL + Settings.GUI_CUSTOM_CATEGORY_LIST_ITEMS_ADD_TEXTURE.getString(), false, Settings.GUI_CUSTOM_CATEGORY_LIST_ITEMS_ADD_TITLE.getString(), Settings.GUI_CUSTOM_CATEGORY_LIST_ITEMS_ADD_LORE.getStringList(), null), ClickType.LEFT, e -> {
            if (e.player.hasPermission("skulls.createcategory")) {
                ChatPrompt.showPrompt(Skulls.getInstance(), e.player, Skulls.getInstance().getLocale().getMessage("skull.category_name_ask").getMessage(), chat -> {
                    if (!chat.getMessage().isEmpty()) {
                        if (SkullAPI.getInstance().doesCustomCategoryExists(chat.getMessage().replace(" ", ""))) {
                            Skulls.getInstance().getLocale().getMessage("skull.category_name_taken").sendPrefixedMessage(e.player);
                        } else {
                            SkullAPI.getInstance().createCustomCategory(chat.getMessage());
                            Skulls.getInstance().getLocale().getMessage("skull.category_created").processPlaceholder("category_id", chat.getMessage().toLowerCase().replace(" ", "")).sendPrefixedMessage(e.player);
                            e.manager.showGUI(e.player, new GUICustomCategoriesList());
                        }
                    }
                }).setOnClose(() -> e.manager.showGUI(e.player, this)).setOnCancel(() -> e.manager.showGUI(e.player, this));
            } else {
                Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(e.player);
            }
        });
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
