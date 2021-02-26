package io.github.lucasstarsz.slopeecs;

import io.github.lucasstarsz.slopeecs.component.ECSComponentManager;
import io.github.lucasstarsz.slopeecs.component.IComponent;
import io.github.lucasstarsz.slopeecs.entity.ECSEntityManager;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import io.github.lucasstarsz.slopeecs.system.ECSSystemBuilder;
import io.github.lucasstarsz.slopeecs.system.ECSSystemManager;
import io.github.lucasstarsz.slopeecs.util.ECSDefaults;

import java.util.BitSet;
import java.util.LinkedHashMap;

/**
 * The class tying the ECS together.
 * <p>
 * <h3>About</h3>
 * The {@code World} in Slope is a convenient abstraction that ties the entire ECS together, providing the full
 * experience and power of the ECS without exposing the extra work of updating each manager.
 * <p>
 * In order to see this class in action, you should check the
 * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki" target="_blank">wiki</a> -- it is the best way to get an
 * understanding of how to make use of Slope.
 *
 * @author Andrew Dey
 */
public class World {

    /**
     * The maximum number of entities allowed within the world.
     * <p>
     * <h3>About</h3>
     * This number is the same across all portions of the ECS. To change it, one must call {@link #init(int)} and set
     * the maximum number of entities accordingly. Re-initializing the ECS to change this value <strong>will</strong>
     * reset the entire ECS.
     *
     * @see #init()
     * @see #init(int)
     */
    private int maxEntities;

    /** The {@code World}'s manager of entities, entity counts, and entity signatures. */
    private ECSEntityManager entityManager;
    /** The {@code World}'s manager of components and component types. */
    private ECSComponentManager componentManager;
    /** The {@code World}'s manager of systems and system signatures. */
    private ECSSystemManager systemManager;

    /**
     * The default -- and only -- constructor for the {@link World} class.
     * <p>
     * <h3>About</h3>
     * Constructing a {@code World} does not prepare or change its state at all -- it is an empty constructor. As such,
     * the class is not yet usable. In order to prepare a {@code World} for use, you must call one of its {@code init}
     * methods.
     *
     * @author Andrew Dey
     * @see #init()
     * @see #init(int)
     */
    public World() {
    }

    /**
     * Initializes the world with the default maximum entity count, as {@link ECSDefaults#defaultMaxEntityCount}.
     * <p>
     * <h3>About</h3>
     * The {@code init} method is one of the most important methods in the Slope {@code World} -- it initializes the
     * internal instances of the entity manager, component manager, and system manager.
     * <p>
     * This method provides the option of omitting specifying the maximum amount of entities to be in the {@code World}
     * at once. Instead, a default value in the form of {@link ECSDefaults#defaultMaxEntityCount} is set as the
     * maximum.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * World world = new World();
     * world.init();
     * System.out.println("Entity count: " + world.getMaxEntities());
     *
     * // This code will print the following:
     * // Entity count: 1000
     * }</pre>
     *
     * @author Andrew Dey
     * @see #init(int)
     */
    public void init() {
        init(ECSDefaults.defaultMaxEntityCount);
    }

