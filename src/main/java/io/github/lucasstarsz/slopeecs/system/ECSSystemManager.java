package io.github.lucasstarsz.slopeecs.system;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.ECSComponentManager;
import io.github.lucasstarsz.slopeecs.component.IComponent;
import io.github.lucasstarsz.slopeecs.entity.ECSEntityManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The manager of systems and system signatures.
 * <p>
 * <h3>About</h3>
 * This class is one of three managers (see: {@link ECSEntityManager}, {@link ECSComponentManager}) used within
 * Slope-ECS. It serves the main purpose of storing systems and their signatures, as well as updating those systems
 * based on changes to entity signatures.
 * <p>
 * Considering this is only one of three managers in Slope-ECS, it is better to use the {@link World} class to manage
 * the ECS. In order to see that class in action, you should check the
 * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki" target="_blank">wiki</a> -- it is the best way to get an
 * understanding of how to make use of Slope.
 */
public class ECSSystemManager {

    /** The map of the signatures of each system. */
    private final Map<String, BitSet> systemSignatures = new HashMap<>();
    /** The map of signatures. */
    private final Map<String, ECSSystem> systems = new HashMap<>();

    /**
     * Registers the specified class as a system in the manager, returning a new system of type T.
     * <p>
     * <h3>About</h3>
     * This is not a method intended to be directly used -- for actual usage, see {@link World#registerSystem(Class)}.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeSystem exists
     * ECSSystemManager systemManager = new ECSSystemManager();
     * SomeSystem someSystem = systemManager.registerSystem(SomeSystem.class);
     * }</pre>
     *
     * @param systemClass The class to register as a system.
     * @param <T>         The generic type of the system to create and register. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     * @return The newly created system.
     */
    public <T extends ECSSystem> T registerSystem(Class<T> systemClass) {
        return registerSystem(systemClass, null);
    }

