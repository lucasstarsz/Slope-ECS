package io.github.lucasstarsz.slopeecs.component;

/**
 * An interface is needed so that the {@code ECSComponentManager} can tell a generic {@code ECSComponentArray} that an
 * entity has been destroyed and it needs to update its array mappings.
 */
public interface IComponentArray {
    /**
     * Action to take when an entity is destroyed.
     *
     * @param entity The entity which was destroyed.
     */
    void entityDestroyed(int entity);
}
