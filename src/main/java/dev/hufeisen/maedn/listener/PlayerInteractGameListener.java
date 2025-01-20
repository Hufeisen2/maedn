package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.model.GameBoard;
import dev.hufeisen.maedn.model.GamePiece;
import dev.hufeisen.maedn.model.GamePlayer;
import dev.hufeisen.maedn.utils.DiceUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractGameListener implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {

        if (MaednMain.getGameState() == GameState.IN_GAME) {

            Player player = event.getPlayer();
            GamePlayer gamePlayer = GamePlayer.getGamePlayer(player.getUniqueId());

            if (event.getItem() == null) {
                return;
            }

            if (gamePlayer.getTeam() != GameBoard.getCurrentTeam()) {
                return;
            }

            if (event.getItem().getType() == Material.TARGET) {

                if (gamePlayer.isDiceRollAllowed() && gamePlayer.getDiceResult() == 0) {

                    int rand = DiceUtils.rollDice();
                    gamePlayer.setDiceResult(rand);

                } else {
                    player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1f, 1f));
                    player.sendMessage(Component.text("You've already rolled your dice!", NamedTextColor.RED));
                }

            } else if (event.getItem().getType() == Material.ARMOR_STAND) {

                GamePiece piece = gamePlayer.getPieces().get(event.getItem().getAmount() - 1);

                if (piece == null) {
                    return;
                }

                if (gamePlayer.getDiceResult() <= 0) {
                    player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1f, 1f));
                    player.sendMessage(Component.text("You have to roll the dice first!", NamedTextColor.RED));
                    return;
                }

                if (GameBoard.getStartFieldEntrance().get(gamePlayer.getTeam()) != piece.getPosition()
                        && GameBoard.isPieceAtStartAndCanMove(gamePlayer)
                        && gamePlayer.getPieces().stream().anyMatch(GamePiece::isAtStart)) {

                    player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1f, 1f));
                    player.sendMessage(Component.text("You must clear your starting field!", NamedTextColor.RED));

                } else if (GameBoard.isMoveAllowed(gamePlayer, piece, gamePlayer.getDiceResult())) {
                    GameBoard.movePiece(gamePlayer, piece, gamePlayer.getDiceResult());
                    gamePlayer.resetDiceResult();

                    if (!gamePlayer.isDiceRollAllowed()) {
                        GameBoard.nextTurn();
                    }
                } else {
                    player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1f, 1f));
                    player.sendMessage(Component.text("This move is not possible!", NamedTextColor.RED));
                }
            }
        }

        if (MaednMain.getGameState() != GameState.SETUP) {
            event.setCancelled(true);
        }
    }
}
