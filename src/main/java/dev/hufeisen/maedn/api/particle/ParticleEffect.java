package dev.hufeisen.maedn.api.particle;

/**
 * Create a new Particle.
 *
 * @author BusinessBaum
 * @version 1.0
 * @since 1.0
 */

public interface ParticleEffect {

    /**
     * <p>
     * Create the Runnable for your Particle
     * <p>
     *
     * @return True: continue Particle / False: Stop Particle
     * @since 1.0
     */
    boolean onRun();

}
