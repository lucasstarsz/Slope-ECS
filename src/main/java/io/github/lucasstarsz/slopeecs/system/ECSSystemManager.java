package io.github.lucasstarsz.slopeecs.system;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** The manager of systems and system signatures. */
public class ECSSystemManager {

    /** The map of the signatures of each system. */
    private final Map<String, BitSet> systemSignatures = new HashMap<>();
    /** The map of signatures. */
    private final Map<String, ECSSystem> systems = new HashMap<>();

    /**
     * Registers the specified class as a system in the manager, returning a new system of type T.
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
     * // Assume classes MySystem and CustomClass exist.
     * // Assume the MySystem class has a constructor that takes a String, int, and CustomClass instance.
     * ECSSystemManager systemManager = new ECSSystemManager();
     *
     * LinkedHashMap arguments = new LinkedHashMap<>() {{
     *     put(String.class, "parameter 1");
     *     put(int.class, 42);
     *     put(CustomClass.class, new CustomClass());
     * }};
     *
     * MySystem system = systemManager.registerSystem(MySystem.class, arguments);
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
     * Sets the bitset signature for the specified class.
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
     * The entities are in a set, so no check is needed.
     *
     * @param entity The entity that was destroyed.
     */
    public void entityDestroyed(int entity) {
        for (Map.Entry<String, ECSSystem> entry : systems.entrySet()) {
            entry.getValue().entities.remove(entity);
        }
    }

    /**
     * Notifies each system that the specified entity's bitset signature has changed.
     *
     * @param entity          The entity whose signature changed.
     * @param entitySignature The changed signature of the entity.
     */
    public void entitySignatureChanged(int entity, BitSet entitySignature) {
        for (Map.Entry<String, ECSSystem> entry : systems.entrySet()) {
            String type = entry.getKey();
            ECSSystem system = entry.getValue();
            BitSet systemSignature = systemSignatures.get(type);

            // so as not to affect entitySignature, we need to create a clone of it for bitwise operations.
            BitSet comparator = (BitSet) entitySignature.clone();
            comparator.and(systemSignature);
            if (comparator.equals(systemSignature)) {
                // Entity signature matches system signature - insert into set
                system.entities.add(entity);
            } else {
                // Entity signature does not match system signature - erase from set
                system.entities.remove(entity);
            }
        }
    }

    /**
     * Gets the signature of the system with the specified system class.
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

    /**
     * Gets the number of systems currently in the system manager.
     *
     * @return The number of systems in the system manager.
     */
    public int getSystemCount() {
        return systems.values().size();
    }
}
