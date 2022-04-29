package ca.tweetzy.skulls.commands;

import ca.tweetzy.rose.command.AllowedExecutor;
import ca.tweetzy.rose.command.Command;
import ca.tweetzy.rose.command.ReturnType;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.enums.ViewMode;
import ca.tweetzy.skulls.guis.SkullsViewGUI;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Date Created: April 20 2022
 * Time Created: 9:42 p.m.
 *
 * @author Kiran Hart
 */
public final class SearchCommand extends Command {

	public SearchCommand() {
		super(AllowedExecutor.PLAYER, "search");
	}

	@Override
	protected ReturnType execute(CommandSender commandSender, String... args) {
		final Player player = (Player) commandSender;
		if (args.length == 0) return ReturnType.INVALID_SYNTAX;

		final StringBuilder builder = new StringBuilder();
		for (String arg : args) builder.append(" ").append(arg);

		Skulls.getGuiManager().showGUI(player, new SkullsViewGUI(null, Skulls.getPlayerManager().findPlayer(player), builder.toString().trim(), ViewMode.SEARCH));

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender commandSender, String... strings) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "skulls.command.search";
	}

	@Override
	public String getSyntax() {
		return "search <keywords>";
	}

	@Override
	public String getDescription() {
		return "Search for skulls";
	}
}
