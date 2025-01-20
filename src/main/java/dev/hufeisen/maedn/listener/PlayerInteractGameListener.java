package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.model.GameBoard;
import dev.hufeisen.maedn.model.GamePiece;
import dev.hufeisen.maedn.model.GamePlayer;
import dev.hufeisen.maedn.utils.DiceUtils;
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

            if (event.getItem().getType() == Material.TARGET && gamePlayer.isDiceRollAllowed() && gamePlayer.getDiceResult() == 0) {
                int rand = DiceUtils.rollDice();
                gamePlayer.setDiceResult(rand);
            } else if (event.getItem().getType() == Material.ARMOR_STAND) {

                GamePiece piece = gamePlayer.getPieces().get(event.getItem().getAmount() - 1);

                if (piece == null) {
                    return;
                }

                if (GameBoard.getStartFieldEntrance().get(gamePlayer.getTeam()) != piece.getPosition()
                        && GameBoard.isPieceAtStartAndCanMove(gamePlayer)
                        && gamePlayer.getPieces().stream().anyMatch(GamePiece::isAtStart)) {

                    player.sendMessage(Component.text("You must clear your starting field!", NamedTextColor.RED));

                } else if (GameBoard.isMoveAllowed(gamePlayer, piece, gamePlayer.getDiceResult())) {
                    GameBoard.movePiece(gamePlayer, piece, gamePlayer.getDiceResult());
                    gamePlayer.resetDiceResult();

                    if (!gamePlayer.isDiceRollAllowed()) {
                        GameBoard.nextTurn();
                    }
                } else {
                    player.sendMessage("You can't move this piece.");
                }
            }

            event.setCancelled(true);
        }
    }
}
