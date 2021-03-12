package ca.tweetzy.skulls.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.inventories.GUIMain;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 10 2021
 * Time Created: 6:14 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandSkulls extends AbstractCommand {

    public CommandSkulls() {
        super(CommandType.CONSOLE_OK, "skulls");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (!(sender instanceof Player)) {
            // just send the console any commands that are console ok.
            Skulls.getInstance().getCommandManager().getAllCommands().stream().filter(command -> !command.isNoConsole()).forEach(command -> sender.sendMessage(TextUtils.formatText(String.format("&8- &e%s &7- %s", command.getSyntax(), command.getDescription()))));
        } else {
            // Open the inventory
            Player player = (Player) sender;
            Skulls.getInstance().getGuiManager().showGUI(player, new GUIMain());
        }
        return ReturnType.SUCCESS;
    }


    @Override
    public String getPermissionNode() {
        return "skulls.cmd";
    }

    @Override
    public String getSyntax() {
        return "/skulls";
    }

    @Override
    public String getDescription() {
        return "Open the heads gui";
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }
}
