package io.github.lucasstarsz.slopeecs.component;

import java.util.HashMap;
import java.util.Map;

public class ComponentMap<T extends Component> implements ComponentMapper {

    private final Map<Integer, T> componentList;

    public ComponentMap() {
        componentList = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <C extends Component> void putAll(Map<Integer, C> entityComponentMap) {
        componentList.putAll((Map<Integer, T>) entityComponentMap);
    }

    @SuppressWarnings("unchecked")
    public <C extends Component> void put(int entity, C component) {
        if (contains(entity)) {
            throw new IllegalStateException("The component " + component.getClass().getTypeName() + " was already added to entity " + entity + ".");
        }

        componentList.put(entity, (T) component);
    }

    public void remove(int entity) {
        if (!contains(entity)) {
            throw new IllegalStateException("Entity " + entity + " does not have data in this component array (" + this.getClass().getTypeName() + ").");
        }

        componentList.remove(entity);
    }

    public T get(int entity) {
        if (!contains(entity)) {
            throw new IllegalStateException("Entity " + entity + " does not have data in this component array.");
        }

        return componentList.get(entity);
    }

    public boolean contains(int entity) {
        return componentList.get(entity) != null;
    }

    @Override
    public void entityDestroyed(int entity) {
        if (contains(entity)) {
            remove(entity);
        }
    }

    public int getValidEntryCount() {
        return componentList.size();
    }
}
