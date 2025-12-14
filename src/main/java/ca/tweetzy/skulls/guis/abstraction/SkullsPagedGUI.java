/*
 * Auction House
 * Copyright 2023 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.guis.abstraction;

import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.template.BaseGUI;
import ca.tweetzy.flight.hooks.PlaceholderAPIHook;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.settings.Settings;
import ca.tweetzy.skulls.settings.Translations;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SkullsPagedGUI<T> extends BaseGUI {

	@Getter
	protected final Player player;
	protected final Gui parent;
	protected List<T> items;

	@Setter
	protected boolean async = false;

	public SkullsPagedGUI(Gui parent, @NonNull final Player player, @NonNull String title, int rows, @NonNull List<T> items) {
		super(parent, PlaceholderAPIHook.tryReplace(player, title), rows);
		this.parent = parent;
		this.player = player;
		// Only wrap in ArrayList if it's not already a mutable list
		this.items = (items instanceof ArrayList) ? items : new ArrayList<>(items);
	}

	public SkullsPagedGUI(@NonNull final Player player, @NonNull String title, int rows, @NonNull List<T> items) {
		this(null, player, title, rows, items);
	}

	@Override
	protected void draw() {
		// Preserve page number before reset (reset() sets page = 1)
		int currentPage = this.page;
		reset();
		// Restore page number after reset
		this.page = currentPage;
		// Set up page change handler synchronously before async operations
		setOnPage(e -> draw());
		populateItems();
		drawFixed();
	}

	protected void prePopulate() {
	}

	protected void drawFixed() {
	}

	private void populateItems() {
		if (this.items != null) {
			if (!this.async) {
				renderItems();
			} else {
				// Do all heavy work async, then update GUI on main thread
				Skulls.newChain().asyncFirst(() -> {
					// Heavy operations on async thread:
					// - prePopulate() might do filtering/sorting
					// - Stream operations for pagination
					for (int i = 0; i < this.rows * 9; i++) {
						setItem(i, getDefaultItem());
					}
					prePopulate();
					final List<T> paginatedItems = this.items.stream().skip((page - 1) * (long) this.fillSlots().size()).limit(this.fillSlots().size()).collect(Collectors.toList());

					// Build ItemStacks asynchronously (heavy work: string processing, lore building)
					final Map<Integer, ItemStack> slotToItemStack = new HashMap<>();
					final Map<Integer, T> slotToObject = new HashMap<>();

					for (int i = 0; i < this.rows * 9; i++) {
						if (this.fillSlots().contains(i) && this.fillSlots().indexOf(i) < paginatedItems.size()) {
							final T object = paginatedItems.get(this.fillSlots().indexOf(i));
							slotToItemStack.put(i, this.makeDisplayItem(object));
							slotToObject.put(i, object);
						}
					}

					// Return both maps as a pair
					return new Object[]{slotToItemStack, slotToObject};
				}).asyncLast((result) -> {
					@SuppressWarnings("unchecked") final Map<Integer, ItemStack> slotToItemStack = (Map<Integer, ItemStack>) ((Object[]) result)[0];
					@SuppressWarnings("unchecked") final Map<Integer, T> slotToObject = (Map<Integer, T>) ((Object[]) result)[1];

					// Calculate pages
					pages = (int) Math.max(1, Math.ceil(this.items.size() / (double) this.fillSlots().size()));

					// Clear fill slots
					for (int i = 0; i < this.rows * 9; i++) {
						setItem(i, getDefaultItem());
					}

					// Set up navigation buttons
					// Only show previous button if not on first page
					if (this.page > 1) {
						setButton(getPreviousButtonSlot(), getPreviousButton(), click -> {
							prevPage();
							draw();
						});
					} else {
						// Lock slot and remove click handlers when button is hidden
						setUnlocked(getPreviousButtonSlot(), false);
						setConditional(getPreviousButtonSlot(), null, null);
						setItem(getPreviousButtonSlot(), getDefaultItem());
					}

					// Only show next button if not on last page
					if (this.page < pages) {
						setButton(getNextButtonSlot(), getNextButton(), click -> {
							nextPage();
							draw();
						});
					} else {
						// Lock slot and remove click handlers when button is hidden
						setUnlocked(getNextButtonSlot(), false);
						setConditional(getNextButtonSlot(), null, null);
						setItem(getNextButtonSlot(), getDefaultItem());
					}

					// Set items for current page using pre-built ItemStacks
					for (Map.Entry<Integer, ItemStack> entry : slotToItemStack.entrySet()) {
						final int slot = entry.getKey();
						final ItemStack itemStack = entry.getValue();
						final T object = slotToObject.get(slot);
						setButton(slot, itemStack, click -> this.onClick(object, click));
					}
				}).execute();
			}
		}
	}

	private void renderItems() {
		for (int i = 0; i < this.rows * 9; i++) {
			setItem(i, getDefaultItem());
		}
		prePopulate();

		final List<T> itemsToFill = this.items.stream().skip((page - 1) * (long) this.fillSlots().size()).limit(this.fillSlots().size()).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(this.items.size() / (double) this.fillSlots().size()));

		// Only show previous button if not on first page
		if (this.page > 1) {
			setButton(getPreviousButtonSlot(), getPreviousButton(), click -> {
				prevPage();
				draw();
			});
		} else {
			// Lock slot and remove click handlers when button is hidden
			setUnlocked(getPreviousButtonSlot(), false);
			setConditional(getPreviousButtonSlot(), null, null);
			setItem(getPreviousButtonSlot(), getDefaultItem());
		}

		// Only show next button if not on last page
		if (this.page < pages) {
			setButton(getNextButtonSlot(), getNextButton(), click -> {
				nextPage();
				draw();
			});
		} else {
			// Lock slot and remove click handlers when button is hidden
			setUnlocked(getNextButtonSlot(), false);
			setConditional(getNextButtonSlot(), null, null);
			setItem(getNextButtonSlot(), getDefaultItem());
		}
		// setOnPage is already set in draw() method, no need to set it again here

		for (int i = 0; i < this.rows * 9; i++) {
			if (this.fillSlots().contains(i) && this.fillSlots().indexOf(i) < itemsToFill.size()) {
				final T object = itemsToFill.get(this.fillSlots().indexOf(i));
				setButton(i, this.makeDisplayItem(object), click -> this.onClick(object, click));
			}
		}
	}

	protected abstract ItemStack makeDisplayItem(final T object);

	protected abstract void onClick(final T object, final GuiClickEvent clickEvent);

	@Override
	protected ItemStack getBackButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_BACK_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_BACK_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_BACK_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected ItemStack getExitButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_EXIT_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_EXIT_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_EXIT_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected ItemStack getPreviousButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_PREVIOUS_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_PREVIOUS_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_PREVIOUS_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected ItemStack getNextButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_NEXT_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_NEXT_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_NEXT_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected int getPreviousButtonSlot() {
		return 48;
	}

	@Override
	protected int getNextButtonSlot() {
		return 50;
	}
}
