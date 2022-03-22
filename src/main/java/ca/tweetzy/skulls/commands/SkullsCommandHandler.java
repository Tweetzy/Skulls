package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import revxrsal.commands.bukkit.BukkitCommandHandler;

/**
 * Date Created: April 01 2022
 * Time Created: 11:18 p.m.
 *
 * @author Kiran Hart
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkullsCommandHandler {

	@Getter
	private static final SkullsCommandHandler instance = new SkullsCommandHandler();

	private final BukkitCommandHandler commandHandler = BukkitCommandHandler.create(Skulls.getInstance());

	public void register() {
		commandHandler.register(new SkullsCommand());
	}
}
