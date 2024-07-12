package ca.tweetzy.skulls.api.events;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Triggered after a skull has been dropped when a player died.
 */
public class PlayerSkullDropEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final Item skull;

    public PlayerSkullDropEvent(Player player, Item skull) {
        this.player = player;
        this.skull = skull;
    }

    /**
     * Get the dead player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the item (skull) that was dropped
     * @return item
     */
    public Item getSkull() {
        return skull;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
