package dev.hufeisen.maedn;

import dev.hufeisen.maedn.api.particle.ParticleAPI;
import dev.hufeisen.maedn.commands.SetDiceCommand;
import dev.hufeisen.maedn.commands.SetupCommand;
import dev.hufeisen.maedn.commands.StartGameCommand;
import dev.hufeisen.maedn.listener.*;
import dev.hufeisen.maedn.model.GameBoard;
import dev.hufeisen.maedn.model.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public final class MaednMain extends JavaPlugin {

    private static MaednMain instance;
    private GameState gameState = GameState.LOBBY;

    private BukkitTask particleRunnable;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getConsoleSender().sendMessage("$6Initializing Maedn Plugin");

        init(getServer().getPluginManager());

        getServer().getConsoleSender().sendMessage("$6Maedn Plugin initialized");

    }

    private void init(PluginManager pluginManager) {

        getCommand("setup").setExecutor(new SetupCommand());
        getCommand("start").setExecutor(new StartGameCommand());
        getCommand("setdice").setExecutor(new SetDiceCommand());

        pluginManager.registerEvents(new DamageAndBlockListener(), this);
        pluginManager.registerEvents(new PlayerJoinLeaveListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PlayerInteractSetupListener(), this);
        pluginManager.registerEvents(new PlayerInteractGameListener(), this);

        particleRunnable = getServer().getScheduler().runTaskTimerAsynchronously(this, ParticleAPI::updateParticle, 0, 2);
    }

    @Override
    public void onDisable() {
        GameBoard.reset();
        if (particleRunnable != null) {
            particleRunnable.cancel();
        }
    }

    public static MaednMain getInstance() {
        return instance;
    }

    public static void setGameState(GameState gameState) {
        instance.gameState = gameState;
    }

    public static GameState getGameState() {
        return instance.gameState;
    }

    public static NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(instance, key);
    }

    public static ItemStack getSetupItem(Player player) {
        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player.getUniqueId());

        String mode = switch (gamePlayer.getSetupSelection()) {
            case "fields" -> "Fields";
            case "red_start" -> "Red Start";
            case "blue_start" -> "Blue Start";
            case "green_start" -> "Green Start";
            case "yellow_start" -> "Yellow Start";
            case "red_home" -> "Red Home";
            case "blue_home" -> "Blue Home";
            case "green_home" -> "Green Home";
            case "yellow_home" -> "Yellow Home";
            case "red_start_entrance" -> "Red Start Entrance";
            case "blue_start_entrance" -> "Blue Start Entrance";
            case "green_start_entrance" -> "Green Start Entrance";
            case "yellow_start_entrance" -> "Yellow Start Entrance";
            case "red_home_entrance" -> "Red Home Entrance";
            case "blue_home_entrance" -> "Blue Home Entrance";
            case "green_home_entrance" -> "Green Home Entrance";
            case "yellow_home_entrance" -> "Yellow Home Entrance";
            default -> "UNKNOWN";
        };

        ItemStack setupItem = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta setupItemMeta = setupItem.getItemMeta();

        setupItemMeta.displayName(Component.text("Setup", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = List.of(
                Component.text("Current Mode: ", NamedTextColor.GREEN).append(Component.text(mode, NamedTextColor.WHITE)).decoration(TextDecoration.ITALIC, false),
                Component.space(),
                Component.text("Left click to setup fields", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("Right click to select setup type", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        setupItemMeta.lore(lore);

        setupItemMeta.getPersistentDataContainer().set(MaednMain.getNamespacedKey("setup"), PersistentDataType.BOOLEAN, true);

        setupItem.setItemMeta(setupItemMeta);
        return setupItem;
    }
}