    /**
     * Initializes the world with a specified maximum entity count.
     * <p>
     * <h3>About</h3>
     * The {@code init} method is one of the most important methods in the Slope {@code World} -- it initializes the
     * internal instances of the entity manager, component manager, and system manager.
     * <p>
     * This method provides the option of specifying the maximum amount of entities to be in the {@code World} at once
     * through the specified integer parameter. The parameter specified must also be at least 1; if the parameter is
     * below 1, an {@link IllegalStateException} will be thrown, specifying the aforementioned rule.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * World world1 = new World();
     * world1.init(5);
     * System.out.println("World 1 entity count: " + world.getMaxEntities());
     *
     * World world2 = new World();
     * world2.init(45);
     * System.out.println("World 2 entity count: " + world.getMaxEntities());
     *
     * world1.init(100);
     * System.out.println("World 1 entity count, after being re-initialized: " + world.getMaxEntities());
     *
     * world2.init(21);
     * System.out.println("World 2 entity count, after being re-initialized: " + world.getMaxEntities());
     *
     * // World world3 = new World();
     * // this throws an IllegalStateException
     * // world3.init(0);
     *
     * // This code will print the following:
     * // World 1 entity count: 5
     * // World 2 entity count: 45
     * // World 1 entity count, after being re-initialized: 100
     * // World 2 entity count, after being re-initialized: 21
     * }</pre>
     *
     * @param maxEntityCount The maximum number of entities to allow within the ECS.
     * @author Andrew Dey
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
     * If there is space, this method creates and adds an entity to the {@code World}.
     * <p>
     * <h3>About</h3>
     * This is the sole method which enables you to add entities to the {@code World}. An entity gets created within the
     * {@code World}'s {@link #entityManager}, and the component/system managers do not get notified since the state of
     * the entity's components do not change.
     * <p>
     * If you attempt to create a new entity when the maximum entity count has been reached, an {@link
     * IllegalStateException} will be thrown stating this.
     * <p>
     * When working with entities in Slope, the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Entities" target="_blank">entities wiki page</a> is
     * incredibly useful. It contains some of the most relevant information as to how entities work in a bigger
     * picture.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * World world = new World();
     * world.init(3); // sets the maximum amount of entities to 3
     * System.out.println("Entity count: " + world.getEntityManager().getLivingEntityCount());
     *
     * int entity1 = world.createEntity();
     * System.out.println("Entity count: " + world.getEntityManager().getLivingEntityCount());
     *
     * int entity2 = world.createEntity();
     * System.out.println("Entity count: " + world.getEntityManager().getLivingEntityCount());
     *
     * int entity3 = world.createEntity();
     * System.out.println("Entity count: " + world.getEntityManager().getLivingEntityCount());
     *
     * // Throws an IllegalStateException.
     * // 3 is the maximum amount of entities, so a fourth cannot be successfully created.
     * // int entity4 = world.createEntity();
     *
     * // This code will print the following:
     * // Entity count: 0
     * // Entity count: 1
     * // Entity count: 2
     * // Entity count: 3
     * }</pre>
     *
     * @return The id of the created entity.
     * @author Andrew Dey
     */
    public int createEntity() {
        return entityManager.createEntity();
    }

    /**
     * Destroys the specified entity and removes its associated components from the ECS.
     * <p>
     * <h3>About</h3>
     * This is the sole method for removing entities from the {@code World}. The entity is first removed from the {@code
     * World}'s {@link #entityManager}. Then, the component manager and system managers get notified of the entity
     * having been destroyed -- any components the entity had, or systems the entity were in, have those corresponding
     * values removed.
     * <p>
     * If you try to remove an entity that is not alive within the {@code World}'s list of entities, an {@link
     * IllegalStateException} will be thrown. The exception will be thrown for one of a few reasons:
     * <ul>
     *     <li>The entity is negative (entity IDs will always be positive)</li>
     *     <li>The entity is larger than the maximum entity (entity IDs are ordered numbers)</li>
     *     <li>The entity is within the valid range, but is found to be in the list of </li>
     * </ul>
     * <p>
     * When working with entities in Slope, the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Entities" target="_blank">entities wiki page</a> is
     * incredibly useful. It contains some of the most relevant information as to how entities work in a bigger picture.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // Initialization
     * World world = new World();
     * world.init(3);
     * int entity1 = world.createEntity();
     * int entity3 = world.createEntity();
     * int entity3 = world.createEntity();
     * System.out.println("Entity count, before destroying: " + world.getEntityManager().getLivingEntityCount());
     *
     * world.destroy(entity1);
     * System.out.println("Entity count after 1 destroyed: " + world.getEntityManager().getLivingEntityCount());
     *
     * world.destroy(entity2);
     * System.out.println("Entity count after 2 destroyed: " + world.getEntityManager().getLivingEntityCount());
     *
     * world.destroy(entity3);
     * System.out.println("Entity count after 3 destroyed: " + world.getEntityManager().getLivingEntityCount());
     *
     *
     * // This code will print the following:
     * Entity count, before destroying: 3
     * Entity count after 1 destroyed: 2
     * Entity count after 2 destroyed: 1
     * Entity count after 3 destroyed: 0
     * }</pre>
     *
     * @param entity The entity to destroy. This value should be a valid entity that has been created -- and is still
     *               alive -- in the {@link World}.
     * @author Andrew Dey
     */
    public void destroyEntity(int entity) {
        entityManager.destroyEntity(entity);
        componentManager.entityDestroyed(entity);
        systemManager.entityDestroyed(entity);
    }

