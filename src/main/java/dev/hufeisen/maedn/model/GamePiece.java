package dev.hufeisen.maedn.model;

import dev.hufeisen.maedn.Team;
import org.bukkit.entity.ArmorStand;

public class GamePiece {

    private int position;
    private boolean atStart;
    private boolean atHome;
    private ArmorStand armorStand;
    private Team team;

    public GamePiece(Team team, int position, ArmorStand armorStand) {
        this.team = team;
        this.position = position;
        this.atStart = true;
        this.atHome = false;
        this.armorStand = armorStand;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isAtStart() {
        return atStart;
    }

    public void setAtStart(boolean atStart) {
        this.atStart = atStart;
    }

    public boolean isAtHome() {
        return atHome;
    }

    public boolean isInGame() {
        return !atStart && !atHome;
    }

    public void setAtHome(boolean atHome) {
        this.atHome = atHome;
    }

    public Team getTeam() {
        return team;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }
}
