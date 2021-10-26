package ca.tweetzy.skulls.commands;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.tweety.Common;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 25 2021
 * Time Created: 11:42 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandDownload extends SkullsSubCommand{

	public CommandDownload() {
		super("download|dl|update");
	}

	@Override
	protected void onCommand() {
		tell("&aSkulls will now begin to redownload all previous and any new heads if available. You will be told when its finished.");
		Common.runAsync(() -> Skulls.getSkullManager().downloadHeads(true));
	}
}
