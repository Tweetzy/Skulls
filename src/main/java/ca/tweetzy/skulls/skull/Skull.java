package ca.tweetzy.skulls.skull;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.settings.Settings;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 10 2021
 * Time Created: 6:35 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */

@Setter
@Getter
public class Skull {

    private UUID uuid;
    private String name;
    private String base64;
    private String texture;
    private SkullCategory category;
    private String[] tags;
    private boolean isFavourite;

    public Skull(String name, UUID uuid, String base64, String texture, SkullCategory category, String[] tags, boolean isFavourite) {
        this.name = name;
        this.uuid = uuid;
        this.base64 = base64;
        this.texture = texture;
        this.category = category;
        this.tags = tags;
        this.isFavourite = isFavourite;
    }

    public ItemStack getItem() {
        ItemStack item = SkullAPI.getInstance().getCustomTextureHead(this.base64, true);
        if (item == null) return XMaterial.PLAYER_HEAD.parseItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TextUtils.formatText(this.name));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getDisplayItem() {
        ItemStack stack = getItem();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(TextUtils.formatText(Settings.GUI_CATEGORY_HEAD_TITLE.getString().replace("%head_name%", this.name)));
        meta.setLore(Settings.GUI_CATEGORY_HEAD_LORE.getStringList().stream().map(line -> TextUtils.formatText(
                line.replace("%head_id%", uuid.toString()).replace("%head_tags%", String.join(",", Arrays.asList(tags)))
        )).collect(Collectors.toList()));
        stack.setItemMeta(meta);
        return stack;
    }
}
