package dev.hufeisen.maedn.gui;

import dev.hufeisen.maedn.gui.events.GUIEvents;
import dev.hufeisen.maedn.gui.utils.GUIType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * The GUI Builder
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public final class GUIInventoryBuilder {
    private String title = "GUI";
    private int size = 3;
    private ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    private GUIType type = GUIType.Inventory;
    private GUIEvents events = new GUIEvents() {
        @Override
        public void onOpen(GUIInventory gui, Inventory inventory, Player player) {
            //NOTHING
        }

        @Override
        public void onClose(GUIInventory gui, InventoryCloseEvent e) {
            //NOTHING
        }

        @Override
        public void onClick(GUIInventory gui, InventoryClickEvent e) {
            //NOTHING
        }
    };

    /**
     * <p>
     * Set the title of the gui
     * <p>
     *
     * @since 1.0
     */
    public GUIInventoryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * <p>
     * Set the size of the gui in rows
     * <p>
     *
     * @since 1.0
     */
    public GUIInventoryBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * <p>
     * Set the filler of the gui
     * <p>
     *
     * @since 1.0
     */
    public GUIInventoryBuilder setFiller(ItemStack filler) {
        this.filler = filler;
        return this;
    }

    /**
     * <p>
     * Set the gui type
     * <p>
     *
     * @since 1.0
     */
    public GUIInventoryBuilder setType(GUIType type) {
        this.type = type;
        return this;
    }

    /**
     * <p>
     * Set the events class
     * <p>
     *
     * @since 1.0
     */
    public GUIInventoryBuilder setEvents(GUIEvents events) {
        this.events = events;
        return this;
    }

    /**
     * <p>
     * Build the gui
     * <p>
     *
     * @since 1.0
     */
    public GUIInventory build() {
        return new GUIInventory(title, size, filler, type, events);
    }
}
