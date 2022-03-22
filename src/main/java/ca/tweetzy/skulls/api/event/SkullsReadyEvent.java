package ca.tweetzy.skulls.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Date Created: March 22 2022
 * Time Created: 1:05 p.m.
 *
 * @author Kiran Hart
 */
public final class SkullsReadyEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	public SkullsReadyEvent() {
		super(true);
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