    /**
     * Registers the specified class as a possible entity component.
     * <p>
     * <h3>About</h3>
     * This method allows components of the specified class to be used in the {@code World}. It tells the {@link
     * #componentManager} to create storage for the specified type so that entities can add components of that type.
     * <p>
     * If you try to add a component that has already been added, an {@link IllegalStateException} will be thrown -- a
     * component class should not be registered more than once.
     * <p>
     * When working with components in Slope, the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Components" target="_blank">components wiki page</a> is
     * incredibly useful. It contains some of the most relevant information as to how components work in a bigger
     * picture.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume classes SomeComponent, AnotherComponent exist
     * World world = new World();
     * world.init(1);
     * int entity = world.createEntity();
     *
     * world.registerComponent(SomeComponent.class);
     *
     * SomeComponent someComponent = new SomeComponent();
     * world.addComponent(entity, someComponent);
     *
     * // AnotherComponent anotherComponent = new AnotherComponent();
     * // world.addComponent(entity, anotherComponent); // throws IllegalStateException - the component has not been registered.
     * }</pre>
     *
     * @param componentClass The class to register as a possible container for components.
     * @param <T>            The generic type of the class to register as a possible entity component. Uses of {@code T}
     *                       must extend {@code IComponent}.
     * @author Andrew Dey
     */
    public <T extends IComponent> void registerComponent(Class<T> componentClass) {
        componentManager.registerComponent(componentClass);
    }

    /**
     * Binds the specified component to the entity specified.
     * <p>
     * <h3>About</h3>
     * This is the sole method allowing you to bind a component (of a type that implements {@link IComponent}) to an
     * entity. (Do note that the component is not <strong>literally</strong> binded to the entity.
     * <p>
     * Any entity can be given any component, as long as these rules are followed:
     * <ul>
     *     <li>The component to add must first be registered in the {@code World}, via {@link
     *     #registerComponent(Class)}. If you fail to do so, an {@link IllegalStateException} will be thrown.</li>
     *     <li>An entity can have up to one component of a given type. The amount of components allowed for an entity is
     *     not limited, but it can only have one instance of that type of component. Trying to add more than one
     *     component of that type will result in an {@link IllegalStateException}.</li>
     * </ul>
     * <p/>
     * Once an entity is given a component, that component will be available for retrieval via {@link
     * #getComponent(int, Class)}, which only requires the entity and the class of the component to retrieve.
     * <p/>
     * If an entity is removed from a component ({@link #removeComponent(int, Class)}), a new one can be added in its
     * place through this same method.
     * <p>
     * When working with components in Slope, the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Components" target="_blank">components wiki page</a> is
     * incredibly useful. It contains some of the most relevant information as to how components work in a bigger
     * picture.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeComponent exists
     * World world = new World();
     * world.init(1);
     * int entity = world.createEntity();
     *
     * // world.addComponent(entity, new SomeComponent); // this throws an IllegalStateException; components must be registered first
     *
     * world.registerComponent(SomeComponent.class);
     *
     * SomeComponent entityComponent = new SomeComponent();
     * world.addComponent(entity, entityComponent);
     *
     * System.out.println("Is the entity from the world the same as the original? " + entityComponent.equals(world.getComponent(entity, SomeComponent.class)));
     *
     * // This code will print the following:
     * // Is the entity from the world the same as the original? true
     * }</pre>
     *
     * @param entity    The entity to bind a component to.
     * @param component The component to bind.
     * @param <T>       The generic type of the component to be bound. Uses of {@code T} must implement {@code
     *                  IComponent}.
     * @author Andrew Dey
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
     * <p>
     * <h3>About</h3>
     * This is the sole method to remove a component (that is aliased to an entity) from the {@code World}. Once a
     * component has been added to the {@code World}, it can then be removed.
     * <p>
     * This method removes the component in the {@link #componentManager}. This change is then passed to the {@link
     * #systemManager}, which checks each of its systems. If the entity no longer meets the requirements for the system,
     * it is removed from that system.
     * <p>
     * A few notes about entities:
     * <ul>
     *     <li>If an entity is destroyed ({@link #destroyEntity(int)}), then its components are also automatically
     *     removed -- the components do not need to be removed manually.</li>
     *     <li>If an entity no longer has any components in the ECS, it is still alive in the ECS.</li>
     * </ul>
     * <p>
     * When working with components in Slope, the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Components" target="_blank">components wiki page</a> is
     * incredibly useful. It contains some of the most relevant information as to how components work in a bigger
     * picture.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeComponent exists
     * World world = new World();
     * world.init(1);
     * world.registerComponent(SomeComponent.class);
     * int entity = world.createEntity();
     *
     * // world.removeComponent(entity, SomeComponent.class); // throws IllegalStateException; the entity doesn't have this component yet
     *
     * world.addComponent(entity, new SomeComponent());
     * world.removeComponent(entity, SomeComponent.class);
     *
     * // world.removeComponent(entity, SomeComponent.class); // throws IllegalStateException; the entity doesn't have this component anymore
     * System.out.println("Is the entity still alive after removing all its components? " + world.getEntityManager().isAlive(entity));
     *
     * // This code prints the following:
     * // Is the entity still alive after removing all its components? true
     * }</pre>
     *
     * @param entity         The entity to remove a component from.
     * @param componentClass The class of the component to be removed.
     * @param <T>            The generic type of the class which matches the class of the component to remove. Uses of
     *                       {@code T} must implement {@code IComponent}.
     * @author Andrew Dey
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
     * <p>
     * <h3>About</h3>
     * This is the sole method in the {@code World} class to get a component associated with an entity. It grabs the
     * entity from the {@link #entityManager}, which grabs the component from its storage of components. That component
     * is then returned.
     * <p>
     * An entity has to have a component in order to get that component. If the entity does not have that component, or
     * the component was removed/entity was destroyed, then an {@link IllegalStateException} will be thrown.
     * <p>
     * When working with components in Slope, the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Components" target="_blank">components wiki page</a> is
     * incredibly useful. It contains some of the most relevant information as to how components work in a bigger
     * picture.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeComponent exists
     * World world = new World();
     * world.init(1);
     * world.registerComponent(SomeComponent.class);
     *
     * int entity = world.createEntity();
     * SomeComponent entityComponent = new SomeComponent();
     * world.addComponent(entity, entityComponent);
     *
     * System.out.println("Is the entity from the world the same as the original? " + entityComponent.equals(world.getComponent(entity, SomeComponent.class)));
     *
     * // This code prints the following:
     * // Is the entity from the world the same as the original? true
     * }</pre>
     *
     * @param entity         The entity to get the component from.
     * @param componentClass The class of the component to get.
     * @param <T>            The generic type of the class of the component to get. Uses of {@code T} must extend {@code
     *                       IComponent}.
     * @return The component of the entity and class requested.
     * @author Andrew Dey
     */
    public <T extends IComponent> T getComponent(int entity, Class<T> componentClass) {
        return componentManager.getComponent(entity, componentClass);
    }

