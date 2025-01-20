package dev.hufeisen.maedn.api.gui.events;

import dev.hufeisen.maedn.api.gui.GUIInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/**
 * An interface with the events of the gui
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public interface GUIEvents {

    /**
     * <p>
     * Will be executed when the gui opens for a user
     * <p>
     *
     * @param gui       The opened gui
     * @param inventory The opened inventory
     * @param player    The player that opens the inventory
     * @since 1.0
     */
    void onOpen(GUIInventory gui, Inventory inventory, Player player);

    /**
     * <p>
     * Will be executed when the gui closes for a user
     * <p>
     *
     * @param gui The closed gui
     * @param e   The close event
     * @since 1.0
     */
    void onClose(GUIInventory gui, InventoryCloseEvent e);

    /**
     * <p>
     * Will be executed when the gui was clicked from a user
     * <p>
     *
     * @param gui The clicked gui
     * @param e   The click event
     * @since 1.0
     */
    void onClick(GUIInventory gui, InventoryClickEvent e);

}
