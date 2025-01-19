package dev.hufeisen.maedn.gui;

import dev.hufeisen.maedn.gui.events.GUIItemEvents;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A class with inventory templates
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public class InventoryTemplates {

    /**
     * <p>
     * A template for a true, false inventory
     * <p>
     *
     * @param title       the title of the gui
     * @param trueEvents  the events that will run when true is selected
     * @param falseEvents the events that will run when false is selected
     * @return A true false GUI Template
     * @since 1.0
     */
    public GUIInventory getTrueFalse(String title, GUIItemEvents trueEvents, GUIItemEvents falseEvents) {

        GUIInventory gui = new GUIInventoryBuilder().setTitle(title).setSize(3).build();

        GUIItem trueItem = new GUIItemBuilder().setItem(new ItemStack(Material.LIME_WOOL)).setEvents(trueEvents).build();
        GUIItem falseItem = new GUIItemBuilder().setItem(new ItemStack(Material.RED_WOOL)).setEvents(trueEvents).build();

        gui.addGUIItem(trueItem, 11);
        gui.addGUIItem(falseItem, 15);

        return gui;
    }

}
