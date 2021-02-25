package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class ECSEntityTests {
    private final World world = new World();
    private int[] entities;

    @Before
    public void init() {
        world.init(5);

        entities = new int[world.getMaxEntities()];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = world.createEntity();
        }
    }

    @Test
    public void checkLivingEntityCount_whenEntitiesArePresent() {
        assertEquals("Entity count should match.", entities.length, world.getEntityManager().getLivingEntityCount());
    }

    @Test
    public void checkAvailableEntityCount_whenEntitiesArePresent() {
        assertEquals("Entity count should match.", 0, world.getEntityManager().getAvailableEntities());
    }

    @Test(expected = IllegalStateException.class)
    public void tryAddEntity_whenNoSpaceIsAvailable() {
        // there is no space to create the entity, so an IllegalStateException should be thrown.
        world.createEntity();
    }

    @Test
    public void checkEntitySignatures_shouldAllBeNull() {
        for (int entity : entities) {
            assertNull("Entity signature should not have been created.", world.getEntityManager().getSignature(entity));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void tryRemoveEntity_outOfRange() {
        // world.getMaxEntities() + 1 is not a valid entity in the world, since it will always be out of range.
        world.destroyEntity(world.getMaxEntities() + 1);
    }

    @Test(expected = IllegalStateException.class)
    public void tryRemoveEntity_afterItWasAlreadyRemoved() {
        world.destroyEntity(entities[0]);

        /* Trying to destroy an entity that is currently not alive should throw an IllegalStateException.
         *
         * This caused me to find a problem with the last implementation of my ECS: destroying an entity assumes the
         * value has a BitSet. There were checks in place to ensure that the entity id specified was not  */
        world.destroyEntity(entities[0]);
    }

    @Test
    public void checkEntityCount_afterRemovingEntity() {
        world.destroyEntity(entities[0]);
        assertEquals("The amount of entities should be " + (entities.length - 1), (entities.length - 1), world.getEntityManager().getLivingEntityCount());
    }

    @Test
    public void checkIfEntityIsAlive_afterItWasDestroyed() {
        world.destroyEntity(entities[0]);
        assertFalse("The entity should not be alive, as it was already destroyed.", world.getEntityManager().isAlive(entities[0]));
    }

    @Test
    public void checkIfEntityIsAlive_afterItWasCreated() {
        assertTrue("The entity should be alive, as it was just created.", world.getEntityManager().isAlive(entities[0]));
    }
}
