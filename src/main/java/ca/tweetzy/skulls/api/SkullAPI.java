package ca.tweetzy.skulls.api;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.utils.PlayerUtils;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.downloader.MinecraftHeadsLinks;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.SkullCategory;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 11 2021
 * Time Created: 7:17 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@SuppressWarnings("deprecation")
public class SkullAPI {

    private SkullAPI() {
    }

    private static SkullAPI instance;

    public static SkullAPI getInstance() {
        if (instance == null) {
            instance = new SkullAPI();
        }
        return instance;
    }

    /**
     * Create a custom textured skull
     *
     * @param texture is the texture URL (http://textures.minecraft.net/texture/???) or the base64 encoded value
     * @param base64 is the entered texture a base64 string?
     * @return the created head
     */
    public ItemStack getCustomTextureHead(String texture, boolean base64) {
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) Objects.requireNonNull(head).getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        if (base64) {
            profile.getProperties().put("textures", new Property("textures", texture));
        } else {
            byte[] encoded = Base64.getEncoder().encode(String.format("{\"textures\": {\"SKIN\": {\"url\": \"%s\"}}}", texture).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encoded)));
        }
        Field profileField;
        try {
            profileField = Objects.requireNonNull(meta).getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    /**
     * Used to create a custom head w/placeholder optional replacements
     *
     * @param texture is the base64 string or the URL to the texture
     * @param name is the new name of the item
     * @param lore the lore you want to give to the item
     * @param replacements is any replacements you want done, can be null.
     * @return the head w/replacements
     */
    public ItemStack getTexturedHead(String texture, boolean base64, String name, List<String> lore, HashMap<String, Object> replacements) {
        ItemStack stack = getCustomTextureHead(texture, base64);
        ItemMeta meta = stack.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(name));

        if (replacements != null) {
            for (String key : replacements.keySet()) {
                if (name.contains(key)) name = name.replace(key, String.valueOf(replacements.get(key)));
            }

            for (int i = 0; i < lore.size(); i++) {
                for (String key : replacements.keySet()) {
                    if (lore.get(i).contains(key)) lore.set(i, lore.get(i).replace(key, String.valueOf(replacements.get(key))));
                }
            }
        }

        meta.setDisplayName(TextUtils.formatText(name));
        meta.setLore(lore.stream().map(TextUtils::formatText).collect(Collectors.toList()));
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Get a basic player head
     *
     * @param name is the name of the player
     * @return the player's head.
     */
    public ItemStack getPlayerHead(String name) {
        ItemStack stack = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) Objects.requireNonNull(stack).getItemMeta();
        Objects.requireNonNull(meta).setOwner(name);
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Used to create a player head for a real player
     *
     * @param name is the name of the player
     * @param title the name of the item stack
     * @param lore the lore of the item stack
     * @return player head
     */
    public ItemStack getPlayerHead(String name, String title, List<String> lore, HashMap<String, Object> replacements) {
        ItemStack stack = getPlayerHead(name);
        ItemMeta meta = stack.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(title));

        if (replacements != null) {
            for (String key : replacements.keySet()) {
                if (title.contains(key)) title = title.replace(key, String.valueOf(replacements.get(key)));
            }

            for (int i = 0; i < lore.size(); i++) {
                for (String key : replacements.keySet()) {
                    if (lore.get(i).contains(key)) lore.set(i, lore.get(i).replace(key, String.valueOf(replacements.get(key))));
                }
            }
        }

        meta.setDisplayName(TextUtils.formatText(title));
        meta.setLore(lore.stream().map(TextUtils::formatText).collect(Collectors.toList()));
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Used to create the base category icons, this is really just meant
     * to be used by Skulls, but if you have a use case, go for it.
     *
     * @param baseCategory Is the primary category type your getting
     * @param useCustomHead Should the item be a custom skull or a default mc item.
     * @return the category item.
     */
    public ItemStack getBaseCategoryIcon(SkullCategory.BaseCategory baseCategory, boolean useCustomHead) {
        ItemStack stack = XMaterial.PLAYER_HEAD.parseItem();
        ItemMeta meta = null;
        String LINK_PREFIX = "https://textures.minecraft.net/texture/";

        switch(baseCategory){
            case ALPHABET:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case ANIMALS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case BLOCKS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_BLOCK_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_BLOCK_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_BLOCK_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_BLOCK_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case DECORATION:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_DECORATION_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_DECORATION_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_DECORATION_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_DECORATION_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case FOOD_AND_DRINKS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_FOODS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_FOODS_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_FOODS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_FOODS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case HUMANS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_HUMANS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_HUMANS_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_HUMANS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_HUMANS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case HUMANOID:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case MISCELLANEOUS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case MONSTERS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case PLANTS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_PLANTS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_PLANTS_ITEM.getMaterial().parseItem();
                meta = Objects.requireNonNull(stack).getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_PLANTS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_PLANTS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
        }

        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Used to give a head to a player if they have the required permissions
     *
     * @param player is the player being given the head
     * @param item is the item stack
     * @param permissions is the list of permissions to check
     */
    public void checkPermissionsBeforeGive(Player player, ItemStack item, String... permissions) {
        boolean hasPerms = true;
        for (String perm : permissions) {
            if (!player.hasPermission(perm)) hasPerms = false;
        }

        if (!hasPerms) {
            Skulls.getInstance().getLocale().getMessage("skull.no_permission").sendPrefixedMessage(player);
            return;
        }

        PlayerUtils.giveItem(player, item);
    }

    public boolean anyCustomCategories() {
        return Skulls.getInstance().getData().contains("custom category") && Objects.requireNonNull(Skulls.getInstance().getData().getConfigurationSection("custom category")).getKeys(false).size() != 0;
    }

    public boolean doesCustomCategoryExists(String name) {
        return Skulls.getInstance().getData().contains("custom category." + name.toLowerCase());
    }

    public void createCustomCategory(String name) {
        if (doesCustomCategoryExists(name)) return;
        String id = name.toLowerCase().replace(" ", "");
        Skulls.getInstance().getData().set("custom category." + id + ".id", id);
        Skulls.getInstance().getData().set("custom category." + id + ".display name", "&e" + name);
        Skulls.getInstance().getData().set("custom category." + id + ".texture", MinecraftHeadsLinks.PAPER_STACK);
        Skulls.getInstance().getData().set("custom category." + id + ".items", Collections.emptyList());
        Skulls.getInstance().getData().save();
        Skulls.getInstance().getSkullManager().addSkullCategory(new SkullCategory(id));
    }

    public void removeCustomCategory(String id) {
        if (Skulls.getInstance().getSkullManager().removeCustomCategory(id)) {
            Skulls.getInstance().getData().set("custom category." + id.toLowerCase(), null);
            Skulls.getInstance().getData().save();
        }
    }
}
