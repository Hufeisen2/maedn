package dev.hufeisen.maedn;

import dev.hufeisen.maedn.api.particle.ParticleAPI;
import dev.hufeisen.maedn.api.particle.templates.BeamEffect;
import dev.hufeisen.maedn.model.GameBoard;
import dev.hufeisen.maedn.model.GamePiece;
import dev.hufeisen.maedn.model.GamePlayer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class GameLoop implements Runnable {

    @Override
    public void run() {

        Optional<GamePlayer> currentPlayerOptional = GameBoard.getPlayers().stream().filter(player -> player.getTeam() == GameBoard.getCurrentTeam()).findFirst();

        if(currentPlayerOptional.isPresent()) {

            GamePlayer currentPlayer = currentPlayerOptional.get();

            displayPiecePath(currentPlayer, currentPlayer.getPlayer().getInventory().getHeldItemSlot());
        }
    }

    private void displayPiecePath(GamePlayer player, int itemSlot) {

        if(player.getDiceResult() <= 0) {
            return;
        }

        ItemStack item = player.getPlayer().getInventory().getItem(itemSlot);

        if(item == null || item.getType() != Material.ARMOR_STAND) {
            return;
        }

        int pieceIndex = item.getAmount() - 1;

        if(pieceIndex >= player.getPieces().size()) {
            return;
        }

        GamePiece piece = player.getPieces().get(pieceIndex);

        if(piece == null) {
            return;
        }

        if(piece.isAtStart()) {
            return;
        }

        Color particleColor = Color.GREEN;

        if ((GameBoard.getStartFieldEntrance().get(player.getTeam()) != piece.getPosition()
                && GameBoard.isPieceAtStartAndCanMove(player)
                && player.getPieces().stream().anyMatch(GamePiece::isAtStart))
                || !GameBoard.isMoveAllowed(player, piece, player.getDiceResult())) {
            particleColor = Color.RED;
        }

        Team team = player.getTeam();

        if(piece.isInGame()) {

            int entrancePosition = GameBoard.getHomeFieldEntrance().get(player.getTeam());

            int prevPosition = piece.getPosition();
            boolean isPrevHome = false;
            for(int i = piece.getPosition() + 1; i <= piece.getPosition() + player.getDiceResult(); i++) {

                int fieldIndex = i;
                if(i >= GameBoard.getFieldSize()) {
                    fieldIndex = i - GameBoard.getFieldSize();
                }

                Location startLocation;
                Location endLocation;
                if(fieldIndex > entrancePosition && piece.getPosition() < entrancePosition) {

                    int homePosition = fieldIndex - entrancePosition - 1;
                    if(isPrevHome) {
                        startLocation = GameBoard.homePositionToLocation(prevPosition, team);
                    } else {
                        startLocation = GameBoard.positionToLocation(prevPosition);
                        isPrevHome = true;
                    }

                    endLocation = GameBoard.homePositionToLocation(homePosition, team);
                    prevPosition = homePosition;

                } else {
                    startLocation = GameBoard.positionToLocation(prevPosition);
                    endLocation = GameBoard.positionToLocation(fieldIndex);
                    prevPosition = fieldIndex;
                }

                drawLine(startLocation.toCenterLocation().subtract(0, 0.7, 0), endLocation.toCenterLocation().subtract(0, 0.7, 0), particleColor);
            }
        } else if(piece.isAtHome()) {
            int prevPosition = piece.getPosition();
            for(int i = piece.getPosition() + 1; i <= piece.getPosition() + player.getDiceResult(); i++) {

                if(i >= GameBoard.getHomeFields(player.getTeam()).size()) {
                    break;
                }

                Location startLocation = GameBoard.homePositionToLocation(prevPosition, team);
                Location endLocation = GameBoard.homePositionToLocation(i, team);

                drawLine(startLocation.toCenterLocation().subtract(0, 0.7, 0), endLocation.toCenterLocation().subtract(0, 0.7, 0), particleColor);
                prevPosition = i;
            }
        }
    }

    private void drawLine(Location start, Location end, Color color) {
        ParticleAPI.startNewParticleEffect(new BeamEffect(start,
                end,
                Particle.DUST,
                new Particle.DustOptions(color, 1),
                2,
                1));
    }
}
