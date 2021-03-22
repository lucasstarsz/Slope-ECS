package io.github.lucasstarsz.slopeecs.entity;

import io.github.lucasstarsz.slopeecs.World;

import java.util.*;

public class EntityManager {

    private final World world;

    private final Deque<Integer> entityQueue;
    private final Map<Integer, Entity> entities;
    private final Map<Integer, Entity> deadEntities;
    private final Map<Integer, BitSet> entitySignatures;

    private int lastEntityCreated;
    private int highestEntityIDCreated;
    private int livingEntityCount;


    public EntityManager(World world, int initialEntityCount) {
        this.world = world;

        entityQueue = new ArrayDeque<>(initialEntityCount);
        entities = new LinkedHashMap<>(initialEntityCount);
        deadEntities = new LinkedHashMap<>();
        entitySignatures = new LinkedHashMap<>(initialEntityCount);

        for (int i = 0; i < initialEntityCount; i++) {
            entityQueue.push(i);
        }
    }

    public int[] createEntities(int entityCount) {
        int[] generatedEntities = new int[entityCount];

        for (int i = 0; i < generatedEntities.length; i++) {
            if (entityQueue.size() > 0) {
                // Take an entity ID from the front of the queue
                lastEntityCreated = entityQueue.pop();
                generatedEntities[i] = lastEntityCreated;

                entities.put(lastEntityCreated, Entity.create(world, lastEntityCreated));
                highestEntityIDCreated = Math.max(highestEntityIDCreated, lastEntityCreated);
            } else if (deadEntities.size() == 0) {
                // Add new entity to queue
                entityQueue.push(++highestEntityIDCreated);
                lastEntityCreated = entityQueue.pop();
                generatedEntities[i] = lastEntityCreated;

                entities.put(lastEntityCreated, Entity.create(world, lastEntityCreated));
            } else {
                // Take entity from dead entities
                Entity entity = deadEntities.remove(deadEntities.keySet().iterator().next());
                lastEntityCreated = entity.id();
                generatedEntities[i] = lastEntityCreated;

                entities.put(lastEntityCreated, entity);
                highestEntityIDCreated = Math.max(highestEntityIDCreated, lastEntityCreated);
            }

            setSignature(lastEntityCreated, new BitSet());
        }

        livingEntityCount += entityCount;
        return generatedEntities;
    }

    public int createEntity() {
        if (entityQueue.size() > 0) {
            // Take an entity ID from the front of the queue
            lastEntityCreated = entityQueue.pop();

            entities.put(lastEntityCreated, Entity.create(world, lastEntityCreated));
            highestEntityIDCreated = Math.max(highestEntityIDCreated, lastEntityCreated);
        } else if (deadEntities.size() == 0) {
            // Add new entity to queue
            entityQueue.push(++highestEntityIDCreated);
            lastEntityCreated = entityQueue.pop();

            entities.put(lastEntityCreated, Entity.create(world, lastEntityCreated));
        } else {
            // Take entity from dead entities
            Entity entity = deadEntities.remove(deadEntities.keySet().iterator().next());
            lastEntityCreated = entity.id();

            entities.put(lastEntityCreated, entity);
            highestEntityIDCreated = Math.max(highestEntityIDCreated, lastEntityCreated);
        }

        // add entity signature
        setSignature(lastEntityCreated, new BitSet());
        livingEntityCount++;

        return lastEntityCreated;
    }

    public Entity[] getEntities(int... entityIDs) {
        for (int entity : entityIDs) {
            if (!isAlive(entity)) {
                throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
            }
        }

        Entity[] grabbedEntities = new Entity[entityIDs.length];
        for (int i = 0; i < grabbedEntities.length; i++) {
            grabbedEntities[i] = entities.get(entityIDs[i]);
        }

        return grabbedEntities;
    }


    public Entity getEntity(int entityID) {
        if (!isAlive(entityID)) {
            throw new IllegalStateException("Entity " + entityID + " is not alive in the ECS.");
        }

        return entities.get(entityID);
    }

    public void destroyEntity(int destroyedEntity) {
        if (!isAlive(destroyedEntity)) {
            throw new IllegalStateException("Entity " + destroyedEntity + " is not alive in the ECS.");
        }

        // transfer dead entity
        deadEntities.put(destroyedEntity, entities.remove(destroyedEntity));

        // invalidate the destroyed entity's signature
        if (entitySignatures.get(destroyedEntity) != null) {
            entitySignatures.get(destroyedEntity).clear();
        }

        // Put the destroyed ID at the back of the queue
        entityQueue.push(destroyedEntity);
        livingEntityCount--;
    }

    public void destroyEntities(int... destroyedEntities) {
        if (destroyedEntities.length > 1000) {
            Arrays.stream(destroyedEntities).parallel().forEach(entity -> {
                if (!isAlive(entity)) {
                    throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
                }

                // transfer dead entity
                deadEntities.put(entity, entities.remove(entity));

                // invalidate the destroyed entity's signature
                if (entitySignatures.get(entity) != null) {
                    entitySignatures.get(entity).clear();
                }

                // Put the destroyed ID at the back of the queue
                entityQueue.push(entity);
                livingEntityCount--;
            });
        } else {
            for (int entity : destroyedEntities) {
                if (!isAlive(entity)) {
                    throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
                }

                // transfer dead entity
                deadEntities.put(entity, entities.remove(entity));

                // invalidate the destroyed entity's signature
                if (entitySignatures.get(entity) != null) {
                    entitySignatures.get(entity).clear();
                }

                // Put the destroyed ID at the back of the queue
                entityQueue.push(entity);
                livingEntityCount--;
            }
        }
    }

    public void setSignature(int entity, BitSet signature) {
        if (!isAlive(entity)) {
            throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
        }

        // Put this entity's signature into the array
        entitySignatures.put(entity, signature);
    }

    public BitSet getSignature(int entity) {
        if (!isAlive(entity)) {
            throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
        }

        // Get this entity's signature from the array
        return entitySignatures.get(entity);
    }

    public boolean isAlive(int entity) {
        return entities.containsKey(entity);
    }

    public int getLivingEntityCount() {
        return livingEntityCount;
    }

    public int getUnusedEntityCount() {
        return entityQueue.size();
    }
}
