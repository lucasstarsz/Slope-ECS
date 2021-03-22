package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.entity.Entity;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static junit.framework.TestCase.*;

public class EntityObjectTests {
    private World world;
    private final int entityCount = 5;
    private Entity[] entities;

    @Before
    public void init() {
        world = new World(entityCount);
        entities = world.generateEntities(entityCount);
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
    public void checkEntitySignatures_shouldAllBeEmpty() {
        BitSet emptyBitset = new BitSet();
        for (Entity entity : entities) {
            assertEquals("Entity signature should be empty.", emptyBitset, entity.signature());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void tryRemoveEntity_outOfRange() {
        // entities[entities.length - 1] - 1 is not a valid entity in the world, since it will always be out of range.
        world.entityManager().destroyEntity(entities[entities.length - 1].id() - 1);
    }

    @Test(expected = IllegalStateException.class)
    public void tryRemoveEntity_afterItWasAlreadyRemoved() {
        Entity.destroy(entities[0]);

        /* Trying to destroy an entity that is currently not alive should throw an IllegalStateException.
         *
         * This caused me to find a problem with the last implementation of my ECS: destroying an entity assumes the
         * value has a BitSet. There were checks in place to ensure that the entity id specified was not  */
        Entity.destroy(entities[0]);
    }

    @Test
    public void checkEntityCount_afterRemovingEntity() {
        Entity.destroy(entities[0]);
        assertEquals("The amount of entities should be " + (entities.length - 1), (entities.length - 1), world.entityManager().getLivingEntityCount());
    }

    @Test
    public void checkIfEntityIsAlive_afterItWasDestroyed() {
        Entity.destroy(entities[0]);
        assertFalse("The entity should not be alive, as it was already destroyed.", entities[0].isAlive());
    }

    @Test
    public void checkIfEntityIsAlive_afterItWasCreated() {
        assertTrue("The entity should be alive, as it was just created.", entities[0].isAlive());
    }
}
