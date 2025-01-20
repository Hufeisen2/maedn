package dev.hufeisen.maedn.commands;

import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.model.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Component.text("You must be a player to use this command", NamedTextColor.RED));
            return true;
        }

        GameState gameState = MaednMain.getGameState();

        if (gameState == GameState.IN_GAME) {
            commandSender.sendMessage(Component.text("You can only use this command while game state is LOBBY", NamedTextColor.RED));
            return true;
        }

        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player.getUniqueId());

        if (gameState == GameState.SETUP && gamePlayer.isSetupMode()) {
            player.sendMessage(Component.text("You are already in setup mode!", NamedTextColor.RED));
            return true;
        }

        MaednMain.setGameState(GameState.SETUP);
        gamePlayer.setSetupMode(true);
        player.sendMessage(Component.text("You are now in setup mode!", NamedTextColor.GREEN));

        player.getInventory().addItem(MaednMain.getSetupItem(player));

        return true;
    }
}
