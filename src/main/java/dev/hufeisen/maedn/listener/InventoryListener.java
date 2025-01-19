package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.gui.GUIInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        for (GUIInventory gui : GUIInventory.getAllGUIs()) gui.onInventoryClick(event);

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        for (GUIInventory gui : GUIInventory.getAllGUIs()) gui.onInventoryClose(event);
    }

}
