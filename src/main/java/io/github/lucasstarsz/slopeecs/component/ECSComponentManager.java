package io.github.lucasstarsz.slopeecs.component;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.entity.ECSEntityManager;
import io.github.lucasstarsz.slopeecs.system.ECSSystemManager;

import java.util.HashMap;
import java.util.Map;

/**
 * The manager of components and component types.
 * <p>
 * <h3>About</h3>
 * This class is one of three managers (see: {@link ECSEntityManager}, {@link ECSSystemManager}) used within Slope-ECS.
 * It serves the main purpose of storing component types and arrays (see: {@link IComponentArray}, as well as
 * addition/retrieval/removal of those components.
 * <p>
 * Furthermore, the component types generated from this class are what are used in signatures by entities and systems
 * alike.
 * <p>
 * Considering this is only one of three managers in Slope-ECS, it is better to use the {@link World} class to manage
 * the ECS. In order to see that class in action, you should check the
 * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki" target="_blank">wiki</a> -- it is the best way to get an
 * understanding of how to make use of Slope.
 *
 * @author Andrew Dey
 */
public class ECSComponentManager {

    /** Mapping from class string to a component type */
    private final Map<String, Integer> componentTypes = new HashMap<>();
    /** Mapping from class string to a component array */
    private final Map<String, IComponentArray> componentArrays = new HashMap<>();
    /** The component type to be assigned to the next registered component, starting at 0. */
    private int nextComponentType;

    /**
     * The maximum number of entities allowed within the arrays of the component manager.
     * <p>
     * This number is the same across all portions of the ECS. To change it, one must call {@code ECSCoordinator#init}
     * and set the maximum number of entities manually. Doing this a second time during use of the ECS
     * <strong>will</strong> remove any existing components, and invalidate entity IDs.
     */
    private final int maxEntities;

    /**
     * Constructs a component manager with the specified maximum entity count.
     * <p>
     * <h3>About</h3>
     * This constructor requires that you set the maximum entity count, foregoing use of a default value. The value
     * specified must be at least 1.
     * <p>
     * The maximum entity count value only plays a role in setting the maximum entity count for a {@link
     * IComponentArray}.
     * <p>
     *
     * @param maxEntityCount The maximum amount of entities allowed within the component manager.
     * @author Andrew Dey
     */
    public ECSComponentManager(int maxEntityCount) {
        this.maxEntities = maxEntityCount;
    }

    /**
     * Registers the specified class as a possible component type within the ECS.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#registerComponent(Class)}, allowing you to register a component within
     * Slope-ECS. For more information and example usages, see {@link World#registerComponent(Class)}.
     *
     * @param componentClass The class of the component type.
     * @param <T>            The generic type of the component class to be registered. Uses of {@code T} must implement
     *                       {@code IComponent}.
     * @author Andrew Dey
     */
    public <T extends IComponent> void registerComponent(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        if (componentTypes.get(typeName) != null) {
            throw new IllegalStateException("Component type " + typeName + " was registered more than once.");
        }

        // Add this component type to the component type map
        componentTypes.put(typeName, nextComponentType);

        // Create a ComponentArray and add it to the component arrays map
        componentArrays.put(typeName, new ECSComponentArray<T>(maxEntities));

        // Increment the value so that the next component registered will be different
        nextComponentType++;
    }

    /**
     * Gets the component type of the specified component class.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#getComponentType(Class)}, allowing you to get the type of a component
     * within Slope-ECS. For more information and example usages, see {@link World#getComponentType(Class)}.
     * <p/>
     * The returned value is based on {@link #nextComponentType}, which starts at 0 and increments every time a new
     * component is registered.
     *
     * @param componentClass The class of the component type.
     * @param <T>            The generic type of the component class to get the type for. Uses of {@code T} must
     *                       implement {@code IComponent}.
     * @return The type of the specified component class.
     * @author Andrew Dey
     */
    public <T extends IComponent> int getComponentType(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        if (componentTypes.get(typeName) == null) {
            throw new IllegalStateException("Component type " + typeName + " was not found in the component manager.");
        }

        // Return this component's type - used for creating signatures
        return componentTypes.get(typeName);
    }

    /**
     * Adds the specified component to the specified entity's component array.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#addComponent(int, IComponent)}, allowing you to add a component aliased
     * to an entity within Slope-ECS. For more information and example usages, see {@link World#addComponent(int,
     * IComponent)}.
     *
     * @param entity    The entity to add the component to.
     * @param component The component to add.
     * @param <T>       The generic type of the component to be added. Uses of {@code T} must implement {@code
     *                  IComponent}.
     * @author Andrew Dey
     */
    public <T extends IComponent> void addComponent(int entity, T component) {
        getComponentArray(component.getClass()).insertData(entity, component);
    }

    /**
     * Remove a component from the array for an entity.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#removeComponent(int, Class)}, allowing you to remove a component
     * aliased to an entity within Slope-ECS. For more information and example usages, see {@link
     * World#removeComponent(int, Class)}.
     *
     * @param entity         The entity to remove a component from.
     * @param componentClass The class of the component to remove.
     * @param <T>            The generic type of the component to be removed. Uses of {@code T} must implement {@code
     *                       IComponent}.
     * @author Andrew Dey
     */
    public <T extends IComponent> void removeComponent(int entity, Class<T> componentClass) {
        getComponentArray(componentClass).removeData(entity);
    }

    /**
     * Gets the component from the array for an entity.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#getComponent(int, Class)}, allowing you to get a component aliased to
     * an entity within Slope-ECS. For more information and example usages, see {@link World#getComponent(int, Class)}.
     *
     * @param entity         The entity to get a component from.
     * @param componentClass The class of the component to get.
     * @param <T>            The generic type of the component to get. Uses of {@code T} must implement {@code
     *                       IComponent}.
     * @return The component from the array for an entity.
     * @author Andrew Dey
     */
    public <T extends IComponent> T getComponent(int entity, Class<T> componentClass) {
        return getComponentArray(componentClass).getData(entity);
    }

    /**
     * Notifies each component array that an entity has been destroyed. If it has a component for that entity, that
     * component will be removed.
     * <p>
     * <h3>About</h3>
     * This is the method called by {@link World#destroyEntity(int)}, where this method removes all components aliased
     * to the entity. For more information and example usages, see {@link World#destroyEntity(int)}.
     *
     * @param entity The entity being destroyed.
     * @author Andrew Dey
     */
    public void entityDestroyed(int entity) {
        for (var pair : componentArrays.entrySet()) {
            IComponentArray component = pair.getValue();
            component.entityDestroyed(entity);
        }
    }

    public int getRegisteredComponentCount() {
        return nextComponentType;
    }

    public int getComponentArrayCount() {
        return componentArrays.values().size();
    }

    /**
     * Gets the {@link IComponentArray} of type {@code T} -- the class specified.
     *
     * @param componentClass The class of the component array to get.
     * @param <T>            The generic type of the component to get the array for. Uses of {@code T} must implement
     *                       {@code IComponent}.
     * @return The component array of type {@code T}.
     * @author Andrew Dey
     */
    @SuppressWarnings("unchecked")
    private <T extends IComponent> ECSComponentArray<T> getComponentArray(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        if (componentTypes.get(typeName) == null) {
            throw new IllegalStateException("An array for component type " + typeName + " was not found in the component manager.");
        }

        return (ECSComponentArray<T>) componentArrays.get(typeName);
    }
}
