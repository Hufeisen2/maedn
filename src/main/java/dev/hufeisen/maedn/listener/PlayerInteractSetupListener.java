package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.api.gui.GUIInventory;
import dev.hufeisen.maedn.api.gui.GUIInventoryBuilder;
import dev.hufeisen.maedn.api.gui.GUIItem;
import dev.hufeisen.maedn.api.gui.GUIItemBuilder;
import dev.hufeisen.maedn.api.gui.events.GUIItemEvents;
import dev.hufeisen.maedn.model.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteractSetupListener implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {

        if (event.getItem() == null || !event.getItem().getItemMeta().getPersistentDataContainer().has(MaednMain.getNamespacedKey("setup"))) {
            return;
        }

        if (!GamePlayer.getGamePlayer(event.getPlayer().getUniqueId()).isSetupMode()) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            event.setCancelled(true);
            openSetupMenu(event.getPlayer());

        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

            event.setCancelled(true);
            handleLeftClick(event.getPlayer(), event.getClickedBlock().getLocation());

        }
    }

    private final GUIItemEvents selectItemEvent = (gui, guiItem, e) -> {

        Player player = (Player) e.getWhoClicked();
        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player.getUniqueId());
        ItemStack item = e.getCurrentItem();

        String setupSelection = item.getItemMeta().getPersistentDataContainer().get(MaednMain.getNamespacedKey("selection"), PersistentDataType.STRING);
        gamePlayer.setSetupSelection(setupSelection);

        player.sendMessage(Component.text("You selected ").append(item.effectiveName()));

        updateSetupItem(player);
        player.closeInventory();
    };

    private void openSetupMenu(Player player) {
        GUIInventory gui = new GUIInventoryBuilder().setTitle("Setup")
                .setSize(5)
                .build();

        gui.addGUIItem(createMenuItem(Material.GRAY_WOOL, "Field", NamedTextColor.GRAY, selectItemEvent, "fields"), 19);

        gui.addGUIItem(createMenuItem(Material.RED_WOOL, "Team Red Start", NamedTextColor.RED, selectItemEvent, "red_start"), 3);
        gui.addGUIItem(createMenuItem(Material.BLUE_WOOL, "Team Blue Start", NamedTextColor.BLUE, selectItemEvent, "blue_start"), 4);
        gui.addGUIItem(createMenuItem(Material.GREEN_WOOL, "Team Green Start", NamedTextColor.GREEN, selectItemEvent, "green_start"), 5);
        gui.addGUIItem(createMenuItem(Material.YELLOW_WOOL, "Team Yellow Start", NamedTextColor.YELLOW, selectItemEvent, "yellow_start"), 6);

        gui.addGUIItem(createMenuItem(Material.RED_WOOL, "Team Red Start Entrance", NamedTextColor.RED, selectItemEvent, "red_start_entrance"), 12);
        gui.addGUIItem(createMenuItem(Material.BLUE_WOOL, "Team Blue Start Entrance", NamedTextColor.BLUE, selectItemEvent, "blue_start_entrance"), 13);
        gui.addGUIItem(createMenuItem(Material.GREEN_WOOL, "Team Green Start Entrance", NamedTextColor.GREEN, selectItemEvent, "green_start_entrance"), 14);
        gui.addGUIItem(createMenuItem(Material.YELLOW_WOOL, "Team Yellow Start Entrance", NamedTextColor.YELLOW, selectItemEvent, "yellow_start_entrance"), 15);

        gui.addGUIItem(createMenuItem(Material.RED_CONCRETE, "Team Red Home", NamedTextColor.RED, selectItemEvent, "red_home"), 30);
        gui.addGUIItem(createMenuItem(Material.BLUE_CONCRETE, "Team Blue Home", NamedTextColor.BLUE, selectItemEvent, "blue_home"), 31);
        gui.addGUIItem(createMenuItem(Material.GREEN_CONCRETE, "Team Green Home", NamedTextColor.GREEN, selectItemEvent, "green_home"), 32);
        gui.addGUIItem(createMenuItem(Material.YELLOW_CONCRETE, "Team Yellow Home", NamedTextColor.YELLOW, selectItemEvent, "yellow_home"), 33);

        gui.addGUIItem(createMenuItem(Material.RED_CONCRETE, "Team Red Home Entrance", NamedTextColor.RED, selectItemEvent, "red_home_entrance"), 39);
        gui.addGUIItem(createMenuItem(Material.BLUE_CONCRETE, "Team Blue Home Entrance", NamedTextColor.BLUE, selectItemEvent, "blue_home_entrance"), 40);
        gui.addGUIItem(createMenuItem(Material.GREEN_CONCRETE, "Team Green Home Entrance", NamedTextColor.GREEN, selectItemEvent, "green_home_entrance"), 41);
        gui.addGUIItem(createMenuItem(Material.YELLOW_CONCRETE, "Team Yellow Home Entrance", NamedTextColor.YELLOW, selectItemEvent, "yellow_home_entrance"), 42);

        gui.open(player);
    }

    private GUIItem createMenuItem(Material material, String name, NamedTextColor color, GUIItemEvents events, String setupSelection) {
        ItemStack field = new ItemStack(material);
        ItemMeta fieldMeta = field.getItemMeta();
        fieldMeta.displayName(Component.text(name, color).decoration(TextDecoration.ITALIC, false));
        fieldMeta.getPersistentDataContainer().set(MaednMain.getNamespacedKey("selection"), PersistentDataType.STRING, setupSelection);
        field.setItemMeta(fieldMeta);
        return new GUIItemBuilder().setItem(field).setEvents(events).build();
    }

    private void updateSetupItem(Player player) {
        player.getInventory().forEach(item -> {
            if (item == null || !item.hasItemMeta()) {
                return;
            }
            if (item.getItemMeta().getPersistentDataContainer().has(MaednMain.getNamespacedKey("setup"))) {
                item.setItemMeta(MaednMain.getSetupItem(player).getItemMeta());
            }
        });
    }

    private void handleLeftClick(Player player, Location location) {
        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player.getUniqueId());
        String setupSelection = gamePlayer.getSetupSelection();
        FileConfiguration config = MaednMain.getInstance().getConfig();

        if (setupSelection.contains("entrance")) {

            config.set(setupSelection, location);

            player.sendMessage(Component.text("Entrance location set!"));

        } else {
            int size = config.getInt(setupSelection + ".size", 0);
            config.set(setupSelection + "." + size, location);
            config.set(setupSelection + ".size", size + 1);

            player.sendMessage(Component.text("Location saved for index ").append(Component.text(size, NamedTextColor.WHITE)));
        }

        MaednMain.getInstance().saveConfig();
    }
}
