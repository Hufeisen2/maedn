package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.model.GameBoard;
import dev.hufeisen.maedn.model.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        GameState gameState = MaednMain.getGameState();

        if(gameState == GameState.IN_GAME) {
            event.joinMessage(Component.text("The player " + event.getPlayer().getName() + " joined the game.", NamedTextColor.GREEN));
        } else if(gameState == GameState.LOBBY) {
            event.joinMessage(Component.text("The player " + event.getPlayer().getName() + " joined the game. Run", NamedTextColor.GREEN)
                    .append(Component.text(" /start", NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text(" to start the game!", NamedTextColor.GREEN)));
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        GamePlayer gamePlayer = GamePlayer.getGamePlayer(event.getPlayer().getUniqueId());

        //Reset the game if the player is playing, otherwise everything will break... Can I fix this? Yes. Do I want to fix this? No.
        if(GameBoard.getPlayers().contains(gamePlayer)) {
            GameBoard.reset();
            event.quitMessage(Component.text("The Player " + event.getPlayer().getName() + " left and the game was aborted.", NamedTextColor.RED));
        }
    }
}
