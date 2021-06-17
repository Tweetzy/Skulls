package ca.tweetzy.skulls.inventories;

import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.gui.events.GuiClickEvent;
import ca.tweetzy.core.input.ChatPrompt;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.downloader.MinecraftHeadsLinks;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.SkullCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 11 2021
 * Time Created: 9:46 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@SuppressWarnings("unused")
public class GUIMain extends Gui {

    final Player player;

    public GUIMain(Player player) {
        this.player = player;
        setTitle(TextUtils.formatText(Settings.GUI_MAIN_GUI_TITLE.getString()));
        setRows(6);
        setAcceptsItems(false);
        setDefaultItem(Settings.GUI_MAIN_GUI_ITEMS_BG_ITEM.getMaterial().parseItem());

        // use bg?
        if (Settings.GUI_MAIN_GUI_FILL_BACKGROUND.getBoolean()) {
            setItems(0, 53, getDefaultItem());
        }

        setButton(1, 2, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.ALPHABET, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(1, 3, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.ANIMALS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(1, 4, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.BLOCKS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(1, 5, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.DECORATION, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(1, 6, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.FOOD_AND_DRINKS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(2, 2, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.HUMANS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(2, 3, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.HUMANOID, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(2, 4, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.MISCELLANEOUS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(2, 5, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.MONSTERS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);
        setButton(2, 6, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.PLANTS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), this::handleClick);

        // additional
        setButton(4, 2, SkullAPI.getInstance().getPlayerHead(player.getName(), Settings.GUI_MAIN_GUI_ITEMS_PLAYERS_NAME.getString(), Settings.GUI_MAIN_GUI_ITEMS_PLAYERS_LORE.getStringList(), null), this::handleClick);
        setButton(4, 3, SkullAPI.getInstance().getTexturedHead(MinecraftHeadsLinks.MC_TEXTURE_URL + Settings.GUI_MAIN_GUI_ITEMS_CUSTOM_CATEGORIES_TEXTURE.getString(), false, Settings.GUI_MAIN_GUI_ITEMS_CUSTOM_CATEGORIES_NAME.getString(), Settings.GUI_MAIN_GUI_ITEMS_CUSTOM_CATEGORIES_LORE.getStringList(), null), this::handleClick);

        setButton(4, 5, SkullAPI.getInstance().getTexturedHead(MinecraftHeadsLinks.MC_TEXTURE_URL + Settings.GUI_MAIN_GUI_ITEMS_FAVOURITES_TEXTURE.getString(), false, Settings.GUI_MAIN_GUI_ITEMS_FAVOURITES_NAME.getString(), Settings.GUI_MAIN_GUI_ITEMS_FAVOURITES_LORE.getStringList(), null), this::handleClick);
        setButton(4, 6, SkullAPI.getInstance().getTexturedHead(MinecraftHeadsLinks.MC_TEXTURE_URL + Settings.GUI_MAIN_GUI_ITEMS_SEARCH_TEXTURE.getString(), false, Settings.GUI_MAIN_GUI_ITEMS_SEARCH_NAME.getString(), Settings.GUI_MAIN_GUI_ITEMS_SEARCH_LORE.getStringList(), null), ClickType.LEFT, e -> ChatPrompt.showPrompt(Skulls.getInstance(), e.player, Skulls.getInstance().getLocale().getMessage("skull.search_ask").getMessage(), chat -> {
            if (!Skulls.getInstance().getSkullManager().search(chat.getMessage(), Settings.INCLUDE_TAGS_IN_SEARCH.getBoolean()).isEmpty()) {
                e.manager.showGUI(e.player, new GUISearch(chat.getMessage()));
            } else {
                Skulls.getInstance().getLocale().getMessage("skull.no_results").processPlaceholder("keyword", chat.getMessage()).sendPrefixedMessage(e.player);
            }
        }).setOnClose(() -> e.manager.showGUI(e.player, this)).setOnCancel(() -> e.manager.showGUI(e.player, this)));
    }

    private void handleClick(GuiClickEvent e) {
        switch (e.slot) {
            case 11:
                showMainCategory(e, SkullCategory.BaseCategory.ALPHABET);
                break;
            case 12:
                showMainCategory(e, SkullCategory.BaseCategory.ANIMALS);
                break;
            case 13:
                showMainCategory(e, SkullCategory.BaseCategory.BLOCKS);
                break;
            case 14:
                showMainCategory(e, SkullCategory.BaseCategory.DECORATION);
                break;
            case 15:
                showMainCategory(e, SkullCategory.BaseCategory.FOOD_AND_DRINKS);
                break;
            case 20:
                showMainCategory(e, SkullCategory.BaseCategory.HUMANS);
                break;
            case 21:
                showMainCategory(e, SkullCategory.BaseCategory.HUMANOID);
                break;
            case 22:
                showMainCategory(e, SkullCategory.BaseCategory.MISCELLANEOUS);
                break;
            case 23:
                showMainCategory(e, SkullCategory.BaseCategory.MONSTERS);
                break;
            case 24:
                showMainCategory(e, SkullCategory.BaseCategory.PLANTS);
                break;
            case 38:
                showSpecialCategory(e, "playerheads");
                break;
            case 39:
                showSpecialCategory(e, "customs");
                break;
            case 41:
                showSpecialCategory(e, "favourites");
                break;
        }
    }

    private boolean checkPermission(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(player);
            return false;
        }

        return true;
    }

    private void showMainCategory(GuiClickEvent e, SkullCategory.BaseCategory category) {
        if (Settings.REQUIRE_PERMISSIONS_FOR_CATEGORY.getBoolean()) {
            if (!checkPermission(e.player, "skulls.category." + category.getName().replace(" ", "").toLowerCase())) {
                return;
            }
        }

        e.manager.showGUI(e.player, new GUICategoryList(category));
    }

    private void showSpecialCategory(GuiClickEvent e, String special) {
        switch (special) {
            case "playerheads":
                if (Settings.REQUIRE_PERMISSIONS_FOR_CATEGORY.getBoolean()) {
                    if (!checkPermission(e.player, "skulls.category.playerheads")) {
                        return;
                    }
                }

                e.manager.showGUI(e.player, new GUIPlayerHeads());
                break;
            case "favourites":
                if (Settings.REQUIRE_PERMISSIONS_FOR_CATEGORY.getBoolean()) {
                    if (checkPermission(e.player, "skulls.category.favourites")) {
                        return;
                    }
                }

                e.manager.showGUI(e.player, new GUIFavourites());
                break;
            case "customs":
                if (Settings.REQUIRE_PERMISSIONS_FOR_CATEGORY.getBoolean()) {
                    if (checkPermission(e.player, "skulls.category.customs")) {
                        return;
                    }
                }

                e.manager.showGUI(e.player, new GUICustomCategoriesList());
                break;
        }
    }
}
