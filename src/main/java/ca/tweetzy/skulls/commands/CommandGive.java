package ca.tweetzy.skulls.commands;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 23 2021
 * Time Created: 2:28 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandGive extends SkullsSubCommand{

	public CommandGive() {
		super("give|g");
		setMinArguments(1);
		setUsage("<id/random> [#] [player]");
	}

	@Override
	protected void onCommand() {

	}
}
