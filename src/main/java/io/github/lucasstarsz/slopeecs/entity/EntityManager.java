package io.github.lucasstarsz.slopeecs.entity;

import io.github.lucasstarsz.slopeecs.World;

import java.util.*;

public class EntityManager {

    private final World world;

    private final Map<Integer, BitSet> entitySignatures;

    private final Deque<Integer> availableEntities = new LinkedList<>();

    private final Map<Integer, Entity> entities;

    private int livingEntityCount;
    private int lastEntityCreated;

    public EntityManager(World world, int initialEntityCount) {
        this.world = world;

        entities = new LinkedHashMap<>(initialEntityCount);
        entitySignatures = new LinkedHashMap<>(initialEntityCount);

        for (int i = 0; i < initialEntityCount; i++) {
            availableEntities.push(i);
        }
    }

    public int[] createEntities(int entityCount) {
        int[] generatedEntities = new int[entityCount];

        // if needed, make space for generating entities
        if (availableEntities.size() < entityCount) {
            for (int i = lastEntityCreated + 1; i < lastEntityCreated + entityCount; i++) {
                availableEntities.push(i);
            }
        }

        for (int i = 0; i < entityCount; i++) {
            int entity = availableEntities.pop();
            generatedEntities[i] = entity;
            entities.put(entity, Entity.create(world, entity));
        }
        livingEntityCount += entityCount;

        return generatedEntities;
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

    public void setEntitySignatures(Map<Integer, BitSet> entitiesAndSignatures) {
        // ensure entities are alive
        for (var entitySignaturePair : entitiesAndSignatures.entrySet()) {
            if (!isAlive(entitySignaturePair.getKey())) {
                throw new IllegalStateException("Entity " + entitySignaturePair.getKey() + " is not alive in the ECS.");
            }
        }

        entitySignatures.putAll(entitiesAndSignatures);
    }


    public int createEntity() {
        // Take an ID from the front of the queue
        lastEntityCreated = availableEntities.pop();
        livingEntityCount++;

        // fill available entities if none are available
        if (availableEntities.size() == 0) {
            availableEntities.push(lastEntityCreated + 1);
        }

        return lastEntityCreated;
    }

    public void destroyEntity(int entity) {
        if (!isAlive(entity)) {
            throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
        }

        // invalidate the destroyed entity's signature
        if (entitySignatures.get(entity) != null) {
            entitySignatures.get(entity).clear();
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

    public int getAvailableEntityCount() {
        return availableEntities.size();
    }
}
