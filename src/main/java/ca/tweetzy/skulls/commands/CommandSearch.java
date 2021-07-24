package ca.tweetzy.skulls.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.utils.NumberUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.SkullAPI;
import ca.tweetzy.skulls.inventories.GUISearch;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.skull.Skull;
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
        int headId = -1;
        boolean idLookup = false;

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
            if (arg.contains("id:") && NumberUtils.isInt(arg.split("id:")[1])) {
                idLookup = true;
                headId = Integer.parseInt(arg.split("id:")[1]);
            } else {
                builder.append(arg).append(" ");
            }
        }

        if (!idLookup) {
            if (Skulls.getInstance().getSkullManager().search(builder.toString().trim(), Settings.INCLUDE_TAGS_IN_SEARCH.getBoolean()).isEmpty()) {
                Skulls.getInstance().getLocale().getMessage("skull.no_results").processPlaceholder("keyword", builder.toString()).sendPrefixedMessage(player);
                return ReturnType.FAILURE;
            }

            Skulls.getInstance().getGuiManager().showGUI(player, new GUISearch(builder.toString().trim()));
            return ReturnType.SUCCESS;
        }

        int finalHeadId = headId;
        if (Skulls.getInstance().getSkullManager().getSkulls().stream().anyMatch(skull -> skull.getWebsiteId() == finalHeadId)) {
            Skulls.getInstance().getGuiManager().showGUI(player, new GUISearch(finalHeadId+""));
            return ReturnType.SUCCESS;
        }
        Skulls.newChain().asyncFirst(() -> SkullAPI.getInstance().searchHeadDatabaseId(finalHeadId)).syncLast((texture) -> {
            if (texture == null) {
                Skulls.getInstance().getLocale().getMessage("skull.no_results_id").processPlaceholder("id", finalHeadId).sendPrefixedMessage(player);
                return;
            }

            Skull skull = Skulls.getInstance().getSkullManager().getSkulls().stream().filter(all -> all.getBase64().equals(texture)).findFirst().orElse(null);
            if (skull == null) {
                Skulls.getInstance().getLocale().getMessage("skull.no_results_id").processPlaceholder("id", finalHeadId).sendPrefixedMessage(player);
                return;
            }

            skull.setWebsiteId(finalHeadId);
            Skulls.getInstance().getGuiManager().showGUI(player, new GUISearch(finalHeadId+""));

        }).execute();

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "skulls.cmd.search";
    }

    @Override
    public String getSyntax() {
        return "search <keywords> / id:head-id";
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
