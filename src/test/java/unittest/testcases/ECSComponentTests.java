package unittest.testcases;

import org.junit.Before;
import org.junit.Test;
import io.github.lucasstarsz.World;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class ECSComponentTests {
    private final World manager = new World();
    private int[] entities;
    private PositionComponent[] positionComponents;
    private VelocityComponent[] velocityComponents;

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
    public void checkGetComponents_shouldMatch() {
        for (int i = 0; i < entities.length; i++) {
            assertEquals("Position components should match.", positionComponents[i], manager.getComponent(entities[i], PositionComponent.class));
            assertEquals("Velocity components should match.", velocityComponents[i], manager.getComponent(entities[i], VelocityComponent.class));
        }
    }

    @Test
    public void checkComponentRegisteredCount_shouldMatchAdded() {
        assertEquals("Component count should match the size of components created.", 2, manager.getComponentManager().getRegisteredComponentCount());
    }

    @Test(expected = IllegalStateException.class)
    public void tryGetComponent_afterRemoval() {
        manager.removeComponent(entities[0], PositionComponent.class);
        assertNull("Component count should match the size of components created.", manager.getComponent(entities[0], PositionComponent.class));
    }

    @Test
    public void checkGetComponentAfterRemovingEntity_shouldMatchOriginal() {
        manager.destroyEntity(entities[0]);
        assertEquals("Position components should match.", positionComponents[1], manager.getComponent(entities[1], PositionComponent.class));
        assertEquals("Velocity components should match.", velocityComponents[1], manager.getComponent(entities[1], VelocityComponent.class));
    }
}
