package io.github.lucasstarsz.slopeecs.component;

import io.github.lucasstarsz.slopeecs.World;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ComponentManager {

    private final World world;

    private final Map<String, Integer> componentTypes;

    private final Map<String, ComponentMap<? extends Component>> componentArrays;

    private int nextComponentType;


    public ComponentManager(World world) {
        this.world = world;
        componentTypes = new HashMap<>();
        componentArrays = new HashMap<>();
    }

    public <T extends Component> void registerComponent(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        if (isComponentRegistered(componentClass)) {
            throw new IllegalStateException("Component type " + typeName + " was registered more than once.");
        }

        // Add this component type to the component type map
        componentTypes.put(typeName, nextComponentType);

        // Create a ComponentArray and add it to the component arrays map
        componentArrays.put(typeName, new ComponentMap<T>());

        // Increment the value so that the next component registered will be different
        nextComponentType++;
    }

    public void addComponents(int[] entities, Component... components) {
        Map<ComponentMap<? extends Component>, Map<Integer, Component>> componentMaps = new LinkedHashMap<>(components.length);
        for (Component component : components) {
            Map<Integer, Component> entityComponentsMap = new LinkedHashMap<>(entities.length);
            for (int entity : entities) {
                entityComponentsMap.put(entity, component.copy());
            }
            componentMaps.put(getComponentArray(component.getClass()), entityComponentsMap);
        }


        if (entities.length > 1000) {
            for (var componentMap : componentMaps.entrySet()) {
                componentMap.getKey().putAll(componentMap.getValue());
            }
        } else {
            for (var componentMap : componentMaps.entrySet()) {
                componentMap.getKey().putAll(componentMap.getValue());
            }
        }
    }

    public void addComponents(int entity, Component... components) {
        for (Component component : components) {
            getComponentArray(component.getClass()).put(entity, component);
        }
    }


    public <T extends Component> boolean isComponentRegistered(Class<T> component) {
        return componentTypes.get(component.getTypeName()) != null;
    }


    public <T extends Component> int getComponentType(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        if (componentTypes.get(typeName) == null) {
            throw new IllegalStateException("Component type " + typeName + " was not found in the component manager.");
        }

        // Return this component's type - used for creating signatures
        return componentTypes.get(typeName);
    }


    public <T extends Component> void addComponent(int entity, T component) {
        getComponentArray(component.getClass()).put(entity, component);
    }


    public <T extends Component> void removeComponent(int entity, Class<T> componentClass) {
        getComponentArray(componentClass).remove(entity);
    }

    public <T extends Component> boolean hasComponent(int entity, Class<T> componentClass) {
        return getComponentArray(componentClass).contains(entity);
    }


    public <T extends Component> T getComponent(int entity, Class<T> componentClass) {
        return getComponentArray(componentClass).get(entity);
    }


    public void entityDestroyed(int entity) {
        for (var pair : componentArrays.entrySet()) {
            pair.getValue().entityDestroyed(entity);
        }
    }

    public int getRegisteredComponentCount() {
        return nextComponentType;
    }

    public int getComponentArrayCount() {
        return componentArrays.values().size();
    }


    @SuppressWarnings("unchecked")
    private <T extends Component> ComponentMap<T> getComponentArray(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        if (componentTypes.get(typeName) == null) {
            throw new IllegalStateException("An array for component type " + typeName + " was not found in the component manager.");
        }

        return (ComponentMap<T>) componentArrays.get(typeName);
    }
}
