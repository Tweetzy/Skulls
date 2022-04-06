package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.settings.Localization;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 26 2021
 * Time Created: 11:49 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandLatest extends SkullsSubCommand {

	private final int insertionNumber = 37;

	public CommandLatest() {
		super("latest");
		setDescription(Localization.Commands.LATEST);
	}

	@Override
	protected void onCommand() {
		tell("https://rose.tweetzy.ca/minecraft/skulls/imagegrid/" + insertionNumber + "");
	}
}