    /**
     * Gets the component type, as an integer, of the class specified.
     * <p>
     * <h3>About</h3>
     * This is the number used in each entity's signature. When an entity has a component added/removed, the entity's
     * signature gets modified. The bit at this method's return value (corresponding to the component added/removed) is
     * set to true/false. This signature is what enables a system to check its signature against an entity's, ensuring
     * only entities with certain components are accepted.
     * <p>
     * The above also allows you to check the entity's signature to see if it has a certain component. If the bit for
     * that component (the bit value is gotten from this method) is set to true, then the entity has that component. If
     * it is false, then it does not have the component.
     * <p>
     * When working with components in Slope, the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Components" target="_blank">components wiki page</a> is
     * incredibly useful. It contains some of the most relevant information as to how components work in a bigger
     * picture.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume classes SomeComponent, AnotherComponent exist
     * World world = new World();
     * world.init(1);
     * world.registerComponent(SomeComponent.class);
     * world.registerComponent(AnotherComponent.class);
     *
     * int entity = world.createEntity();
     * world.addComponent(entity, new SomeComponent());
     *
     * BitSet entitySignature = world.getEntityManager().getSignature(entity);
     * int someComponentBitIndex = world.getComponentType(SomeComponent.class);
     * int anotherComponentBitIndex = world.getComponentType(SomeComponent.class);
     * System.out.println("Does the entity have a SomeComponent component? " + entitySignature.get(someComponentBitIndex));
     * System.out.println("Does the entity have a AnotherComponent component? " + entitySignature.get(anotherComponentBitIndex));
     *
     * // This code prints the following:
     * // Does the entity have a SomeComponent component? true
     * // Does the entity have a AnotherComponent component? false
     * }</pre>
     *
     * @param componentClass The class to get the component type of.
     * @param <T>            The generic type of the component to be bound. Uses of {@code T} must implement {@code
     *                       IComponent}.
     * @return The type of the component class, as an integer.
     * @author Andrew Dey
     */
    public <T extends IComponent> int getComponentType(Class<T> componentClass) {
        return componentManager.getComponentType(componentClass);
    }

