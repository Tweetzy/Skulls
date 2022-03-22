package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.bukkit.BukkitCommandActor;

/**
 * Date Created: April 01 2022
 * Time Created: 11:19 p.m.
 *
 * @author Kiran Hart
 */
@Command({"skulls"})
public final class SkullsCommand {

	@Default
	public void main(final BukkitCommandActor actor) {
		actor.getSender().sendMessage(Skulls.getInstance().getSkullManager().getSkulls().size() + " loaded");
	}
}
