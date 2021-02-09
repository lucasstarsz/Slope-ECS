package io.github.lucasstarsz;

import io.github.lucasstarsz.component.ECSComponentManager;
import io.github.lucasstarsz.component.IComponent;
import io.github.lucasstarsz.entity.ECSEntityManager;
import io.github.lucasstarsz.system.ECSSystem;
import io.github.lucasstarsz.system.ECSSystemManager;
import io.github.lucasstarsz.util.ECSDefaults;

import java.util.BitSet;
import java.util.LinkedHashMap;

/** The glue holding the entire ECS together. */
public class World {

    /**
     * The maximum number of entities allowed within the manager.
     * <p>
     * This number is the same across all portions of the ECS. To change it, one must call {@link #init(int)} and set
     * the maximum number of entities accordingly. Re-initializing the ECS to change this value <strong>will</strong>
     * reset the entire ECS.
     *
     * @see #init()
     * @see #init(int)
     */
    private int maxEntities;

    /** The manager for entities, entity counts, and entity signatures. */
    private ECSEntityManager entityManager;
    /** The manager for components and component types. */
    private ECSComponentManager componentManager;
    /** The manager of systems and system signatures. */
    private ECSSystemManager systemManager;

    /**
     * Initializes the manager with the default maximum entity count, as {@link ECSDefaults#defaultMaxEntityCount}.
     *
     * @see #init(int)
     */
    public void init() {
        init(ECSDefaults.defaultMaxEntityCount);
    }

    /**
     * Initializes the manager with a specified maximum entity count.
     * <p>
     * This method effectively resets the state of the entire ECS when called.
     *
     * @param maxEntityCount The maximum number of entities to allow within the ECS.
     */
    public void init(int maxEntityCount) {
        if (maxEntityCount < 1) {
            throw new IllegalStateException("Entity count must be at least 1.");
        }

        this.maxEntities = maxEntityCount;
        componentManager = new ECSComponentManager(maxEntities);
        entityManager = new ECSEntityManager(maxEntities);
        systemManager = new ECSSystemManager();
    }

    /**
     * Creates an entity if available, returning the newly created entity.
     *
     * @return The id of the created entity.
     */
    public int createEntity() {
        return entityManager.createEntity();
    }

    /**
     * Destroys the specified entity, and all components tied to it.
     *
     * @param entity The entity to destroy.
     */
    public void destroyEntity(int entity) {
        entityManager.destroyEntity(entity);
        componentManager.entityDestroyed(entity);
        systemManager.entityDestroyed(entity);
    }

    /**
     * Registers the specified class as a possible entity component.
     *
     * @param componentClass The class to register as a possible container for components.
     * @param <T>            The generic type of the class to register as a possible entity component. Uses of {@code T}
     *                       must extend {@code IComponent}.
     */
    public <T extends IComponent> void registerComponent(Class<T> componentClass) {
        componentManager.registerComponent(componentClass);
    }

    /**
     * Binds the specified component to the entity specified.
     *
     * @param entity    The entity to bind a component to.
     * @param component The component to bind.
     * @param <T>       The generic type of the component to be bound. Uses of {@code T} must implement {@code
     *                  IComponent}.
     */
    public <T extends IComponent> void addComponent(int entity, T component) {
        componentManager.addComponent(entity, component);

        BitSet signature = entityManager.getSignature(entity);
        if (signature == null) {
            signature = new BitSet();
        }
        signature.set(componentManager.getComponentType(component.getClass()));

        entityManager.setSignature(entity, signature);
        systemManager.entitySignatureChanged(entity, signature);
    }

    /**
     * Removes the specified component from the specified entity.
     *
     * @param entity         The entity to remove a component from.
     * @param componentClass The class of the component to be removed.
     * @param <T>            The generic type of the class which matches the class of the component to remove. Uses of
     *                       {@code T} must implement {@code IComponent}.
     */
    public <T extends IComponent> void removeComponent(int entity, Class<T> componentClass) {
        componentManager.removeComponent(entity, componentClass);

        BitSet signature = entityManager.getSignature(entity);
        signature.set(componentManager.getComponentType(componentClass), false);
        entityManager.setSignature(entity, signature);

        systemManager.entitySignatureChanged(entity, signature);
    }

    /**
     * Gets the component of the specified class from the specified entity.
     *
     * @param entity         The entity to get the component from.
     * @param componentClass The class of the component to get.
     * @param <T>            The generic type of the class of the component to get. Uses of {@code T} must extend {@code
     *                       IComponent}.
     * @return The component of the entity and class requested.
     */
    public <T extends IComponent> T getComponent(int entity, Class<T> componentClass) {
        return componentManager.getComponent(entity, componentClass);
    }

    /**
     * Gets the component type, as an integer, of the class specified.
     *
     * @param componentClass The class to get the component type of.
     * @param <T>            The generic type of the component to be bound. Uses of {@code T} must implement {@code
     *                       IComponent}.
     * @return The type of the component class, as an integer.
     */
    public <T extends IComponent> int getComponentType(Class<T> componentClass) {
        return componentManager.getComponentType(componentClass);
    }

    /**
     * Creates and registers a {@code ECSSystem} based on the specified class.
     *
     * @param systemClass The class to register as a system.
     * @param <T>         The generic type of the system to create and register. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     * @return The created and registered {@code ECSSystem}.
     */
    public <T extends ECSSystem> T registerSystem(Class<T> systemClass) {
        return registerSystem(systemClass, null);
    }

    /**
     * Creates and registers a {@code ECSSystem} based on the specified class and arguments.
     * <p>
     * For information about the {@code arguments} parameter, see {@link ECSSystemManager#registerSystem(Class,
     * LinkedHashMap)}.
     *
     * @param systemClass The class to register as a system.
     * @param arguments   The arguments to construct the system.
     * @param <T>         The generic type of the system to create and register. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     * @return The created and registered {@code ECSSystem}.
     * @see ECSSystemManager#registerSystem(Class, LinkedHashMap)
     */
    public <T extends ECSSystem> T registerSystem(Class<T> systemClass, LinkedHashMap<Class<?>, Object> arguments) {
        T system = systemManager.registerSystem(systemClass, arguments);
        system.setWorld(this);

        return system;
    }

    /**
     * Sets the system manager's signature to the specified signature.
     *
     * @param systemClass The class of the system in use.
     * @param signature   The signature to set.
     * @param <T>         The generic type of the system to set the signature for. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     */
    public <T extends ECSSystem> void setSystemSignature(Class<T> systemClass, BitSet signature) {
        systemManager.setSignature(systemClass, signature);
    }

    /**
     * Gets the maximum number of entities allowed within the manager.
     *
     * @return The maximum number of entities allowed within the manager.
     */
    public int getMaxEntities() {
        return maxEntities;
    }

    /**
     * Gets the entity manager of the ECS manager.
     *
     * @return The entity manager of the ECS manager.
     */
    public ECSEntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Gets the component manager of the ECS manager.
     *
     * @return The component manager of the ECS manager.
     */
    public ECSComponentManager getComponentManager() {
        return componentManager;
    }

    /**
     * Gets the system manager of the ECS manager.
     *
     * @return The system manager of the ECS manager.
     */
    public ECSSystemManager getSystemManager() {
        return systemManager;
    }
}
