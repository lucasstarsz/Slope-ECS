package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class InitialStateTests {
    private World world;

    @Before
    public void initialize() {
        /* These tests only need an empty world -- no builder necessary. */
        world = new World();
    }

    @Test
    public void checkInitialLivingEntityCount() {
        assertEquals("No entities should be present.", 0, world.entityManager().getLivingEntityCount());
    }

    @Test
    public void checkInitialComponentCount() {
        assertEquals("No components should have been registered.", 0, world.componentManager().getRegisteredComponentCount());
    }

    @Test
    public void checkInitialComponentArrayCount() {
        assertEquals("No component arrays should have been added.", 0, world.componentManager().getComponentArrayCount());
    }

    @Test
    public void checkInitialSystemCount() {
        assertEquals("No systems should have been added.", 0, world.systemManager().getSystemCount());
    }
}
