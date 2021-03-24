package io.github.lucasstarsz.slopeecs;

import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.component.ComponentManager;
import io.github.lucasstarsz.slopeecs.entity.Entity;
import io.github.lucasstarsz.slopeecs.entity.EntityManager;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import io.github.lucasstarsz.slopeecs.system.SystemManager;
import io.github.lucasstarsz.slopeecs.util.Defaults;

import java.util.List;
import java.util.Map;

/**
 * The main entrypoint to the ECS.
 *<p>
 * <h2>About</h2>
 * The {@code World} in Slope is arguably the most important part of the ECS -- it gives you the ability to access all
 * parts of the ECS at any given point.
 * <p>
 * A {@code World} strings together the 3 key parts of the ECS:
 * <ol>
 *     <li>The {@link EntityManager} -- this stores entity ids, entity objects, and their signatures.</li>
 *     <li>The {@link ComponentManager} -- this stores components in a way easily accessible by entity IDs.</li>
 *     <li>The {@link SystemManager} -- this stores systems, their signatures, and cached entities.</li>
 * </ol>
 * For the best information on how to use the {@code World} class and other aspects of the ECS, check out
 * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki" target="_blank">the wiki</a>.
 *
 * @author Andrew Dey
 * @since 0.1
 * @version 0.2.0
 */
public class World {

    /** The world builder instance for convenient use in building a {@link World}. */
    private static WorldBuilder worldBuilder;

    /** The entity manager instance for the {@link World}. */
    private EntityManager entityManager;
    /** The component manager instance for the {@link World}. */
    private ComponentManager componentManager;
    /** The system manager instance for the {@link World}. */
    private SystemManager systemManager;

    public World() {
        reset();
    }

    public World(int initialEntityAllocation) {
        reset(initialEntityAllocation);
    }

    public void reset() {
        reset(Defaults.initialEntityCount);
    }

    public void reset(int initialEntityAllocation) {
        entityManager = new EntityManager(this, initialEntityAllocation);
        componentManager = new ComponentManager(this);
        systemManager = new SystemManager(this);
    }



    public Entity generateEntity(Component... components) {
        Entity entity = entityManager.getEntity(entityManager.createEntity());
        entity.addComponents(components);
        return entity;
    }

    public Entity[] generateEntities(int entityCount, Component... components) {
        Entity[] entities = entityManager.getEntities(entityManager.createEntities(entityCount));
        for (Entity entity : entities) {
            entity.addComponents(components);
        }

        return entities;
    }

    public int generateEntityID(Component... components) {
        int entity = entityManager.createEntity();

        componentManager.addComponents(entity, components);
        systemManager.entityChanged(entity, entityManager.getSignature(entity));

        return entity;
    }

    public int[] generateEntityIDs(int entityCount, Component... components) {
        int[] entities = entityManager.createEntities(entityCount);

        componentManager.addComponents(entities, components);
        for (int entity : entities) {
            systemManager.entityChanged(entity, entityManager.getSignature(entity));
        }

        return entities;
    }

    public void destroyEntityIDs(int... entities) {
        entityManager.destroyEntities(entities);
        componentManager.entitiesDestroyed(entities);
        systemManager.entitiesDestroyed(entities);
    }



    public void addComponents(int entity, Component... components) {
        if (!entityManager.isAlive(entity)) {
            throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
        }

        componentManager.addComponents(entity, components);
        systemManager.entityChanged(entity, entityManager.getSignature(entity));
    }

    public void addComponents(int[] entities, Component... components) {
        for (int entity : entities) {
            if (!entityManager.isAlive(entity)) {
                throw new IllegalStateException("Entity " + entity + " is not alive in the ECS.");
            }
        }

        componentManager.addComponents(entities, components);
        for (int entity : entities) {
            systemManager.entityChanged(entity, entityManager.getSignature(entity));
        }
    }

    public <T extends Component> T getComponent(int entity, Class<T> componentClass) {
        return componentManager.getComponent(entity, componentClass);
    }

    @SafeVarargs
    public final <T extends Component> void removeComponents(int entity, Class<T>... componentClasses) {
        componentManager.removeComponents(entity, componentClasses);
    }



    public ECSSystem[] addSystems(Map<Class<? extends ECSSystem>, List<?>> systemsAndArgs) {
        return systemManager.addSystems(systemsAndArgs);
    }

    public <T extends ECSSystem> T addSystem(Class<T> systemClass) {
        return systemManager.addSystem(systemClass);
    }

    public <T extends ECSSystem> T addSystem(Class<T> systemClass, List<?> systemArgs) {
        return systemManager.addSystem(systemClass, systemArgs);
    }

    public <T extends ECSSystem> T getSystem(Class<T> systemClass) {
        return systemManager.getSystem(systemClass);
    }

    public <T extends ECSSystem> void removeSystem(Class<T> systemClass) {
        systemManager.removeSystem(systemClass);
    }

    @SafeVarargs
    public final <T extends ECSSystem> void removeSystems(Class<T>... systemClasses) {
        systemManager.removeSystems(systemClasses);
    }



    public <T extends ECSSystem> void runSystem(Class<T> systemClass) {
        systemManager.runSystem(systemClass);
    }

    public void runSystems() {
        systemManager.runSystems();
    }



    public static WorldBuilder init() {
        if (worldBuilder == null) {
            worldBuilder = new WorldBuilder();
        }

        return worldBuilder.reset();
    }

    /** {@return the {@link World}'s entity manager instance} */
    public EntityManager entityManager() {
        return entityManager;
    }

    /** {@return the {@link World}'s component manager instance} */
    public ComponentManager componentManager() {
        return componentManager;
    }

    /** {@return the {@link World}'s system manager instance} */
    public SystemManager systemManager() {
        return systemManager;
    }
}
