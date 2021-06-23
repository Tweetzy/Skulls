package ca.tweetzy.skulls.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.inventories.GUISearch;
import ca.tweetzy.skulls.settings.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 12 2021
 * Time Created: 5:59 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@SuppressWarnings("ALL")
public class CommandSearch extends AbstractCommand {

    public CommandSearch() {
        super(CommandType.PLAYER_ONLY, "search");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length == 0) return ReturnType.SYNTAX_ERROR;
        Player player = (Player) sender;

        if (Settings.USE_BLOCK_WORLDS.getBoolean() && Settings.BLOCKED_WORLDS.getStringList().stream().anyMatch(world -> world.equals(player.getWorld().getName()))) {
            Skulls.getInstance().getLocale().getMessage("general.blocked_world").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (Skulls.getInstance().isHeadsDownloading()) {
            Skulls.getInstance().getLocale().getMessage("skull.unavailable").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }

        if (Skulls.getInstance().getSkullManager().search(builder.toString().trim(), Settings.INCLUDE_TAGS_IN_SEARCH.getBoolean()).isEmpty()) {
            Skulls.getInstance().getLocale().getMessage("skull.no_results").processPlaceholder("keyword", builder.toString()).sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        Skulls.getInstance().getGuiManager().showGUI(player, new GUISearch(builder.toString().trim()));
        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "skulls.cmd.search";
    }

    @Override
    public String getSyntax() {
        return "search <keywords>";
    }

    @Override
    public String getDescription() {
        return "Search for a head";
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }
}
