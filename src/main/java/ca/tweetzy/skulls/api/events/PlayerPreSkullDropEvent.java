package ca.tweetzy.skulls.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Triggered before a skull has been dropped when a player died.
 * Cancelling this event will prevent the Skull from dropping.
 */
public class PlayerPreSkullDropEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final Location dropLocation;
    private final ItemStack skull;

    public PlayerPreSkullDropEvent(Player player, Location dropLocation, ItemStack skull) {
        this.player = player;
        this.dropLocation = dropLocation;
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
     * Get the location where the item will be dropped (A random offset is added when the item is dropped)
     * @return location
     */
    public Location getDropLocation() {
        return dropLocation;
    }

    /**
     * Get the item-stack (skull) that will be dropped
     * @return item-stack
     */
    public ItemStack getSkull() {
        return skull;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
