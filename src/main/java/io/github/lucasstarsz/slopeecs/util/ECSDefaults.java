package io.github.lucasstarsz.slopeecs.util;

import io.github.lucasstarsz.slopeecs.World;

/**
 * Default values for the ECS.
 * <p>
 * <h3>About</h3>
 * This class provides default values used across the entire ECS -- specifically when the {@link World} class is
 * initialized without specifying the maximum entity amount (see: {@link World#init()}.
 *
 * @author Andrew Dey
 */
public class ECSDefaults {
    /** The default maximum number of entities allowed. */
    public static final int defaultMaxEntityCount = 1000;
}
