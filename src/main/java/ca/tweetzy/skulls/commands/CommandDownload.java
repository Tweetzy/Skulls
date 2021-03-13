package ca.tweetzy.skulls.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 12 2021
 * Time Created: 8:38 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@SuppressWarnings("unused")
public class CommandDownload extends AbstractCommand {

    public CommandDownload() {
        super(CommandType.CONSOLE_OK, "download");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {

        if (Skulls.getInstance().isHeadsDownloading()) {
            Skulls.getInstance().getLocale().getMessage("skull.download_in_progress").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (args.length == 0) {
            Skulls.getInstance().getLocale().getMessage("skull.download_confirm").sendPrefixedMessage(sender);
            Skulls.getInstance().getLocale().newMessage(TextUtils.formatText("&e/skulls download confirm")).sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            Skulls.getInstance().downloadHeads(sender);
        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) return Collections.singletonList("confirm");
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "skulls.cmd.download";
    }

    @Override
    public String getSyntax() {
        return "download";
    }

    @Override
    public String getDescription() {
        return "Used to re-download heads.";
    }
}
