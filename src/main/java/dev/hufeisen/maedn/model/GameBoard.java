package dev.hufeisen.maedn.model;

import dev.hufeisen.maedn.GameLoop;
import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import dev.hufeisen.maedn.Team;
import dev.hufeisen.maedn.utils.ArmorStandUtils;
import dev.hufeisen.maedn.utils.ColorUtils;
import dev.hufeisen.maedn.utils.PlayerUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GameBoard {

    private static Team currentTeam;

    private static List<Location> fields;
    private static HashMap<Team, List<Location>> startFields;
    private static HashMap<Team, List<Location>> homeFields;

    private static HashMap<Team, Integer> startFieldEntrance;
    private static HashMap<Team, Integer> homeFieldEntrance;

    private static List<Team> playingTeams;
    private static List<GamePlayer> players;

    private static BossBar bossBar;

    private static BukkitTask gameLoop;

    public static void init() {

        FileConfiguration config = MaednMain.getInstance().getConfig();

        reset();

        gameLoop = Bukkit.getScheduler().runTaskTimerAsynchronously(MaednMain.getInstance(), new GameLoop(), 0, 2);
        //Set up fields
        int fieldSize = config.getInt("fields.size");
        for (int i = 0; i < fieldSize; i++) {
            String locationPath = "fields." + i;
            fields.add(config.getLocation(locationPath));
        }

        //Setup team locations
        for (Team team : Team.getEnabledTeams()) {

            int startFieldSize = config.getInt(team.getKey() + "_start.size");
            int homeFieldSize = config.getInt(team.getKey() + "_home.size");

            List<Location> teamStartFields = new ArrayList<>();
            List<Location> teamHomeFields = new ArrayList<>();

            for (int i = 0; i < startFieldSize; i++) {
                String locationPath = team.getKey() + "_start." + i;
                teamStartFields.add(config.getLocation(locationPath));
            }

            for (int i = 0; i < homeFieldSize; i++) {
                String locationPath = team.getKey() + "_home." + i;
                teamHomeFields.add(config.getLocation(locationPath));
            }

            startFields.put(team, teamStartFields);
            homeFields.put(team, teamHomeFields);

            Location startEntrance = config.getLocation(team.getKey() + "_start_entrance");
            Location homeEntrance = config.getLocation(team.getKey() + "_home_entrance");

            fields.forEach(field -> {
                if (field.equals(startEntrance)) {
                    startFieldEntrance.put(team, fields.indexOf(field));
                }
                if (field.equals(homeEntrance)) {
                    homeFieldEntrance.put(team, fields.indexOf(field));
                }
            });

            if (!startFieldEntrance.containsKey(team)) {
                throw new IllegalArgumentException("Start entrance not found for team " + team);
            }

            if (!homeFieldEntrance.containsKey(team)) {
                throw new IllegalArgumentException("Home entrance not found for team " + team);
            }
        }

        currentTeam = Team.RED;
    }

    // Assign every player to one team if there are enough teams available
    public static void assignTeams() {
        List<Team> availableTeams = new ArrayList<>(Team.getEnabledTeams());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (availableTeams.isEmpty()) {
                break;
            }
            Team team = availableTeams.removeFirst();
            GamePlayer gamePlayer = GamePlayer.getGamePlayer(player.getUniqueId());
            gamePlayer.assignTeam(team);
            playingTeams.add(team);
            players.add(gamePlayer);
            player.sendMessage(Component.text("You are now in team " + team.getDisplayName(), NamedTextColor.GREEN));
        }
    }

    public static void start() {

        MaednMain.setGameState(GameState.IN_GAME);

        //Set up the pieces and update the player inventories
        players.forEach(player -> {

            Player realPlayer = player.getPlayer();

            Team team = player.getTeam();
            List<Location> startLocations = startFields.get(team);
            for (int i = 0; i < startLocations.size(); i++) {
                player.assignPiece(new GamePiece(team, i,
                        ArmorStandUtils.spawnAmorStand(startLocations.get(i),
                                team.getColor(),
                                "Piece " + (i + 1),
                                PlayerUtils.getSkull(realPlayer))));
            }
            player.updateInventory();

            Component titleComponent = Component.text("Let's go!", NamedTextColor.GOLD);
            Component subtitleComponent = Component.text("Don't get angry!");

            realPlayer.showTitle(Title.title(titleComponent, subtitleComponent));
            realPlayer.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1f, 1f));
        });

        updateBossBar();

        Bukkit.broadcast(Component.text("Game started!", NamedTextColor.GREEN, TextDecoration.BOLD));
    }

    public static void movePieceToBoard(GamePlayer player, GamePiece piece) {
        piece.setPosition(startFieldEntrance.get(player.getTeam()));
        piece.setAtStart(false);
        piece.setAtHome(false);
        piece.getArmorStand().teleport(positionToLocation(piece.getPosition()));
        player.updateInventory();
    }

    public static void checkWin() {

        Optional<GamePlayer> winner = players.stream().filter(GamePlayer::isHomeFull).findFirst();

        winner.ifPresent(gamePlayer -> {

            Player realPlayer = gamePlayer.getPlayer();

            Bukkit.broadcast(Component.text(realPlayer.getName() + " is the winner!", NamedTextColor.GREEN, TextDecoration.BOLD));

            Component titleComponent = Component.text("Congratulations!", NamedTextColor.GOLD);
            Component subtitleComponent = Component.text("You have won!", NamedTextColor.GREEN);
            realPlayer.showTitle(Title.title(titleComponent, subtitleComponent));
            realPlayer.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1f, 1f));

            players.stream().filter(player -> !player.getUuid().equals(gamePlayer.getUuid())).forEach(loser -> {

                Component loserTitleComponent = Component.text("You lost!", NamedTextColor.RED);
                Component loserSubtitleComponent = Component.text("Maybe you'll have more luck next time!");
                loser.getPlayer().showTitle(Title.title(loserTitleComponent, loserSubtitleComponent));
                loser.getPlayer().playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.MASTER, 1f, 1f));

            });

            GameBoard.reset();

        });

    }

    public static void takePiece(GamePiece piece) {

        int freePosition;
        for (freePosition = 0; freePosition < homeFields.get(piece.getTeam()).size(); freePosition++) {
            if (getPieceAtStartPosition(freePosition, piece.getTeam()) == null) {
                break;
            }
        }

        piece.setPosition(freePosition);
        piece.setAtStart(true);
        piece.setAtHome(false);
        piece.getArmorStand().teleport(startFields.get(piece.getTeam()).get(piece.getPosition()).toCenterLocation());
        players.forEach(GamePlayer::updateInventory);

    }

    public static boolean isMoveAllowed(GamePlayer player, GamePiece piece, int steps) {

        //a 0-step move is not possible
        if (steps <= 0) {
            return false;
        }

        int homeEntrance = homeFieldEntrance.get(player.getTeam());
        //if a piece is at the start, a move is not possible
        if (piece.isAtStart()) {
            return false;
        } else if (piece.isAtHome()) {
            //Check if a move is possible in home
            if (piece.getPosition() + steps >= homeFields.get(player.getTeam()).size() || getPieceAtHomePosition(piece.getPosition() + steps, piece.getTeam()) != null) {
                return false;
            }
            return true;

        } else {
            //Check if a move is possible on the board
            if (piece.getPosition() + steps > homeEntrance && piece.getPosition() <= homeEntrance) {

                int remainingSteps = steps - (homeEntrance - piece.getPosition()) - 1;

                return remainingSteps >= 0 && remainingSteps < homeFields.get(player.getTeam()).size() && getPieceAtHomePosition(remainingSteps, piece.getTeam()) == null;
            }
        }

        //Check if there is already another piece at the desired field on the board and if it is from the same team
        int positionToCheck = piece.getPosition() + steps;

        if (positionToCheck >= fields.size()) {
            positionToCheck = positionToCheck - fields.size();
        }

        GamePiece pieceAtPosition = getPieceAtFieldPosition(positionToCheck);

        if (pieceAtPosition != null && pieceAtPosition.getTeam() == player.getTeam()) {
            return false;
        }

        return true;
    }

    public static boolean isPieceAtStartAndCanMove(GamePlayer player) {

        GamePiece startPiece = getPieceAtFieldPosition(getStartFieldEntrance().get(player.getTeam()));

        if (startPiece == null) {
            return false;
        }

        if (startPiece.getTeam() != player.getTeam()) {
            return false;
        }

        return isMoveAllowed(player, startPiece, player.getDiceResult());

    }

    public static void nextTurn() {
        while (!playingTeams.contains(currentTeam.getNext())) {
            currentTeam = currentTeam.getNext();
        }

        currentTeam = currentTeam.getNext();
        GamePlayer.getGamePlayerByTeam(currentTeam).resetDice();

        players.forEach(player -> {
            player.updateInventory();
            if (player.getTeam() == currentTeam) {
                player.getPlayer().playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1f, 1f));
            } else {
                player.getPlayer().playSound(Sound.sound(Key.key("entity.enderman.hurt"), Sound.Source.MASTER, 1f, 1f));
            }
        });

        updateBossBar();

        Bukkit.broadcast(Component.text("It's now team ")
                .append(Component.text(currentTeam.getDisplayName(), ColorUtils.colorToTextColor(currentTeam.getColor())))
                .append(Component.text("'s turn")));
    }

    public static void movePiece(GamePlayer player, GamePiece piece, int steps) {

        int homeEntrance = homeFieldEntrance.get(player.getTeam());

        //Check if piece can enter its home
        if (piece.getPosition() + steps > homeEntrance && piece.getPosition() <= homeEntrance) {

            int remainingSteps = steps - (homeEntrance - piece.getPosition()) - 1;
            piece.setPosition(remainingSteps);
            piece.setAtHome(true);
            piece.getArmorStand().teleport(homePositionToLocation(piece.getPosition(), piece.getTeam()));

        } else {

            int newPosition = piece.getPosition() + steps;

            //move piece at home
            if (piece.isAtHome()) {

                piece.setPosition(newPosition);
                piece.getArmorStand().teleport(homePositionToLocation(piece.getPosition(), piece.getTeam()));

            } else {

                //move piece on board
                if (newPosition >= fields.size()) {
                    newPosition = newPosition - fields.size();
                }

                GamePiece pieceAtField = getPieceAtFieldPosition(newPosition);

                if (pieceAtField != null) {
                    takePiece(pieceAtField);
                }

                piece.setPosition(newPosition);
                piece.getArmorStand().teleport(positionToLocation(piece.getPosition()));
            }
        }

        checkWin();
    }

    public static void reset() {

        MaednMain.setGameState(GameState.LOBBY);

        if (gameLoop != null) {
            gameLoop.cancel();
        }

        if (players != null) {
            players.forEach(player -> {
                player.resetPlayer();
                if (bossBar == null) {
                    return;
                }

                Player realPlayer = player.getPlayer();

                if(realPlayer != null && realPlayer.isOnline()) {
                    realPlayer.hideBossBar(bossBar);
                }
            });
        }

        fields = new ArrayList<>();
        startFields = new HashMap<>();
        homeFields = new HashMap<>();
        startFieldEntrance = new HashMap<>();
        homeFieldEntrance = new HashMap<>();
        playingTeams = new ArrayList<>();
        players = new ArrayList<>();

    }

    public static void updateBossBar() {

        Color color = currentTeam.getColor();

        if (bossBar == null) {
            bossBar = BossBar.bossBar(Component.text("It's Team ")
                            .append(Component.text(currentTeam.getDisplayName(), ColorUtils.colorToTextColor(color), TextDecoration.BOLD))
                            .append(Component.text("'s turn!")),
                    1f,
                    ColorUtils.colorToBarColor(color),
                    BossBar.Overlay.PROGRESS);
        } else {
            bossBar.color(ColorUtils.colorToBarColor(color));
            bossBar.name(Component.text("It's Team ")
                    .append(Component.text(currentTeam.getDisplayName(), ColorUtils.colorToTextColor(color), TextDecoration.BOLD))
                    .append(Component.text("'s turn!")));
        }

        players.forEach(player -> player.getPlayer().showBossBar(bossBar));
    }

    public static GamePiece getPieceAtStartPosition(int position, Team team) {
        return players.stream().flatMap(player -> player.getPieces().stream()).filter(piece -> piece.isAtStart() && piece.getTeam() == team && piece.getPosition() == position).findFirst().orElse(null);
    }

    public static GamePiece getPieceAtFieldPosition(int position) {
        return players.stream().flatMap(player -> player.getPieces().stream()).filter(piece -> piece.isInGame() && piece.getPosition() == position).findFirst().orElse(null);
    }

    public static GamePiece getPieceAtHomePosition(int position, Team team) {
        return players.stream().flatMap(player -> player.getPieces().stream()).filter(piece -> piece.isAtHome() && piece.getTeam() == team && piece.getPosition() == position).findFirst().orElse(null);
    }

    public static Location homePositionToLocation(int position, Team team) {
        return homeFields.get(team).get(position).toCenterLocation().add(0, 2, 0);
    }

    public static Location positionToLocation(int position) {
        return fields.get(position).toCenterLocation().add(0, 2, 0);
    }

    public static int getFieldSize() {
        return fields.size();
    }

    public static Team getCurrentTeam() {
        return currentTeam;
    }

    public static List<GamePlayer> getPlayers() {
        return players;
    }

    public static HashMap<Team, Integer> getStartFieldEntrance() {
        return startFieldEntrance;
    }

    public static HashMap<Team, Integer> getHomeFieldEntrance() {
        return homeFieldEntrance;
    }

    public static List<Location> getHomeFields(Team team) {
        return homeFields.get(team);
    }
}
