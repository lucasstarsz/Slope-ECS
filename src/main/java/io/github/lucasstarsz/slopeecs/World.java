package io.github.lucasstarsz.slopeecs;

import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.component.ComponentManager;
import io.github.lucasstarsz.slopeecs.entity.Entity;
import io.github.lucasstarsz.slopeecs.entity.EntityManager;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import io.github.lucasstarsz.slopeecs.system.ECSSystemManager;
import io.github.lucasstarsz.slopeecs.util.Defaults;

import java.util.List;
import java.util.Map;


public class World {

    private static WorldBuilder worldBuilder;

    private EntityManager entityManager;
    private ComponentManager componentManager;
    private ECSSystemManager systemManager;

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
        systemManager = new ECSSystemManager(this);
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



    public static WorldBuilder init() {
        if (worldBuilder == null) {
            worldBuilder = new WorldBuilder();
        }

        return worldBuilder.reset();
    }

//
//
//    public void init() {
//        init(ECSDefaults.defaultMaxEntityCount);
//    }
//
//
//    public void init(int maxEntityCount) {
//        if (maxEntityCount < 1) {
//            throw new IllegalStateException("Entity count must be at least 1.");
//        }
//
//        this.maxEntities = maxEntityCount;
//        componentManager = new ComponentManager(maxEntities);
//        entityManager = new EntityManager(maxEntities);
//        systemManager = new ECSSystemManager();
//    }
//
//
//    public int createEntity() {
//        return entityManager.createEntity();
//    }
//
//
//    public void destroyEntity(int entity) {
//        entityManager.destroyEntity(entity);
//        componentManager.entityDestroyed(entity);
//        systemManager.entityDestroyed(entity);
//    }
//
//
//    public <T extends Component> void registerComponent(Class<T> componentClass) {
//        componentManager.registerComponent(componentClass);
//    }
//
//
//    public <T extends Component> void addComponent(int entity, T component) {
//        componentManager.addComponent(entity, component);
//
//        BitSet signature = entityManager.getSignature(entity);
//        if (signature == null) {
//            signature = new BitSet();
//        }
//        signature.set(componentManager.getComponentType(component.getClass()));
//
//        entityManager.setSignature(entity, signature);
//        systemManager.entitySignatureChanged(entity, signature);
//    }
//
//
//    public <T extends Component> void removeComponent(int entity, Class<T> componentClass) {
//        componentManager.removeComponent(entity, componentClass);
//
//        BitSet signature = entityManager.getSignature(entity);
//        signature.set(componentManager.getComponentType(componentClass), false);
//        entityManager.setSignature(entity, signature);
//
//        systemManager.entitySignatureChanged(entity, signature);
//    }
//
//
//    public <T extends Component> T getComponent(int entity, Class<T> componentClass) {
//        return componentManager.getComponent(entity, componentClass);
//    }
//
//
//    public <T extends Component> int getComponentType(Class<T> componentClass) {
//        return componentManager.getComponentType(componentClass);
//    }
//
//
//    public <T extends ECSSystem> T registerSystem(Class<T> systemClass) {
//        return registerSystem(systemClass, null);
//    }
//
//
//    public <T extends ECSSystem> T registerSystem(Class<T> systemClass, LinkedHashMap<Class<?>, Object> arguments) {
//        T system = systemManager.registerSystem(systemClass, arguments);
//
//        /* Add the system's components */
//        BitSet systemSignature = new BitSet();
//        for (Class<? extends Component> component : system.getComponentsList()) {
//            if (!componentManager.isComponentRegistered(component)) {
//                componentManager.registerComponent(component);
//            }
//
//            systemSignature.set(getComponentType(component));
//        }
//        systemManager.setSignature(systemClass, systemSignature);
//
//        return system;
//    }
//
//
//    public <T extends ECSSystem> void setSystemSignature(Class<T> systemClass, BitSet signature) {
//        systemManager.setSignature(systemClass, signature);
//    }
//
//    public void runSystems() {
//        systemManager.runSystems(this);
//    }
//
//    public <T extends ECSSystem> void runSystem(Class<T> systemClass) {
//        systemManager.runSystem(this, systemClass);
//    }
//
//    public int getMaxEntities() {
//        return maxEntities;
//    }
//
    public EntityManager entityManager() {
        return entityManager;
    }

    public ComponentManager componentManager() {
        return componentManager;
    }

    public ECSSystemManager systemManager() {
        return systemManager;
    }
}
