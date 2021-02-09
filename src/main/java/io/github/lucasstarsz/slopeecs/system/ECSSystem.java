package io.github.lucasstarsz.slopeecs.system;

import io.github.lucasstarsz.slopeecs.World;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * The class all systems in this ECS derive from.
 * <p>
 * This class defines the entities available to the system, and the manager they derive from on creation within the
 * ECS.
 * <p>
 * However, the current system has one caveat. Any class implementing {@code ECSSystem} <strong>must</strong> either
 * have a no-arguments constructor, specify no constructor, or (if it has the need for a constructor with arguments)
 * specify the arguments in the secondary method for registering that system, found here: {@link
 * ECSSystemManager#registerSystem(Class, LinkedHashMap)}
 */
public class ECSSystem {
    /** The entities available in this system. */
    protected Set<Integer> entities;
    /** The manager of this system. */
    protected World world;

    /** Instantiates a new {@code ECSSystem}. */
    public ECSSystem() {
        entities = new HashSet<>();
    }

    /**
     * Sets the ecs world for the system.
     *
     * @param world The ecs world acting as the manager of the system.
     */
    public void setWorld(World world) {
        this.world = world;
    }
}
