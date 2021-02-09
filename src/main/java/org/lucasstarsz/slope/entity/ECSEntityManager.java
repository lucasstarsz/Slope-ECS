package org.lucasstarsz.slope.entity;

import java.util.BitSet;
import java.util.Deque;
import java.util.LinkedList;

/** The manager of entities, entity counts, and entity signatures. */
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
     * Initializes the entity manager queue with all possible entity IDs.
     *
     * @param maxEntityCount The maximum amount of entities to allow in the ECS.
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
     *
     * @return The ID of the created entity.
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
     * Destroys the entity of the specified type, invalidating its signature and moving it to the back of the queue.
     *
     * @param entity The ID of the entity to destroy.
     */
    public void destroyEntity(int entity) {
        if (entity > maxEntities) {
            throw new IllegalStateException("Entity " + entity + " out of range.");
        }

        // invalidate the destroyed entity's signature
        entitySignatures[entity].clear();

        // Put the destroyed ID at the back of the queue
        availableEntities.push(entity);
        livingEntityCount--;
    }

    /**
     * Sets the signature of the specified entity to the specified signature.
     *
     * @param entity    The entity to set the signature of.
     * @param signature The signature to set.
     */
    public void setSignature(int entity, BitSet signature) {
        if (entity > maxEntities) {
            throw new IllegalStateException("Entity " + entity + " out of range.");
        }

        // Put this entity's signature into the array
        entitySignatures[entity] = signature;
    }

    /**
     * Gets the signature of the specified entity.
     *
     * @param entity The entity to get the signature for.
     * @return The signature corresponding to the specified entity.
     */
    public BitSet getSignature(int entity) {
        if (entity > maxEntities) {
            throw new IllegalStateException("Entity " + entity + " out of range.");
        }

        // Get this entity's signature from the array
        return entitySignatures[entity];
    }

    /**
     * Gets the count of entities currently alive.
     *
     * @return The count of entities currently alive.
     */
    public int getLivingEntityCount() {
        return livingEntityCount;
    }

    /**
     * Gets the number of remaining entity slots.
     *
     * @return The number of remaining entity slots.
     */
    public int getAvailableEntities() {
        return availableEntities.size();
    }
}