    /**
     * Registers the specified class as a system in the manager, returning a new system of type T, constructed with the
     * specified arguments.
     * <p>
     * <h3>About</h3>
     * This is not a method intended to be directly used -- for actual usage, see {@link World#registerSystem(Class,
     * LinkedHashMap)}.
     * <p>
     * The arguments for construct the system are formatted as a {@code java.util.LinkedHashMap}. Specifically a {@code
     * LinkedHashMap}, in order to retain the order of the arguments you put in. Each entry in the map must be formatted
     * such that:
     * <ul>
     *     <li>The {@code key} is the class of the argument.</li>
     *     <li>The {@code value} is the actual argument.</li>
     * </ul>
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // Assume classes SomeSystem and CustomClass exist.
     * // Assume the SomeSystem class has a constructor that takes a String, int, and CustomClass instance.
     * ECSSystemManager systemManager = new ECSSystemManager();
     *
     * LinkedHashMap arguments = new LinkedHashMap<>() {{
     *     put(String.class, "parameter 1");
     *     put(int.class, 42);
     *     put(CustomClass.class, new CustomClass());
     * }};
     *
     * SomeSystem system = systemManager.registerSystem(SomeSystem.class, arguments);
     * }</pre>
     *
     * @param systemClass The class to register as a system.
     * @param arguments   The arguments to construct the system.
     * @param <T>         The generic type of the system to create and register. Uses of {@code T} must extend {@code
     *                    ECSSystem}.
     * @return The newly created system.
     */
    public <T extends ECSSystem> T registerSystem(Class<T> systemClass, LinkedHashMap<Class<?>, Object> arguments) {
        String typeName = systemClass.getTypeName();
        if (systems.get(typeName) != null) {
            throw new IllegalStateException("System with class " + typeName + " is already registered.");
        }

        // Create a reference to the system and return it so it can be used externally
        T system;

        // use reflection to instantiate the system
        try {
            if (arguments == null) {
                system = systemClass.getDeclaredConstructor().newInstance();
            } else {
                Constructor<T> constructor = systemClass.getDeclaredConstructor(arguments.keySet().toArray(new Class[0]));
                constructor.setAccessible(true);
                system = constructor.newInstance(arguments.values().toArray());
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }

        systems.put(typeName, system);
        return system;
    }

    /**
     * Sets the signature for the specified system class to the specified signature.
     * <p>
     * <h3>About</h3>
     * For the best information on this method, see {@link World#setSystemSignature(Class, BitSet)} -- that method calls
     * this method, so the parameters are the same.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeSystem exists
     * ECSSystemManager systemManager = new ECSSystemManager();
     * SomeSystem someSystem = systemManager.registerSystem(SomeSystem.class);
     *
     * BitSet someSystemSignature = new BitSet();
     * someSystemSignature.set(getComponentType(SomeComponent.class)); // sets the bit to true
     * // someSystemSignature.set(getComponentType(SomeComponent.class), true); // you can be explicit about what you set the bit to as well.
     * systemManager.setSignature(SomeSystem.class, someSystemSignature);
     * // now the system only accepts entities with the SomeComponent component
     * }</pre>
     *
     * @param signatureClass The class whose signature is to be set.
     * @param signature      The signature.
     * @param <T>            The generic type of the class whose signature is to be set. Uses of {@code T} must extend
     *                       {@code ECSSystem}.
     */
    public <T extends ECSSystem> void setSignature(Class<T> signatureClass, BitSet signature) {
        String typeName = signatureClass.getTypeName();

        if (systems.get(typeName) == null) {
            throw new IllegalStateException("System with class " + typeName + " was used before it was registered.");
        }

        // Set the signature for this system
        systemSignatures.put(typeName, signature);
    }

    /**
     * Erases a destroyed entity from all system lists.
     * <p>
     * <h3>About</h3>
     * This method is directly called by {@link World#destroyEntity(int)}, as part of the updating of systems to ensure
     * the dead entity is removed. It goes through each system, and removes the entity from its {@code Set} of
     * entities.
     * <p>
     * If you are working with the {@link World} class, this method should not be called directly. The {@code World}
     * handles this internally.
     * <p>
     * <h3>Example Usages</h3>
     * This example specifically takes the approach of using the {@link World} class, as managing the {@link
     * ECSSystemManager} and systems yourself is <strong>not</strong> recommended.
     * <pre>{@code
     * // assume classes SomeSystem, SomeComponent exist
     * World world = new World();
     * world.init(1);
     * world.registerComponent(SomeComponent.class);
     *
     * int entity = world.createEntity();
     * SomeComponent entityComponent = new SomeComponent();
     * world.addComponent(entity, entityComponent);
     *
     * SomeSystem someSystem = new ECSSystemBuilder<>(world, SomeSystem.class)
     *             .withComponent(SomeComponent.class)
     *             .build();
     *
     * System.out.println("Entities in SomeSystem, before destruction: " + someSystem.getEntityCount());
     *
     * // inside this method, entityDestroyed(entity) is called
     * world.destroyEntity(entity);
     *
     * System.out.println("Entities in SomeSystem, after destruction: " + someSystem.getEntityCount());
     *
     * // This code prints the following:
     * // Entities in SomeSystem, before destruction: 1
     * // Entities in SomeSystem, after destruction: 0
     * }</pre>
     *
     * @param entity The entity that was destroyed.
     */
    public void entityDestroyed(int entity) {
        for (Map.Entry<String, ECSSystem> entry : systems.entrySet()) {
            entry.getValue().entities.remove(entity);
        }
    }

    /**
     * Notifies each system in the manager that the specified entity's signature has changed.
     * <p>
     * <h3>About</h3>
     * This method is directly called by {@link World#addComponent(int, IComponent)} and {@link
     * World#removeComponent(int, Class)}, as part of updating the systems every time an entity's signature changes.
     * <p>
     * When an entity is passed into this method, its signature is checked against each system's signature. If the
     * system's signature cannot be derived from within the entity's signature -- that is, a bitwise AND operation is
     * performed to check if the system signature is a subset of the entity signature.
     * <p>
     * <h3>Example Usages</h3>
     * This example specifically takes the approach of using the {@link World} class, as managing the {@link
     * ECSSystemManager} and systems yourself is <strong>not</strong> recommended.
     * <pre>{@code
     * // assume classes SomeSystem, SomeComponent exist
     * World world = new World();
     * world.init(1);
     * world.registerComponent(SomeComponent.class);
     *
     * int entity = world.createEntity();
     * SomeComponent entityComponent = new SomeComponent();
     * world.addComponent(entity, entityComponent);
     *
     * SomeSystem someSystem = new ECSSystemBuilder<>(world, SomeSystem.class)
     *             .withComponent(SomeComponent.class)
     *             .build();
     *
     * System.out.println("Entities in SomeSystem, before signature change: " + someSystem.getEntityCount());
     *
     * // inside this method, entitySignatureChanged(entity, entitySignature) is called
     * world.removeComponent(entity, SomeComponent.class);
     *
     * System.out.println("Entities in SomeSystem, after signature change: " + someSystem.getEntityCount());
     *
     * // This code prints the following:
     * // Entities in SomeSystem, before signature change: 1
     * // Entities in SomeSystem, after signature change: 0
     * }</pre>
     *
     * @param entity          The entity whose signature has changed.
     * @param entitySignature The recently changed signature of the entity.
     */
    public void entitySignatureChanged(int entity, BitSet entitySignature) {
        for (Map.Entry<String, ECSSystem> entry : systems.entrySet()) {
            String type = entry.getKey();
            ECSSystem system = entry.getValue();
            BitSet systemSignature = systemSignatures.get(type);

            /* The BitSet class in java does not produce a new BitSet when doing bitwise operations -- the operations
             * are applied to the BitSet that the method is called on.
             * As such, we need to create a clone to avoid messing up the original BitSet. */
            BitSet entitySignatureClone = (BitSet) entitySignature.clone();
            entitySignatureClone.and(systemSignature);
            if (entitySignatureClone.equals(systemSignature)) {
                // Entity signature contains system signature - insert into set
                system.entities.add(entity);
            } else {
                // Entity signature does not contain system signature - erase from set
                system.entities.remove(entity);
            }
        }
    }

    /**
     * Gets the signature of the system with the specified system class.
     * <p>
     * <h3>About</h3>
     * Based on the system class specified, this method gets the signature for that system.
     * <p/>
     * If the system class is not registered (see: {@link #registerSystem(Class, LinkedHashMap)}), then the returned
     * value will be null.
     *
     * @param systemClass The class of the system to retrieve the signature of.
     * @param <T>         The generic type of the system class to get the signature for. Uses of {@code T} must extend
     *                    {@code ECSSystem}.
     * @return The requested system signature, if found.
     */
    public <T extends ECSSystem> BitSet getSystemSignature(Class<T> systemClass) {
        return systemSignatures.get(systemClass.getTypeName());
    }

    /**
     * Gets the system with the specified system class.
     * <p>
     * <h3>About</h3>
     * Based on the system class specified, this method gets that system.
     * <p/>
     * If the system is not registered (see: {@link #registerSystem(Class, LinkedHashMap)}), then an {@link
     * IllegalStateException} will be thrown.
     *
     * @param systemClass The class of the system to retrieve.
     * @param <T>         The generic type of the system to get. Uses of {@code T} must extend {@code ECSSystem}.
     * @return The requested system, if found.
     */
    @SuppressWarnings("unchecked")
    public <T extends ECSSystem> T getSystem(Class<T> systemClass) {
        T system = (T) systems.get(systemClass.getTypeName());

        if (system == null) {
            throw new IllegalStateException("Could not find a system with the class " + systemClass.getTypeName());
        }

        return system;
    }

    public int getSystemCount() {
        return systems.values().size();
    }
}
