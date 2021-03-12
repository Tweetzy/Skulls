package ca.tweetzy.skulls.inventories;

import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.SkullCategory;
import org.bukkit.event.inventory.ClickType;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 11 2021
 * Time Created: 9:46 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUIMain extends Gui {

    public GUIMain() {
        setTitle(TextUtils.formatText(Settings.GUI_MAIN_GUI_TITLE.getString()));
        setRows(6);
        setAcceptsItems(false);
        setDefaultItem(Settings.GUI_MAIN_GUI_ITEMS_BG_ITEM.getMaterial().parseItem());

        // use bg?
        if (Settings.GUI_MAIN_GUI_FILL_BACKGROUND.getBoolean()) {
            setItems(0, 53, getDefaultItem());
        }

        setButton(1, 2, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.ALPHABET, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.ALPHABET)));
        setButton(1, 3, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.ANIMALS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.ANIMALS)));
        setButton(1, 4, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.BLOCKS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.BLOCKS)));
        setButton(1, 5, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.DECORATION, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.DECORATION)));
        setButton(1, 6, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.FOOD_AND_DRINKS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.FOOD_AND_DRINKS)));
        setButton(2, 2, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.HUMANS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.HUMANS)));
        setButton(2, 3, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.HUMANOID, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.HUMANOID)));
        setButton(2, 4, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.MISCELLANEOUS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.MISCELLANEOUS)));
        setButton(2, 5, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.MONSTERS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.MONSTERS)));
        setButton(2, 6, SkullAPI.getInstance().getBaseCategoryIcon(SkullCategory.BaseCategory.PLANTS, Settings.GUI_MAIN_CATEGORY_USE_CUSTOM_SKULL.getBoolean()), e -> e.manager.showGUI(e.player, new GUICategoryList(SkullCategory.BaseCategory.PLANTS)));
    }
}
