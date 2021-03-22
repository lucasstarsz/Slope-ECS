package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static junit.framework.TestCase.*;

public class EntityIDTests {
    private World world;

    private int[] entities;
    private final int entityCount = 5;

    @Before
    public void init() {
        world = new World(entityCount);
        entities = world.generateEntityIDs(entityCount);
    }

    @Test
    public void checkLivingEntityCount_whenEntitiesArePresent() {
        assertEquals("Entity count should match.", entityCount, world.entityManager().getLivingEntityCount());
    }

    @Test
    public void checkAvailableEntityCount_whenEntitiesArePresent() {
        assertEquals("Entity count should match.", 0, world.entityManager().getUnusedEntityCount());
    }

    @Test
    public void checkEntitySignatures_shouldAllBeNull() {
        for (int entity : entities) {
            assertEquals("Entity signature should be empty.", new BitSet(), world.entityManager().getSignature(entity));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void tryRemoveEntity_outOfRange() {
        // -1 is not a valid entity in the world, since it will always be out of range.
        world.entityManager().destroyEntity(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void tryRemoveEntity_afterItWasAlreadyRemoved() {
        world.entityManager().destroyEntity(entities[0]);

        /* Trying to destroy an entity that is currently not alive should throw an IllegalStateException.
         *
         * This caused me to find a problem with the last implementation of my ECS: destroying an entity assumes the
         * value has a BitSet. There were checks in place to ensure that the entity id specified was not  */
        world.entityManager().destroyEntity(entities[0]);
    }

    @Test
    public void checkEntityCount_afterRemovingEntity() {
        world.entityManager().destroyEntity(entities[0]);
        assertEquals("The amount of entities should be " + (entityCount - 1), entityCount - 1, world.entityManager().getLivingEntityCount());
    }

    @Test
    public void checkIfEntityIsAlive_afterItWasDestroyed() {
        world.entityManager().destroyEntity(entities[0]);
        assertFalse("The entity should not be alive, as it was already destroyed.", world.entityManager().isAlive(entities[0]));
    }

    @Test
    public void checkIfEntityIsAlive_afterItWasCreated() {
        assertTrue("The entity should be alive, as it was just created.", world.entityManager().isAlive(entities[0]));
    }
}
