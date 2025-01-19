package dev.hufeisen.maedn.gui;

import dev.hufeisen.maedn.gui.events.GUIEvents;
import dev.hufeisen.maedn.gui.utils.GUIItemType;
import dev.hufeisen.maedn.gui.utils.GUIType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class to create gui inventory
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public class GUIInventory {

    private static final List<GUIInventory> guis = new ArrayList<>();
    private final HashMap<Integer, GUIItem> items = new HashMap<>();
    private final List<Player> players = new ArrayList<>();
    //private GUIType type;
    private final List<Inventory> inventories = new ArrayList<>();
    public String title;
    private int size;
    private ItemStack filler;
    private GUIEvents events;

    protected GUIInventory(String title, int size, ItemStack filler, GUIType type, GUIEvents events) {
        this.title = title;
        this.size = size;
        this.filler = filler;
        //this.type = type;
        this.events = events;

        guis.add(this);
    }

    /**
     * <p>
     * Get a list of all GUIs
     * <p>
     *
     * @since 1.0
     */
    public static List<GUIInventory> getAllGUIs() {
        return guis;
    }

    /**
     * <p>
     * Add an item to the gui
     * <p>
     *
     * @param item The item to add
     * @param slot The slot where the item will be added
     * @since 1.0
     */
    public void addGUIItem(GUIItem item, int slot) {

        item.setGui(this);
        items.put(slot, item);

    }

    /**
     * <p>
     * Add an item to the next free slot in the gui
     * <p>
     *
     * @param item The item to add
     * @since 1.0
     */
    public void addGUIItem(GUIItem item) {

        for (int i = 0; i < size * 9; i++) {

            if (!items.containsKey(i)) {

                addGUIItem(item, i);
                break;
            }
        }
    }

    /**
     * <p>
     * Open the gui for the given player
     * <p>
     *
     * @param player The player to open the gui
     * @since 1.0
     */
    public void open(Player player) {

        Inventory inv = generateInventory(player);

        player.openInventory(inv);
        players.add(player);
        inventories.add(inv);
    }

    /**
     * <p>
     * Update the gui inventory for all players
     * <p>
     *
     * @since 1.0
     */
    public void updateInventory() {

        for (Player player : players) {

            Inventory inv = generateInventory(player);

            player.openInventory(inv);
            inventories.add(inv);

        }
    }

    private Inventory generateInventory(Player player) {

        Inventory inv = Bukkit.createInventory(player, size * 9, Component.text(title));

        for (int slot = 0; slot < inv.getSize(); slot++) {

            inv.setItem(slot, filler);

        }

        for (int slot : items.keySet()) {

            GUIItem item = items.get(slot);

            if (item.getType() == GUIItemType.Button || item.getType() == GUIItemType.SubGUI) {

                inv.setItem(slot, item.getItem());

            } else if (item.getType() == GUIItemType.Toggler) {

                if (item.isActive()) {

                    inv.setItem(slot, item.getWhenActive());

                } else {

                    inv.setItem(slot, item.getItem());

                }
            }
        }

        if (events != null) {
            events.onOpen(this, inv, player);
        }

        return inv;
    }

    /**
     * <p>
     * Run if the player clicks. For internal use only!
     * <p>
     *
     * @since 1.0
     */
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player && inventories.contains(e.getClickedInventory())) {
            events.onClick(this, e);

            GUIItem item = items.get(e.getSlot());

            if (item != null) {

                if (item.getType() == GUIItemType.SubGUI) {

                    item.getSubGui().open(((Player) e.getWhoClicked()).getPlayer());

                }

                item.onClick(e);

                e.setCancelled(!item.isRetrievable());
                return;
            }

            e.setCancelled(true);
        }
    }

    /**
     * <p>
     * Run if the player closes an inventory. For internal use only!
     * <p>
     *
     * @since 1.0
     */
    public void onInventoryClose(InventoryCloseEvent e) {

        if (inventories.contains(e.getInventory())) {

            if (events != null) events.onClose(this, e);
            inventories.remove(e.getInventory());

        }
    }
}
