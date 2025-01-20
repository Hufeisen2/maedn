package dev.hufeisen.maedn.api.particle.templates;


import dev.hufeisen.maedn.api.particle.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Template class for a beam particle effect
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public class BeamEffect implements ParticleEffect {

    private Location start;

    private Player startPlayer;

    private Location end;

    private Particle particle;

    private Particle.DustOptions dustOptions;

    private int count;

    private int time;


    /**
     * <p>
     * Create a new beam effect
     * <p>
     *
     * @param start    the start location
     * @param end      the end location
     * @param particle the particle that will be displayed
     * @param count    the count
     * @param time     the time the particle will be displayed in ticks
     * @since 1.0
     */
    public BeamEffect(Location start, Location end, Particle particle, int count, int time) {

        this.start = start;
        this.end = end;
        this.particle = particle;
        this.count = count;
        this.time = time;

    }

    /**
     * <p>
     * Create a new beam effect
     * <p>
     *
     * @param start       the start location
     * @param end         the end location
     * @param particle    the particle that will be displayed
     * @param dustOptions The dust options if the particle is a dust
     * @param count       the count
     * @param time        the time the particle will be displayed in ticks
     * @since 1.0
     */
    public BeamEffect(Location start, Location end, Particle particle, Particle.DustOptions dustOptions, int count, int time) {

        this.start = start;
        this.end = end;
        this.particle = particle;
        this.dustOptions = dustOptions;
        this.count = count;
        this.time = time;

    }

    /**
     * <p>
     * Create a new beam effect
     * <p>
     *
     * @param startPlayer the start player (will change the start location automatically to the player location
     * @param end         the end location
     * @param particle    the particle that will be displayed
     * @param count       the count
     * @param time        the time the particle will be displayed in ticks
     * @since 1.0
     */
    public BeamEffect(Player startPlayer, Location end, Particle particle, int count, int time) {

        this.startPlayer = startPlayer;
        this.end = end;
        this.particle = particle;
        this.count = count;
        this.time = time;

    }

    /**
     * <p>
     * Create a new beam effect
     * <p>
     *
     * @param startPlayer the start player (will change the start location automatically to the player location
     * @param end         the end location
     * @param particle    the particle that will be displayed
     * @param dustOptions The dust options if the particle is a dust
     * @param count       the count
     * @param time        the time the particle will be displayed in ticks
     * @since 1.0
     */
    public BeamEffect(Player startPlayer, Location end, Particle particle, Particle.DustOptions dustOptions, int count, int time) {

        this.startPlayer = startPlayer;
        this.end = end;
        this.particle = particle;
        this.dustOptions = dustOptions;
        this.count = count;
        this.time = time;

    }

    /**
     * <p>
     * Set the start location of the effect
     * <p>
     *
     * @since 1.0
     */
    public void setStartLocation(Location start) {
        this.start = start;
    }

    /**
     * <p>
     * Set the end location of the effect
     * <p>
     *
     * @since 1.0
     */
    public void setEndLocation(Location end) {
        this.end = end;
    }

    /**
     * <p>
     * Will run every tick if the particle is played. For internal use only
     * <p>
     *
     * @return true if the method will run again and false if the effect is ended.
     * @since 1.0
     */
    @Override
    public boolean onRun() {

        if (time != 0) {

            if (startPlayer != null) {
                start = startPlayer.getLocation().add(0, 1.7, 0);
            }

            Vector vector = getDirectionBetweenLocations(start, end).normalize();
            Location tempLocation = start.clone();

            for (double i = 0; i <= start.distance(end); i += 0.3) {
                tempLocation.add(vector.clone().multiply(i));
                if (dustOptions != null) {
                    tempLocation.getWorld().spawnParticle(particle, tempLocation, count, dustOptions);
                } else {
                    tempLocation.getWorld().spawnParticle(particle, tempLocation, count);
                }
                tempLocation.subtract(vector.clone().multiply(i));
            }

            time--;
            return true;

        }

        return false;

    }

    /**
     * <p>
     * Get the direction between two positions
     * <p>
     *
     * @param start the start location
     * @param end   the end location
     * @return the direction
     * @since 1.0
     */
    public static Vector getDirectionBetweenLocations(Location start, Location end) {
        Vector from = start.toVector();
        Vector to = end.toVector();
        return to.subtract(from);
    }
}
