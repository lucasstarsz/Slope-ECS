package unittest.testcases;

import org.junit.Before;
import org.junit.Test;
import org.lucasstarsz.slope.World;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;
import unittest.mock.systems.PositionSystem;

import java.util.BitSet;

import static junit.framework.TestCase.assertEquals;


public class ECSInitialStateTests {
    private final World manager = new World();

    @Before
    public void initialize() {
        /* Initialize the manager before each test. */
        manager.init();
    }

    @Test
    public void checkLivingEntityCount_whenNoEntitiesArePresent() {
        assertEquals("No entities should be present.", 0, manager.getEntityManager().getLivingEntityCount());
    }

    @Test
    public void checkAvailableEntityCount_whenNoEntitiesArePresent() {
        assertEquals("No entities should be present.", manager.getMaxEntities(), manager.getEntityManager().getAvailableEntities());
    }

    @Test
    public void checkRegisteredComponentCount_whenTwoAreRegistered() {
        manager.registerComponent(PositionComponent.class);
        manager.registerComponent(VelocityComponent.class);
        assertEquals("2 components should have been added.", 2, manager.getComponentManager().getRegisteredComponentCount());
    }

    @Test
    public void checkRegisteredComponentCount_whenNoneAreRegistered() {
        assertEquals("No components should have been added.", 0, manager.getComponentManager().getRegisteredComponentCount());
    }

    @Test
    public void checkGetSystemAndSignature_shouldMatchOriginal() {
        PositionSystem positionSystem = manager.registerSystem(PositionSystem.class);
        manager.registerComponent(PositionComponent.class);

        BitSet positionSystemSignature = new BitSet();
        positionSystemSignature.set(manager.getComponentType(PositionComponent.class));
        manager.setSystemSignature(PositionSystem.class, positionSystemSignature);

        assertEquals("System signatures should match.", positionSystemSignature, manager.getSystemManager().getSystemSignature(positionSystem.getClass()));
        assertEquals("Systems should match.", positionSystem, manager.getSystemManager().getSystem(positionSystem.getClass()));
    }
}
