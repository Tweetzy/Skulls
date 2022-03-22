package ca.tweetzy.skulls.guis.template;

import ca.tweetzy.tweety.gui.Gui;
import ca.tweetzy.tweety.model.Common;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Date Created: April 02 2022
 * Time Created: 12:22 p.m.
 *
 * @author Kiran Hart
 */
public abstract class View extends Gui {

	public View(@NonNull final String title, int rows) {
		setTitle(Common.colorize(title));
		setRows(rows);
	}

	protected List<Integer> fillSlots() {
		return IntStream.rangeClosed(0, 53).boxed().collect(Collectors.toList());
	}
}
