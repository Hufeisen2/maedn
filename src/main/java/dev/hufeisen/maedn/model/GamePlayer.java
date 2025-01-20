package dev.hufeisen.maedn.model;

import dev.hufeisen.maedn.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GamePlayer {

    private static HashMap<String, GamePlayer> players = new HashMap<>();

    private String uuid;
    private Team team;

    private List<GamePiece> pieces = new ArrayList<>();

    private int diceRollCount = 0;
    private boolean canRollDice = true;
    private int diceResult = 0;

    private boolean setupMode;
    private String setupSelection = "fields";

    public GamePlayer(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public void resetPlayer() {
        pieces.forEach(GamePiece::destroy);
        pieces = new ArrayList<>();
        team = null;
        resetDice();
    }

    public void updateInventory() {
        Player player = getPlayer();

        for (int i = 0; i < 9; i++) {
            player.getInventory().setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        if (GameBoard.getCurrentTeam() == team) {
            for (int i = 0; i < pieces.size(); i++) {
                ItemStack item = new ItemStack(Material.ARMOR_STAND, i + 1);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text("Piece " + (i + 1)).decoration(TextDecoration.ITALIC, false));
                item.setItemMeta(meta);
                player.getInventory().setItem(i, item);
            }

            ItemStack diceItem = new ItemStack(Material.TARGET);
            ItemMeta diceMeta = diceItem.getItemMeta();
            diceMeta.displayName(Component.text("Roll Dice").decoration(TextDecoration.ITALIC, false));
            diceItem.setItemMeta(diceMeta);
            player.getInventory().setItem(8, diceItem);
        }
    }

    // true if all pieces in home are at last possible position
    public boolean isHomeComplete() {

        int piecesAtHomeCount = getPiecesAtHomeCount();

        for(int i = GameBoard.getHomeFields(team).size()-1; i >= GameBoard.getHomeFields(team).size() - piecesAtHomeCount; i--) {
            if(GameBoard.getPieceAtHomePosition(i, team) == null) {
                return false;
            }
        }

        return true;
    }

    // True if all fields in the home are used
    public boolean isHomeFull() {
        return isHomeComplete() && GameBoard.getHomeFields(team).size() == getPiecesAtHomeCount();
    }

    private int getPiecesAtHomeCount() {
        return  (int) pieces.stream().filter(GamePiece::isAtHome).count();
    }

    public void setDiceResult(int result) {
        Player player = getPlayer();
        player.sendMessage(Component.text("You rolled a " + result, NamedTextColor.GREEN));

        if (result == 6) {

            int entrancePosition = GameBoard.getStartFieldEntrance().get(team);

            if (pieces.stream().anyMatch(GamePiece::isAtStart)) {
                GamePiece pieceAtStartField = GameBoard.getPieceAtFieldPosition(entrancePosition);

                if(pieceAtStartField == null) {
                    GameBoard.movePieceToBoard(this, pieces.stream().filter(GamePiece::isAtStart).findFirst().get());
                    diceResult = 0;
                } else if(pieceAtStartField.getTeam() != team) {
                    GameBoard.takePiece(pieceAtStartField);
                    GameBoard.movePieceToBoard(this, pieces.stream().filter(GamePiece::isAtStart).findFirst().get());
                }
                else {
                    diceResult = result;
                }
            } else {
                diceResult = result;
            }

            diceRollCount = 0;
            canRollDice = true;
            player.sendMessage(Component.text("You can roll again", NamedTextColor.GREEN));
        } else {

            if (isPieceInGame() || !isHomeComplete()) {
                canRollDice = false;

                if(pieces.stream().filter(piece -> piece.isInGame() || piece.isAtHome())
                        .noneMatch(piece -> GameBoard.isMoveAllowed(this, piece, result))) {
                    GameBoard.nextTurn();
                    return;
                }

                diceResult = result;

            } else if (diceRollCount < 2 && isHomeComplete()) {
                canRollDice = true;
                diceResult = 0;
                diceRollCount++;
            } else {
                GameBoard.nextTurn();
            }
        }
    }

    public void resetDice() {
        diceResult = 0;
        diceRollCount = 0;
        canRollDice = true;
    }

    public void resetDiceResult() {
        diceResult = 0;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public boolean isDiceRollAllowed() {
        return canRollDice;
    }

    public void setSetupMode(boolean setupMode) {
        this.setupMode = setupMode;
    }

    public boolean isSetupMode() {
        return setupMode;
    }

    public boolean isPieceInGame() {
        return pieces.stream().anyMatch(GamePiece::isInGame);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public static GamePlayer getGamePlayer(UUID uuid) {
        if (!players.containsKey(uuid.toString())) {
            players.put(uuid.toString(), new GamePlayer(uuid));
        }
        return players.get(uuid.toString());
    }

    public static GamePlayer getGamePlayerByTeam(Team team) {
        for (GamePlayer value : players.values()) {
            if (value.team == team) {
                return value;
            }
        }

        return null;
    }

    public static List<GamePlayer> getPlayingPlayers() {
        List<GamePlayer> playingPlayers = new ArrayList<>();
        for (GamePlayer value : players.values()) {
            if (value.team != null) {
                playingPlayers.add(value);
            }
        }
        return playingPlayers;
    }

    public String getSetupSelection() {
        return setupSelection;
    }

    public void setSetupSelection(String setupSelection) {
        this.setupSelection = setupSelection;
    }

    public void assignTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void assignPiece(GamePiece piece) {
        pieces.add(piece);
    }

    public List<GamePiece> getPieces() {
        return pieces;
    }
}
