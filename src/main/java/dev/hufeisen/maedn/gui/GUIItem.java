package dev.hufeisen.maedn.gui;

import dev.hufeisen.maedn.gui.events.GUIItemEvents;
import dev.hufeisen.maedn.gui.utils.GUIItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The GUIItem class
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public class GUIItem implements Cloneable {

    private boolean isActive;
    private boolean isRetrievable;

    private ItemStack item;
    private ItemStack whenActive;

    private GUIItemType type;
    private GUIInventory subGui;
    private GUIItemEvents events;

    private GUIInventory gui;

    protected GUIItem(boolean isActive, boolean isRetrievable, ItemStack item, ItemStack whenActive, GUIItemType type, GUIInventory subGui, GUIItemEvents events) {
        this.isActive = isActive;
        this.isRetrievable = isRetrievable;
        this.item = item;
        this.whenActive = whenActive;
        this.type = type;
        this.subGui = subGui;
        this.events = events;
    }

    protected void onClick(InventoryClickEvent e) {

        if (type == GUIItemType.Button) {

            events.onClick(gui, this, e);

        } else if (type == GUIItemType.SubGUI) {

            events.onClick(gui, this, e);
            subGui.open((Player) e.getWhoClicked());

        } else if (type == GUIItemType.Toggler) {

            if (isActive) {

                events.onDisable(gui, this);
                isActive = false;

            } else {

                events.onActivate(gui, this);
                isActive = true;
            }

            events.onClick(gui, this, e);
            gui.updateInventory();

        }

    }

    /**
     * <p>
     * Get if the item is active
     * <p>
     *
     * @since 1.0
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * <p>
     * Enable or disable the item
     * <p>
     *
     * @param isActive true, when you want to set it active, false if you want to disable it
     * @since 1.0
     */
    public void setActive(boolean isActive) {

        if (isActive) {

            events.onActivate(gui, this);
            this.isActive = true;

        } else {

            events.onDisable(gui, this);
            this.isActive = false;

        }

        gui.updateInventory();

    }

    protected boolean isRetrievable() {
        return isRetrievable;
    }

    protected ItemStack getItem() {
        return item;
    }

    protected GUIItemType getType() {
        return type;
    }

    protected ItemStack getWhenActive() {
        return whenActive;
    }

    protected GUIInventory getSubGui() {
        return subGui;
    }

    protected void setGui(GUIInventory gui) {
        this.gui = gui;
    }

    @Override
    public GUIItem clone() {
        try {
            GUIItem clone = (GUIItem) super.clone();

            clone.isActive = isActive;
            clone.isRetrievable = isRetrievable;
            clone.item = item.clone();
            clone.whenActive = whenActive.clone();
            clone.type = type;
            clone.subGui = subGui;
            clone.events = events;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
