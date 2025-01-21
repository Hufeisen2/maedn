package dev.hufeisen.maedn.commands;

import dev.hufeisen.maedn.model.GameBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You must be a player to use this command", NamedTextColor.RED));
            return true;
        }

        GameBoard.reset();
        player.sendMessage(Component.text("The server is now in lobby mode again.", NamedTextColor.GREEN));

        return true;

    }
}
