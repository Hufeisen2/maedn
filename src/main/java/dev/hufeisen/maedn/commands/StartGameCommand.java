package dev.hufeisen.maedn.commands;

import dev.hufeisen.maedn.model.GameBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("You must be a player to use this command", NamedTextColor.RED));
            return true;
        }

        GameBoard.init();
        GameBoard.assignTeams();
        GameBoard.start();

        return true;
    }

}
