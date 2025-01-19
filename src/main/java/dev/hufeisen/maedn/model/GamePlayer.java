package dev.hufeisen.maedn.model;

import java.util.HashMap;
import java.util.UUID;

public class GamePlayer {

    private static HashMap<String, GamePlayer> players = new HashMap<>();

    private boolean setupMode;
    private String setupSelection = "fields";

    public GamePlayer() {

    }

    public void setSetupMode(boolean setupMode) {
        this.setupMode = setupMode;
    }

    public boolean isSetupMode() {
        return setupMode;
    }

    public static GamePlayer getGamePlayer(UUID uuid) {
        if(!players.containsKey(uuid.toString())) {
            players.put(uuid.toString(), new GamePlayer());
        }
        return players.get(uuid.toString());
    }

    public String getSetupSelection() {
        return setupSelection;
    }

    public void setSetupSelection(String setupSelection) {
        this.setupSelection = setupSelection;
    }
}
