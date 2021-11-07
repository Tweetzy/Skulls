package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;

/**
 * The current file has been created by Kiran Hart
 * Date Created: November 07 2021
 * Time Created: 11:49 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandReload extends SkullsSubCommand{

	public CommandReload() {
		super("reload|rl");
	}

	@Override
	protected void onCommand() {
		Skulls.getInstance().reload();
		tell("&aSkulls has been reloaded");
	}
}
