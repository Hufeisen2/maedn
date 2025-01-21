package dev.hufeisen.maedn;

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public enum Team {

    RED("Red", "red", Color.RED),
    GREEN("Green", "green", Color.GREEN),
    BLUE("Blue", "blue", Color.BLUE),
    YELLOW("Yellow", "yellow", Color.YELLOW);

    private final String displayName;
    private final String key;
    private final Color color;

    Team(String displayName, String key, Color color) {
        this.displayName = displayName;
        this.key = key;
        this.color = color;
    }

    public String getKey() {
        return key;
    }

    public Color getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Team getNext() {
        return switch (this) {
            case RED -> BLUE;
            case BLUE -> GREEN;
            case GREEN -> YELLOW;
            case YELLOW -> RED;
        };
    }

    public static List<Team> getEnabledTeams() {
        FileConfiguration config = MaednMain.getInstance().getConfig();

        return Arrays.stream(values()).filter(team -> {
            int size = config.getInt(team.getKey() + "_start.size");
            return size > 0;
        }).toList();
    }
}
