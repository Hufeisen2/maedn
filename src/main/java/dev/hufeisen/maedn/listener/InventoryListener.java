package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.api.gui.GUIInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        for (GUIInventory gui : GUIInventory.getAllGUIs()) gui.onInventoryClick(event);

        if (MaednMain.getGameState() != GameState.SETUP) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (MaednMain.getGameState() != GameState.SETUP) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        for (GUIInventory gui : GUIInventory.getAllGUIs()) gui.onInventoryClose(event);
    }

}
