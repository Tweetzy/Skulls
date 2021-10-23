package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.tweety.command.SimpleSubCommand;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 19 2021
 * Time Created: 5:57 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public abstract class SkullsSubCommand extends SimpleSubCommand {

	public SkullsSubCommand(String sublabel) {
		super(Skulls.getInstance().getMainCommand(), sublabel);
	}
}
