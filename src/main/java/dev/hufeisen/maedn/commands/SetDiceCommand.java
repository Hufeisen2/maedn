package dev.hufeisen.maedn.commands;

import dev.hufeisen.maedn.utils.DiceUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetDiceCommand implements CommandExecutor {

    //Just for easier debugging

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        DiceUtils.setNextDiceValue(Integer.parseInt(args[0]));

        return true;
    }
}
