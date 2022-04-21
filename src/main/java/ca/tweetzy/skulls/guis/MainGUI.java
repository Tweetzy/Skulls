package ca.tweetzy.skulls.guis;

import ca.tweetzy.rose.gui.template.BaseGUI;
import ca.tweetzy.skulls.settings.Translation;

/**
 * Date Created: April 20 2022
 * Time Created: 9:47 p.m.
 *
 * @author Kiran Hart
 */
public final class MainGUI extends BaseGUI {

	public MainGUI() {
		super(null, Translation.GUI_MAIN_TITLE.getString(), 6);
		draw();
	}

	@Override
	protected void draw() {

	}
}
