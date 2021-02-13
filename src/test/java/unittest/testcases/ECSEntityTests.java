package unittest.testcases;

import org.junit.Before;
import org.junit.Test;
import io.github.lucasstarsz.slopeecs.World;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

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
        world.createEntity();
    }

    @Test
    public void checkEntitySignatures_shouldAllBeNull() {
        for (int entity : entities) {
            assertNull("Entity signature should not have been created.", world.getEntityManager().getSignature(entity));
        }
    }
}
