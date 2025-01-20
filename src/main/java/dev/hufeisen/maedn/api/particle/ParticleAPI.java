package dev.hufeisen.maedn.api.particle;

import java.util.ArrayList;
import java.util.List;

/**
 * ParticleAPI main class
 *
 * @author Hufeisen
 * @version 1.0
 * @since 1.0
 */
public class ParticleAPI {

    private static List<ParticleEffect> effectList = new ArrayList<>();

    /**
     * <p>
     * Start to spawn the Particle
     * <p>
     *
     * @param effect The Particle you want to spawn.
     * @since 1.0
     */
    public static void startNewParticleEffect(ParticleEffect effect) {
        effectList.add(effect);
    }

    /**
     * <p>
     * Internal use only
     * <p>
     *
     * @since 1.0
     */
    public static void updateParticle() {

        effectList.removeIf(allEffect -> !allEffect.onRun());

    }

}
