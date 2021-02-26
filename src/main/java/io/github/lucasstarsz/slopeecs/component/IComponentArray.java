package io.github.lucasstarsz.slopeecs.component;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.entity.ECSEntityManager;

/**
 * An interface added to tie generics together.
 * <p>
 * <h3>About</h3>
 * This interface is needed so that the {@code ECSComponentManager} can tell a generic {@code ECSComponentArray} that an
 * entity has been destroyed and it needs to update its array mappings.
 */
public interface IComponentArray {
    /**
     * Action to take when an entity is destroyed.
     * <p>
     * <h3>About</h3>
     * This method is called by {@link ECSEntityManager#destroyEntity(int)} -- each component array in the entity
     * manager is updated to remove the component aliased to it, if it exists. For more information on the process, see
     * {@link World#destroyEntity(int)}
     *
     * @param entity The entity which was destroyed.
     */
    void entityDestroyed(int entity);
}
