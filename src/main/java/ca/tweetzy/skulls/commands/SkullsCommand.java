package ca.tweetzy.skulls.commands;

import ca.tweetzy.rose.command.AllowedExecutor;
import ca.tweetzy.rose.command.Command;
import ca.tweetzy.rose.command.ReturnType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Date Created: April 20 2022
 * Time Created: 9:42 p.m.
 *
 * @author Kiran Hart
 */
public final class SkullsCommand extends Command {

	public SkullsCommand() {
		super(AllowedExecutor.BOTH, "skulls");
	}

	@Override
	protected ReturnType execute(CommandSender commandSender, String... strings) {
		if (commandSender instanceof Player) {
			final Player player = (Player) commandSender;


		}

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender commandSender, String... strings) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "skulls.command";
	}

	@Override
	public String getSyntax() {
		return null;
	}

	@Override
	public String getDescription() {
		return "The main command for the plugin";
	}
}
