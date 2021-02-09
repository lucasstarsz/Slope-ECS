package unittest.testcases;

import org.junit.Before;
import org.junit.Test;
import io.github.lucasstarsz.slopeecs.World;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class ECSEntityTests {
    private final World manager = new World();
    private int[] entities;

    @Before
    public void init() {
        manager.init(5);

        entities = new int[manager.getMaxEntities()];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = manager.createEntity();
        }
    }

    @Test
    public void checkLivingEntityCount_whenEntitiesArePresent() {
        assertEquals("Entity count should match.", entities.length, manager.getEntityManager().getLivingEntityCount());
    }

    @Test
    public void checkAvailableEntityCount_whenEntitiesArePresent() {
        assertEquals("Entity count should match.", 0, manager.getEntityManager().getAvailableEntities());
    }

    @Test(expected = IllegalStateException.class)
    public void tryAddEntity_whenNoSpaceIsAvailable() {
        manager.createEntity();
    }

    @Test
    public void checkEntitySignatures_shouldAllBeNull() {
        for (int entity : entities) {
            assertNull("Entity signature should not have been created.", manager.getEntityManager().getSignature(entity));
        }
    }
}
