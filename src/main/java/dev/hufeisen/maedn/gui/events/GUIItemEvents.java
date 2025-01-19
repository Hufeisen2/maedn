package dev.hufeisen.maedn.gui.events;

import dev.hufeisen.maedn.gui.GUIInventory;
import dev.hufeisen.maedn.gui.GUIItem;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * An interface with the events of the gui item
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public interface GUIItemEvents {

    /**
     * <p>
     * Will be executed when the player clicks on the item
     * <p>
     *
     * @param gui  The clicked gui
     * @param item The clicked gui item
     * @param e    The click event
     * @since 1.0
     */
    void onClick(GUIInventory gui, GUIItem item, InventoryClickEvent e);

    /**
     * <p>
     * Will be executed when the player activates a gui item
     * <p>
     *
     * @param gui  The clicked gui
     * @param item The activated gui item
     * @since 1.0
     */
    default void onActivate(GUIInventory gui, GUIItem item) {
    }

    /**
     * <p>
     * Will be executed when the player disables a gui item
     * <p>
     *
     * @param gui  The clicked gui
     * @param item The disabled gui item
     * @since 1.0
     */
    default void onDisable(GUIInventory gui, GUIItem item) {
    }

}
