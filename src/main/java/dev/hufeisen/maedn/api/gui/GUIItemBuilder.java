package dev.hufeisen.maedn.api.gui;

import dev.hufeisen.maedn.api.gui.events.GUIItemEvents;
import dev.hufeisen.maedn.api.gui.utils.GUIItemType;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The GUIItem Builder
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public final class GUIItemBuilder {

    private boolean isActive = false;
    private boolean isRetrievable = false;
    private ItemStack item = new ItemStack(Material.STONE);
    private ItemStack whenActive = new ItemStack(Material.GRASS_BLOCK);
    private GUIItemType type = GUIItemType.Button;
    private GUIInventory subGui = null;
    private GUIItemEvents events = new GUIItemEvents() {
        @Override
        public void onClick(GUIInventory gui, GUIItem item, InventoryClickEvent e) {
            //NOTHING
        }

        @Override
        public void onActivate(GUIInventory gui, GUIItem item) {
            //NOTHING
        }

        @Override
        public void onDisable(GUIInventory gui, GUIItem item) {
            //NOTHING
        }
    };

    /**
     * <p>
     * Set the item active
     * <p>
     *
     * @since 1.0
     */
    public GUIItemBuilder setActive() {
        this.isActive = true;
        return this;
    }

    /**
     * <p>
     * Set the item retrievable
     * <p>
     *
     * @since 1.0
     */
    public GUIItemBuilder setRetrievable() {
        this.isRetrievable = true;
        return this;
    }

    /**
     * <p>
     * Set the itemstack
     * <p>
     *
     * @since 1.0
     */
    public GUIItemBuilder setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    /**
     * <p>
     * Set the itemstack that will be displayed when the item is active
     * <p>
     *
     * @since 1.0
     */
    public GUIItemBuilder setWhenActive(ItemStack whenActive) {
        this.whenActive = whenActive;
        return this;
    }

    /**
     * <p>
     * Set the GUIItemType
     * <p>
     *
     * @since 1.0
     */
    public GUIItemBuilder setType(GUIItemType type) {
        this.type = type;
        return this;
    }

    /**
     * <p>
     * Set the Sub GUI
     * <p>
     *
     * @since 1.0
     */
    public GUIItemBuilder setSubGui(GUIInventory subGui) {
        this.subGui = subGui;
        return this;
    }

    /**
     * <p>
     * Set the events class
     * <p>
     *
     * @since 1.0
     */
    public GUIItemBuilder setEvents(GUIItemEvents events) {
        this.events = events;
        return this;
    }

    /**
     * <p>
     * Build the item
     * <p>
     *
     * @since 1.0
     */
    public GUIItem build() {
        return new GUIItem(isActive, isRetrievable, item, whenActive, type, subGui, events);
    }
}
