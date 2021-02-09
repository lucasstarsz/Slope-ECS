package io.github.lucasstarsz.component;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * The container of components for each type of component to be stored.
 *
 * @param <T> The generic type of the component to be stored in a given component array. Uses of {@code (.)} must
 *            implement {@code IComponent}.
 */
public class ECSComponentArray<T extends IComponent> implements IComponentArray {

    /**
     * The packed array of components set to a specified maximum amount, matching the maximum number of entities allowed
     * to exist simultaneously, so that each entity has a unique spot.
     * <p>
     * Uses of generic type {@code T} must implement {@code IComponent}.
     */
    private final T[] componentArray;

    /**
     * The maximum number of entities allowed within a component array.
     * <p>
     * This number is the same across all portions of the ECS. To change it, one must call {@code ECSCoordinator#init}
     * and set the maximum number of entities manually. Doing this a second time during use of the ECS
     * <strong>will</strong> remove any existing components, and invalidate entity IDs.
     */
    private final int maxEntities;

    /** A map from an entity ID to an array index. */
    private final Dictionary<Integer, Integer> entityToIndexMap = new Hashtable<>();
    /** A map from an array index to an entity ID. */
    private final Map<Integer, Integer> indexToEntityMap = new HashMap<>();
    /** Total size of valid entries in the component array. */
    private int validEntries;

    /**
     * Creates an {@code ECSComponentArray} with the specified maximum entity count.
     *
     * @param maxEntityCount The maximum amount of entities allowed within the component array.
     */
    @SuppressWarnings("unchecked")
    public ECSComponentArray(int maxEntityCount) {
        this.maxEntities = maxEntityCount;
        componentArray = (T[]) new IComponent[maxEntities];
    }

    /**
     * Adds the specified component to the component array.
     *
     * @param entity    The entity (and array index) to add the component for.
     * @param component The component to add.
     * @param <C>       The generic type of the component to add. Uses of {@code C} must implement {@code IComponent},
     *                  identical to type {@code T} already defined.
     */
    @SuppressWarnings("unchecked")
    public <C extends IComponent> void insertData(int entity, C component) {
        if (entityToIndexMap.get(entity) != null) {
            throw new IllegalStateException("Component of class " + component.getClass() + " was added to same entity more than once.");
        }

        // Put new entry at end and update the maps
        int newIndex = validEntries;
        entityToIndexMap.put(entity, newIndex);
        indexToEntityMap.put(newIndex, entity);
        componentArray[newIndex] = (T) component;
        validEntries++;
    }

    /**
     * Removes the data for the specified entity, if it exists.
     *
     * @param entity The entity to remove data for.
     */
    public void removeData(int entity) {
        if (entityToIndexMap.get(entity) == null) {
            throw new IllegalStateException("Entity with ID: " + entity + " does not have data in this component array.");
        }

        // Copy element at end into deleted element's place to maintain density
        int removedEntityIndex = entityToIndexMap.get(entity);
        int lastElementIndex = validEntries - 1;
        componentArray[removedEntityIndex] = componentArray[lastElementIndex];

        // Update map to point to moved spot
        int lastElementEntity = indexToEntityMap.get(lastElementIndex);
        entityToIndexMap.put(lastElementEntity, removedEntityIndex);
        indexToEntityMap.put(removedEntityIndex, lastElementEntity);

        // Remove the entity and its corresponding index
        entityToIndexMap.remove(entity);
        indexToEntityMap.remove(lastElementIndex);

        validEntries--;
    }

    /**
     * Gets the component bound to the specified entity, if it exists.
     *
     * @param entity The entity to get the bound component of.
     * @return The existing component.
     */
    public T getData(int entity) {
        if (entityToIndexMap.get(entity) == null) {
            throw new IllegalStateException("Entity with ID: " + entity + " does not have data in this component array.");
        }

        return componentArray[entityToIndexMap.get(entity)];
    }

    /**
     * Removes the specified entity's component if it exists.
     *
     * @param entity The entity whose components are to be removed.
     */
    @Override
    public void entityDestroyed(int entity) {
        if (!entityToIndexMap.get(entity).equals(entityToIndexMap.get(validEntries - 1))) {
            removeData(entity);
        }
    }

    /**
     * Gets the maximum number of entities allowed within the component array.
     *
     * @return The maximum number of entities allowed within the component array.
     */
    public int getMaxEntities() {
        return maxEntities;
    }

    /**
     * Gets the total size of valid entries in the component array.
     *
     * @return The total size of valid entries in the component array.
     */
    public int getValidEntryCount() {
        return validEntries;
    }
}
