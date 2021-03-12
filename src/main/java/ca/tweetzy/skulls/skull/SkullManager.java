package ca.tweetzy.skulls.skull;

import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.downloader.HeadDownloader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 10 2021
 * Time Created: 6:35 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class SkullManager {

    private final ArrayList<Skull> skulls = new ArrayList<>();
    private final HashSet<SkullCategory> categories = new HashSet<>();

    public Skull addSkull(Skull skull) {
        this.skulls.add(skull);
        return skull;
    }

    public SkullCategory addSkullCategory(SkullCategory skullCategory) {
        this.categories.add(skullCategory);
        return skullCategory;
    }

    public SkullCategory[] addSkullCategories(SkullCategory... skullCategories) {
        this.categories.addAll(Arrays.asList(skullCategories));
        return skullCategories;
    }

    public Skull getSkull(String name) {
        return this.skulls.stream().filter(skull -> skull.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Skull> getSkulls(SkullCategory.BaseCategory baseCategory) {
        return Collections.unmodifiableList(this.skulls.stream().filter(skull -> skull.getCategory().getBaseCategory() == baseCategory).sorted(Comparator.comparing(Skull::getName)).collect(Collectors.toList()));
    }

    public List<Skull> getSkulls(SkullCategory category) {
        return Collections.unmodifiableList(this.skulls.stream().filter(skull -> skull.getCategory() == category).sorted(Comparator.comparing(Skull::getName)).collect(Collectors.toList()));
    }

    public List<Skull> getSkulls() {
        return Collections.unmodifiableList(this.skulls);
    }

    public Set<SkullCategory> getCategories() {
        return Collections.unmodifiableSet(this.categories);
    }

    public SkullCategory getCategory(SkullCategory.BaseCategory baseCategory, boolean isCustom) {
        return getCategories().stream().filter(cat -> cat.getBaseCategory() == baseCategory && cat.isCustom() == isCustom).findFirst().orElse(null);
    }

    public List<Skull> search(String keyword, boolean includeTags) {
        return getSkulls().stream().filter(skull -> match(keyword, skull.getName()) || Arrays.stream(skull.getTags()).anyMatch(tag -> match(keyword, tag)) && includeTags).collect(Collectors.toList());
    }

    public void toggleFavouriteSkull(UUID skullID, boolean isFavourite) {
        if (!Skulls.getInstance().getData().contains("favourite skulls") && isFavourite) {
            Skulls.getInstance().getData().set("favourite skulls", Collections.singletonList(skullID.toString()));
            Skulls.getInstance().getData().save();
            this.skulls.stream().filter(skull -> skull.getUuid().equals(skullID)).findFirst().orElse(null).setFavourite(true);
            return;
        }

        List<UUID> ids =  Skulls.getInstance().getData().getStringList("favourite skulls").stream().map(UUID::fromString).collect(Collectors.toList());
        if (ids.stream().anyMatch(id -> id.equals(skullID)) && !isFavourite) {
            ids.removeIf(id -> id.equals(skullID));
            this.skulls.stream().filter(skull -> skull.getUuid().equals(skullID)).findFirst().orElse(null).setFavourite(false);
        } else {
            ids.add(skullID);
            this.skulls.stream().filter(skull -> skull.getUuid().equals(skullID)).findFirst().orElse(null).setFavourite(true);
        }

        Skulls.getInstance().getData().set("favourite skulls", ids.stream().map(UUID::toString).collect(Collectors.toList()));
        Skulls.getInstance().getData().save();
    }

    public void clearTemporaryStorage() {
        skulls.clear();
        categories.clear();
    }

    private boolean match(String pattern, String sentence) {
        Pattern patt = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patt.matcher(sentence);
        return matcher.find();
    }
}
