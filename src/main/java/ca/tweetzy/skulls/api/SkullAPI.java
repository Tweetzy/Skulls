package ca.tweetzy.skulls.api;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.SkullCategory;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 11 2021
 * Time Created: 7:17 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
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

    private Field skullMetaProfile;

    /**
     * Create a custom textured skull
     *
     * @param texture is the texture URL (http://textures.minecraft.net/texture/???) or the base64 encoded value
     * @param base64 is the entered texture a base64 string?
     * @return the created head
     */
    public ItemStack getCustomTextureHead(String texture, boolean base64) {
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        if (base64) {
            profile.getProperties().put("textures", new Property("textures", texture));
        } else {
            byte[] encoded = Base64.getEncoder().encode(String.format("{\"textures\": {\"SKIN\": {\"url\": \"%s\"}}}", texture).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encoded)));
        }
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public ItemStack getBaseCategoryIcon(SkullCategory.BaseCategory baseCategory, boolean useCustomHead) {
        ItemStack stack = XMaterial.PLAYER_HEAD.parseItem();
        ItemMeta meta = null;
        String LINK_PREFIX = "https://textures.minecraft.net/texture/";

        switch(baseCategory){
            case ALPHABET:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_ALPHABET_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case ANIMALS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_ANIMAL_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case BLOCKS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_BLOCK_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_BLOCK_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_BLOCK_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_BLOCK_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case DECORATION:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_DECORATION_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_DECORATION_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_DECORATION_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_DECORATION_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case FOOD_AND_DRINKS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_FOODS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_FOODS_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_FOODS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_FOODS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case HUMANS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_HUMANS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_HUMANS_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_HUMANS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_HUMANS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case HUMANOID:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_HUMANOID_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case MISCELLANEOUS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_MISCELLANEOUS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case MONSTERS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_MONSTERS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
            case PLANTS:
                stack = useCustomHead ? getCustomTextureHead(LINK_PREFIX + Settings.GUI_MAIN_GUI_ITEMS_PLANTS_TEXTURE.getString(), false) : Settings.GUI_MAIN_GUI_ITEMS_PLANTS_ITEM.getMaterial().parseItem();
                meta = stack.getItemMeta();
                meta.setDisplayName(TextUtils.formatText(Settings.GUI_MAIN_GUI_ITEMS_PLANTS_NAME.getString()));
                meta.setLore(Settings.GUI_MAIN_GUI_ITEMS_PLANTS_LORE.getStringList().stream().map(line -> TextUtils.formatText(line.replace("%head_count%", String.valueOf(Skulls.getInstance().getSkullManager().getSkulls(baseCategory).size())))).collect(Collectors.toList()));
                break;
        }

        stack.setItemMeta(meta);
        return stack;
    }
}
