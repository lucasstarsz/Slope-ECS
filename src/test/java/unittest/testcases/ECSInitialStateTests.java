package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class ECSInitialStateTests {
    private final World world = new World();

    @Before
    public void initialize() {
        /* Initialize the manager before each test. */
        world.init();
    }

    @Test
    public void checkInitialLivingEntityCount() {
        assertEquals("No entities should be present.", 0, world.getEntityManager().getLivingEntityCount());
    }

    @Test
    public void checkInitialAvailableEntityCount() {
        assertEquals("Every entity slot should be available.", world.getMaxEntities(), world.getEntityManager().getAvailableEntities());
    }

    @Test
    public void checkInitialComponentCount() {
        assertEquals("No components should have been registered.", 0, world.getComponentManager().getRegisteredComponentCount());
    }

    @Test
    public void checkInitialComponentArrayCount() {
        assertEquals("No component arrays should have been added.", 0, world.getComponentManager().getComponentArrayCount());
    }

    @Test
    public void checkInitialSystemCount() {
        assertEquals("No systems should have been added.", 0, world.getSystemManager().getSystemCount());
    }
}
