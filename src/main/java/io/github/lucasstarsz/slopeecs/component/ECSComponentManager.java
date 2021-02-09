package io.github.lucasstarsz.slopeecs.component;

import java.util.HashMap;
import java.util.Map;

/** The manager of components and component types. */
public class ECSComponentManager {

    /** Map from type string pointer to a component type */
    private final Map<String, Integer> componentTypes = new HashMap<>();
    /** Map from type string pointer to a component array */
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
     *
     * @param maxEntityCount The maximum amount of entities allowed within the component manager.
     */
    public ECSComponentManager(int maxEntityCount) {
        this.maxEntities = maxEntityCount;
    }

    /**
     * Registers the specified class as a possible component type within the ECS.
     *
     * @param componentClass The class of the component type.
     * @param <T>            The generic type of the component class to be registered. Uses of {@code T} must implement
     *                       {@code IComponent}.
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
     *
     * @param componentClass The class of the component type.
     * @param <T>            The generic type of the component class to get the type for. Uses of {@code T} must
     *                       implement {@code IComponent}.
     * @return The type of the specified component class.
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
     *
     * @param entity    The entity to add the component to.
     * @param component The component to add.
     * @param <T>       The generic type of the component to be added. Uses of {@code T} must implement {@code
     *                  IComponent}.
     */
    public <T extends IComponent> void addComponent(int entity, T component) {
        getComponentArray(component.getClass()).insertData(entity, component);
    }

    /**
     * Remove a component from the array for an entity.
     *
     * @param entity         The entity to remove a component from.
     * @param componentClass The class of the component to remove.
     * @param <T>            The generic type of the component to be removed. Uses of {@code T} must implement {@code
     *                       IComponent}.
     */
    public <T extends IComponent> void removeComponent(int entity, Class<T> componentClass) {
        getComponentArray(componentClass).removeData(entity);
    }

    /**
     * Gets the component from the array for an entity.
     *
     * @param entity         The entity to get a component from.
     * @param componentClass The class of the component to get.
     * @param <T>            The generic type of the component to get. Uses of {@code T} must implement {@code
     *                       IComponent}.
     * @return The component from the array for an entity.
     */
    public <T extends IComponent> T getComponent(int entity, Class<T> componentClass) {
        return getComponentArray(componentClass).getData(entity);
    }

    /**
     * Notifies each component array that an entity has been destroyed. If it has a component for that entity, that
     * component will be removed.
     *
     * @param entity The entity being destroyed.
     */
    public void entityDestroyed(int entity) {
        for (var pair : componentArrays.entrySet()) {
            IComponentArray component = pair.getValue();
            component.entityDestroyed(entity);
        }
    }

    /**
     * Gets the number of registered component types.
     *
     * @return The number of registered component types.
     */
    public int getRegisteredComponentCount() {
        return nextComponentType;
    }

    /**
     * Gets the ComponentArray of type {@code T}.
     *
     * @param componentClass The class of the component array to get.
     * @param <T>            The generic type of the component to get the array for. Uses of {@code T} must implement
     *                       {@code IComponent}.
     * @return The component array of type {@code T}.
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
