package unittest.testcases;

import org.junit.Before;
import org.junit.Test;
import io.github.lucasstarsz.slopeecs.World;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;
import unittest.mock.systems.PositionSystem;

import java.util.BitSet;

import static junit.framework.TestCase.assertEquals;

public class ECSSystemTests {
    private final World manager = new World();
    private int[] entities;
    private PositionComponent[] positionComponents;
    private VelocityComponent[] velocityComponents;
    private PositionSystem positionSystem;

    @Before
    public void initialize() {
        /* Initialize arrays and manager */
        manager.init(2);
        entities = new int[manager.getMaxEntities()];
        positionComponents = new PositionComponent[manager.getMaxEntities()];
        velocityComponents = new VelocityComponent[manager.getMaxEntities()];

        /* Register components */
        manager.registerComponent(PositionComponent.class);
        manager.registerComponent(VelocityComponent.class);

        /* Create System */
        positionSystem = manager.registerSystem(PositionSystem.class);
        BitSet positionSystemSignature = new BitSet();
        positionSystemSignature.set(manager.getComponentType(PositionComponent.class));
        positionSystemSignature.set(manager.getComponentType(VelocityComponent.class));
        manager.setSystemSignature(PositionSystem.class, positionSystemSignature);

        /* Add components */
        for (int i = 0; i < entities.length; i++) {
            entities[i] = manager.createEntity();
            positionComponents[i] = new PositionComponent();
            velocityComponents[i] = new VelocityComponent();

            positionComponents[i].x += (i + 1);
            velocityComponents[i].y += 145 * (i + 1);

            manager.addComponent(entities[i], positionComponents[i]);
            manager.addComponent(entities[i], velocityComponents[i]);
        }
    }

    @Test
    public void checkSystemEntityCount_shouldMatch() {
        assertEquals("Entity values should match.", entities.length, positionSystem.getEntityCount());
    }

    @Test
    public void checkGetComponentsAfterModification_shouldMatch() {
        positionSystem.update(false);
        for (int i = 0; i < entities.length; i++) {
            assertEquals("Components should match after modification.", positionComponents[i], manager.getComponent(entities[i], PositionComponent.class));
            assertEquals("Components should match after modification.", velocityComponents[i], manager.getComponent(entities[i], VelocityComponent.class));
        }
    }
}
