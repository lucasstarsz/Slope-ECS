package io.github.lucasstarsz.slopeecs.entity;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.ECSComponentManager;
import io.github.lucasstarsz.slopeecs.system.ECSSystemManager;

import java.util.BitSet;
import java.util.Deque;
import java.util.LinkedList;

/**
 * The manager of entities, entity counts, and entity signatures.
 * <p>
 * <h3>About</h3>
 * This class is one of three managers (see: {@link ECSSystemManager}, {@link ECSComponentManager}) used within
 * Slope-ECS. It serves the main purpose of storing entities and their signatures, as well as creation/destruction of
 * those entities and their signatures.
 * <p>
 * Considering this is only one of three managers in Slope-ECS, it is better to use the {@link World} class to manage
 * the ECS. In order to see that class in action, you should check the
 * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki" target="_blank">wiki</a> -- it is the best way to get an
 * understanding of how to make use of Slope.
 *
 * @author Andrew Dey
 */
public class ECSEntityManager {

    /** The array of signatures for each entity. */
    private final BitSet[] entitySignatures;
    /** The queue of unused entity IDs. */
    private final Deque<Integer> availableEntities = new LinkedList<>();
    /** Total amount of living entities. */
    private int livingEntityCount;

    /**
     * The maximum number of entities allowed within the entity manager.
     * <p>
     * This number is the same across all portions of the ECS. To change it, one must call {@code ECSCoordinator#init}
     * and set the maximum number of entities manually. Doing this a second time during use of the ECS
     * <strong>will</strong> remove any existing components, and invalidate entity IDs.
     */
    private final int maxEntities;

    /**
     * Constructs the entity manager queue with all possible entity IDs.
     * <p>
     * <h3>About</h3>
     * This constructor requires that you set the maximum entity count, foregoing use of a default value. The value
     * specified must be at least 1.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * ECSEntityManager entityManager = new ECSEntityManager(100);
     * System.out.println("Maximum entities allowed: " + entityManager.getAvailableEntities());
     * System.out.println("Entities currently created: " + entityManager.getLivingEntityCount());
     *
     * // This code will print the following:
     * // Maximum entities allowed: 100
     * // Entities currently created: 0
     * }</pre>
     *
     * @param maxEntityCount The maximum amount of entities to allow in the ECS.
     * @author Andrew Dey
     */
    public ECSEntityManager(int maxEntityCount) {
        this.maxEntities = maxEntityCount;
        entitySignatures = new BitSet[maxEntities];

        for (int i = 0; i < maxEntities; i++) {
            availableEntities.push(i);
        }
    }

    /**
     * Creates an entity in the next available slot, then returns the entity's ID.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#createEntity()}, allowing you to create an entity within Slope-ECS. For
     * more information, see {@link World#createEntity()}.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * ECSEntityManager entityManager = new ECSEntityManager(100);
     * System.out.println("Available entity slots, before creation: " + entityManager.getAvailableEntities());
     * System.out.println("Entities currently created, before creation: " + entityManager.getLivingEntityCount());
     *
     * int entity = entityManager.createEntity();
     *
     * System.out.println("Available entity slots, after creation: " + entityManager.getAvailableEntities());
     * System.out.println("Entities currently created, after creation: " + entityManager.getLivingEntityCount());
     *
     * // This code will print the following:
     * // Available entity slots, before creation: 100
     * // Entities currently created, before creation: 0
     * // Available entity slots, after creation: 99
     * // Entities currently created, after creation: 1
     * }</pre>
     *
     * @return The ID of the created entity.
     * @author Andrew Dey
     */
    public int createEntity() {
        if (livingEntityCount >= maxEntities) {
            throw new IllegalStateException("Maximum number of entities (" + maxEntities + ") was exceeded. No more could be created.");
        }

        // Take an ID from the front of the queue
        int id = availableEntities.pop();
        livingEntityCount++;

        return id;
    }

    /**
     * Destroys the entity of the specified type, invalidating its signature and moving it to the back of the {@link
     * #availableEntities} queue.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#createEntity()}, allowing you to create an entity within Slope-ECS. For
     * more information, see {@link World#createEntity()}.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * ECSEntityManager entityManager = new ECSEntityManager(100);
     * int entity1 = entityManager.createEntity();
     * int entity2 = entityManager.createEntity();
     * int entity3 = entityManager.createEntity();
     *
     * System.out.println("Available entity slots, before destruction: " + entityManager.getAvailableEntities());
     * System.out.println("Entities currently created, before destruction: " + entityManager.getLivingEntityCount());
     *
     * entityManager.destroyEntity(entity1);
     *
     * System.out.println("Available entity slots, after destruction 1: " + entityManager.getAvailableEntities());
     * System.out.println("Entities currently created, after destruction 1: " + entityManager.getLivingEntityCount());
     *
     * entityManager.destroyEntity(entity2);
     *
     * System.out.println("Available entity slots, after destruction 2: " + entityManager.getAvailableEntities());
     * System.out.println("Entities currently created, after destruction 2: " + entityManager.getLivingEntityCount());
     *
     * entityManager.destroyEntity(entity3);
     *
     * System.out.println("Available entity slots, after destruction 3: " + entityManager.getAvailableEntities());
     * System.out.println("Entities currently created, after destruction 3: " + entityManager.getLivingEntityCount());
     *
     * // This code will print the following:
     * // Available entity slots, before destruction: 97
     * // Entities currently created, before destruction: 3
     * // Available entity slots, after destruction 1: 98
     * // Entities currently created, after destruction 1: 2
     * // Available entity slots, after destruction 2: 99
     * // Entities currently created, after destruction 2: 1
     * // Available entity slots, after destruction 3: 100
     * // Entities currently created, after destruction 3: 0
     * }</pre>
     *
     * @param entity The ID of the entity to destroy.
     * @author Andrew Dey
     */
    public void destroyEntity(int entity) {
        if (!isAlive(entity)) {
            throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
        }

        // invalidate the destroyed entity's signature
        if (entitySignatures[entity] != null) {
            entitySignatures[entity].clear();
        }

        // Put the destroyed ID at the back of the queue
        availableEntities.push(entity);
        livingEntityCount--;
    }

    public void setSignature(int entity, BitSet signature) {
        if (!isAlive(entity)) {
            throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
        }

        // Put this entity's signature into the array
        entitySignatures[entity] = signature;
    }

    public BitSet getSignature(int entity) {
        if (!isAlive(entity)) {
            throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
        }

        // Get this entity's signature from the array
        return entitySignatures[entity];
    }

    public boolean isAlive(int entity) {
        if (entity < 0 || entity > maxEntities) {
            throw new IllegalStateException("Entity " + entity + " out of range.");
        }

        for (int e : availableEntities) {
            if (e == entity) {
                return false;
            }
        }

        return true;
    }

    public int getLivingEntityCount() {
        return livingEntityCount;
    }

    public int getAvailableEntityCount() {
        return availableEntities.size();
    }
}
