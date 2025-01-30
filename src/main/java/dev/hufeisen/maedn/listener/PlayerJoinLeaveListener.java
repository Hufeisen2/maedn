package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.model.GameBoard;
import dev.hufeisen.maedn.model.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
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
                    .append(Component.text(" /start", NamedTextColor.GOLD, TextDecoration.BOLD).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/start")))
                    .append(Component.text(" to start the game!", NamedTextColor.GREEN)));
        } else if (gameState == GameState.PAUSE && GameBoard.getPlayers().contains(GamePlayer.getGamePlayer(event.getPlayer().getUniqueId()))) {
            if(GameBoard.getPlayers().stream()
                    .filter(player -> !player.getUuid().equals(event.getPlayer().getUniqueId().toString()))
                    .allMatch(player -> player.getPlayer() != null && player.getPlayer().isOnline())) {

                event.joinMessage(Component.text("The player " + event.getPlayer().getName() + " joined the game. The game will be continued.", NamedTextColor.GREEN));
                MaednMain.setGameState(GameState.IN_GAME);

            } else {

                event.joinMessage(Component.text("The player " + event.getPlayer().getName() + " joined the game. There are more players missing...", NamedTextColor.GREEN));

            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        GamePlayer gamePlayer = GamePlayer.getGamePlayer(event.getPlayer().getUniqueId());

        //Reset the game if the player is playing, otherwise everything will break... Can I fix this? Yes. Do I want to fix this? No.
        if(MaednMain.getGameState() == GameState.IN_GAME && GameBoard.getPlayers().contains(gamePlayer)) {
            MaednMain.setGameState(GameState.PAUSE);
            event.quitMessage(Component.text("The Player " + event.getPlayer().getName() + " left and the game was paused.", NamedTextColor.RED));
        }
    }
}
