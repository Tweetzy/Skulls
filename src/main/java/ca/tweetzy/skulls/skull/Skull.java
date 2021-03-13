package ca.tweetzy.skulls.skull;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.api.SkullAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.UUID;

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
        Objects.requireNonNull(meta).setDisplayName(TextUtils.formatText(this.name));
        item.setItemMeta(meta);
        return item;
    }
}
