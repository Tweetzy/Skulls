package ca.tweetzy.skulls.skull;

import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.downloader.HeadDownloader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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

    public void clearTemporaryStorage() {
        skulls.clear();
        categories.clear();
    }
}
