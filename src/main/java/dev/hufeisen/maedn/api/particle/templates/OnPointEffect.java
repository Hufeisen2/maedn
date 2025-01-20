package dev.hufeisen.maedn.api.particle.templates;

import dev.hufeisen.maedn.api.particle.ParticleEffect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

/**
 * Template class for a point particle effect
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public class OnPointEffect implements ParticleEffect {

    private Location loc;

    private Color color;

    private int size;

    private int count;

    private int time;

    /**
     * <p>
     * Create a new beam effect
     * <p>
     *
     * @param loc   the location the effect will be played
     * @param color the color the particle will have
     * @param size  the size the particles will have
     * @param count the count
     * @param time  the time the particle will be displayed in ticks
     * @since 1.0
     */
    public OnPointEffect(Location loc, Color color, int size, int count, int time) {
        this.loc = loc;
        this.color = color;
        this.size = size;
        this.count = count;
        this.time = time;
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

            double x = loc.getX();
            double z = loc.getZ();
            if (x >= 0) {
                x = x - 0.5;
            } else if (x < 0) {
                x = x + 0.5;
            }
            if (z >= 0) {
                z = z - 0.5;
            } else if (z < 0) {
                z = z + 0.5;
            }


            loc.getWorld().spawnParticle(Particle.DUST, x, loc.getY(), z, count, new Particle.DustOptions(color, size));

            time--;

            return true;
        }
        return false;
    }

}