    /**
     * Creates and registers a {@code ECSSystem} based on the specified class.
     * <p>
     * <h3>About</h3>
     * This method is part of the more error-prone way of defining a system. If you're looking to learn about how to add
     * systems to Slope, I highly suggest you look at the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Systems" target="_blank">wiki page for systems</a>. It is
     * the best resource for understanding the bigger picture on how things work in Slope.
     * <p>
     * This method instantiates a system and adds it to the ECS, returning a reference to the system. It does not add
     * any components to the system -- these must be defined yourself using the {@link #setSystemSignature(Class,
     * BitSet)} method. Once again, this is the more error-prone version of setting up a system -- for a friendlier
     * version, use the {@link ECSSystemBuilder} class.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeSystem exists
     * World world = new World();
     * world.init(1);
     *
     * SomeSystem someSystem = world.registerSystem(SomeSystem.class);
     * }</pre>
     *
     * @param systemClass The class to register as a system.
     * @param <T>         The generic type of the system to create and register. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     * @return The created and registered {@code ECSSystem}.
     * @author Andrew Dey
     */
    public <T extends ECSSystem> T registerSystem(Class<T> systemClass) {
        return registerSystem(systemClass, null);
    }

    /**
     * Creates and registers a {@code ECSSystem} based on the specified class and arguments.
     * <p>
     * <h3>About</h3>
     * For general information about this method, see {@link #registerSystem(Class)}. For information about the {@code
     * arguments} parameter, see {@link ECSSystemManager#registerSystem(Class, LinkedHashMap)}.
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // Assume classes SomeSystem and CustomClass exist.
     * // Assume the SomeSystem class has a constructor that takes a String, int, and CustomClass instance.
     * World world = new World();
     * world.init(1);
     *
     * LinkedHashMap arguments = new LinkedHashMap<>() {{
     *     put(String.class, "parameter 1");
     *     put(int.class, 42);
     *     put(CustomClass.class, new CustomClass());
     * }};
     *
     * SomeSystem system = world.registerSystem(SomeSystem.class, arguments);
     * }</pre>
     *
     * @param systemClass The class to register as a system.
     * @param arguments   The arguments to construct the system.
     * @param <T>         The generic type of the system to create and register. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     * @return The created and registered {@code ECSSystem}.
     * @author Andrew Dey
     * @see ECSSystemManager#registerSystem(Class, LinkedHashMap)
     */
    public <T extends ECSSystem> T registerSystem(Class<T> systemClass, LinkedHashMap<Class<?>, Object> arguments) {
        T system = systemManager.registerSystem(systemClass, arguments);
        system.setWorld(this);

        return system;
    }

    /**
     * Sets the signature of the specified system class to the specified signature.
     * <p>
     * <h3>About</h3>
     * This is the way to manually set the accepted components for a system -- you get the ability to manually set the
     * bits for the components you want the system to have. The bits in the signature determine what components the
     * system accepts; any bit set to true (you access the bit index for a component using the {@link
     * #getComponentType(Class)} method) will be a requirement for entities to have before being added to the system's
     * entities.
     * <p>
     * This method is part of the more error-prone way of defining a system. If you're looking to learn about how to add
     * systems to Slope, I highly suggest you look at the
     * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Systems" target="_blank">wiki page for systems</a>. It is
     * the best resource for understanding the bigger picture on how things work in Slope.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeSystem exists
     * // assume class SomeComponent exists
     * World world = new World();
     * world.init(1);
     * SomeSystem someSystem = world.registerSystem(SomeSystem.class);
     *
     * BitSet someSystemSignature = new BitSet();
     * someSystemSignature.set(getComponentType(SomeComponent.class)); // sets the bit to true
     * // someSystemSignature.set(getComponentType(SomeComponent.class), true); // you can be explicit about what you set the bit to as well.
     * world.setSystemSignature(SomeSystem.class, someSystemSignature);
     * // now the system only accepts entities with the SomeComponent component
     * }</pre>
     *
     * @param systemClass The class of the system to the set the signature for.
     * @param signature   The signature to set.
     * @param <T>         The generic type of the system to set the signature for. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     * @author Andrew Dey
     */
    public <T extends ECSSystem> void setSystemSignature(Class<T> systemClass, BitSet signature) {
        systemManager.setSignature(systemClass, signature);
    }

    public int getMaxEntities() {
        return maxEntities;
    }

    public ECSEntityManager getEntityManager() {
        return entityManager;
    }

    public ECSComponentManager getComponentManager() {
        return componentManager;
    }

    public ECSSystemManager getSystemManager() {
        return systemManager;
    }
}
