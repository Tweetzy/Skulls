package ca.tweetzy.skulls.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.utils.NumberUtils;
import ca.tweetzy.core.utils.PlayerUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.skull.Skull;
import ca.tweetzy.skulls.skull.SkullCategory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: July 14 2021
 * Time Created: 3:52 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandGiveRandom extends AbstractCommand {

    public CommandGiveRandom() {
        super(CommandType.CONSOLE_OK, "giverandom");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length < 2) return ReturnType.SYNTAX_ERROR;
        Random random = new Random();

        boolean isGiveAll = args[0].equalsIgnoreCase("all");
        Player target = PlayerUtils.findPlayer(args[0]);
        int giveAmount = args.length == 3 && NumberUtils.isInt(args[2]) ? Integer.parseInt(args[2]) : 1;

        SkullCategory.BaseCategory selectedCategory = null;
        for (SkullCategory.BaseCategory baseCategory : SkullCategory.BaseCategory.values()) {
            if (baseCategory.getName().equalsIgnoreCase(args[1]) || baseCategory.name().equalsIgnoreCase(args[1])) {
                selectedCategory = baseCategory;
            }
        }

        // invalid category
        if (selectedCategory == null) {
            Skulls.getInstance().getLocale().getMessage("skull.invalid_category").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        List<ItemStack> skullsToGive = new ArrayList<>();
        List<Skull> categorySkulls = Skulls.getInstance().getSkullManager().getSkulls(selectedCategory);

        for (int i = 0; i < giveAmount; i++) {
            int randomIndex = random.nextInt(categorySkulls.size());
            skullsToGive.add(categorySkulls.get(randomIndex).getItem());
        }

        if (isGiveAll) {
            Bukkit.getOnlinePlayers().forEach(players -> PlayerUtils.giveItem(players, skullsToGive));
            Skulls.getInstance().getLocale().getMessage("skull.gave_random_to_all").processPlaceholder("amount", giveAmount).processPlaceholder("skull_category", selectedCategory.getName()).sendPrefixedMessage(sender);
            return ReturnType.SUCCESS;
        }

        if (target == null) {
            Skulls.getInstance().getLocale().getMessage("skull.player_offline").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        PlayerUtils.giveItem(target, skullsToGive);
        Skulls.getInstance().getLocale().getMessage("skull.gave_random_to_player").processPlaceholder("amount", giveAmount).processPlaceholder("skull_category", selectedCategory.getName()).processPlaceholder("player", target.getName()).sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) {
            List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            players.add("all");
            return players;
        }
        if (args.length == 2)
            return Arrays.stream(SkullCategory.BaseCategory.values()).map(SkullCategory.BaseCategory::getName).map(s -> s.replace(" ", "_")).collect(Collectors.toList());
        if (args.length == 3) return Arrays.asList("1", "2", "3", "4", "5");
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "skulls.cmd.giverandom";
    }

    @Override
    public String getSyntax() {
        return "giverandom <player|all> <category> [#]";
    }

    @Override
    public String getDescription() {
        return "Give a player a random skull from the specified category.";
    }
}
