package io.github.lucasstarsz.slopeecs.component;

import io.github.lucasstarsz.slopeecs.World;

import java.util.*;


public class ComponentManager {

    private final World world;

    private final Map<String, Integer> componentTypes;

    private final Map<Integer, ComponentMap<? extends Component>> componentMaps;

    private int nextComponentType;


    public ComponentManager(World world) {
        this.world = world;
        componentTypes = new HashMap<>();
        componentMaps = new HashMap<>();
    }

    public <T extends Component> void registerComponent(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        if (isComponentRegistered(componentClass)) {
            throw new IllegalStateException("Component type " + typeName + " was registered more than once.");
        }

        // Add this component type to the component type map
        componentTypes.put(typeName, nextComponentType);

        /* Create a ComponentArray and add it to the component arrays map.
         *
         * Instead of doing a map call for the new component type, we'll just use
         * nextComponentType (since that's the same value). */
        componentMaps.put(nextComponentType, new ComponentMap<T>());

        // Increment the value so that the next component registered will be different
        nextComponentType++;
    }

    public void addComponents(int[] entities, Component... components) {
        Map<ComponentMap<? extends Component>, Map<Integer, Component>> componentMaps = new LinkedHashMap<>(components.length);
        for (Component component : components) {
            Map<Integer, Component> entityComponentsMap = new HashMap<>(entities.length);
            for (int entity : entities) {
                entityComponentsMap.put(entity, component.copy());
            }
            componentMaps.put(getComponentArray(component.getClass()), entityComponentsMap);
        }

        for (var componentMap : componentMaps.entrySet()) {
            componentMap.getKey().putAll(componentMap.getValue());
        }

        addToEntitySignatures(entities, components);
    }

    public void addComponents(int entity, Component... components) {
        for (Component component : components) {
            getComponentArray(component.getClass()).put(entity, component);
        }

        addToEntitySignature(entity, components);
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

    @SafeVarargs
    public final <T extends Component> void removeComponents(int entity, Class<T>... componentClasses) {
        for (Class<T> componentClass : componentClasses) {
            getComponentArray(componentClass).remove(entity);
        }
    }

    public <T extends Component> boolean hasComponent(int entity, Class<T> componentClass) {
        return getComponentArray(componentClass).contains(entity);
    }

    public <T extends Component> T getComponent(int entity, Class<T> componentClass) {
        return getComponentArray(componentClass).get(entity);
    }

    public void entityDestroyed(int entity) {
        for (var componentMap : componentMaps.values()) {
            componentMap.entityDestroyed(entity);
        }
    }

    public void entitiesDestroyed(int... entities) {
        if (entities.length > 1000) {
            Arrays.stream(entities).parallel().forEach(entity -> {
                for (var componentMap : componentMaps.values()) {
                    componentMap.entityDestroyed(entity);
                }
            });
        } else {
            for (int entity : entities) {
                for (var componentMap : componentMaps.values()) {
                    componentMap.entityDestroyed(entity);
                }
            }
        }
    }

    public int getRegisteredComponentCount() {
        return nextComponentType;
    }

    public int getComponentArrayCount() {
        return componentMaps.values().size();
    }


    @SuppressWarnings("unchecked")
    private <T extends Component> ComponentMap<T> getComponentArray(Class<T> componentClass) {
        String typeName = componentClass.getTypeName();

        Integer componentType = componentTypes.get(typeName);
        if (componentType == null) {
            throw new IllegalStateException("An array for component type " + typeName + " was not found in the component manager.");
        }

        return (ComponentMap<T>) componentMaps.get(componentType);
    }

    public void addToEntitySignature(int entity, Component... components) {
        BitSet entitySignature = world.entityManager().getSignature(entity);
        for (Component component : components) {
            entitySignature.set(getComponentType(component.getClass()), true);
        }

        world.entityManager().setSignature(entity, entitySignature);
    }

    public void addToEntitySignatures(int[] entities, Component... components) {
        if (entities.length > 1000) {
            Arrays.stream(entities).parallel().forEach(entity -> {
                BitSet entitySignature = world.entityManager().getSignature(entity);
                for (Component component : components) {
                    entitySignature.set(getComponentType(component.getClass()), true);
                }

                world.entityManager().setSignature(entity, entitySignature);
            });
        } else {
            for (int entity : entities) {
                BitSet entitySignature = world.entityManager().getSignature(entity);
                for (Component component : components) {
                    entitySignature.set(getComponentType(component.getClass()), true);
                }

                world.entityManager().setSignature(entity, entitySignature);
            }
        }
    }
}
