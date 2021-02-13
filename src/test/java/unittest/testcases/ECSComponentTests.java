package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import org.junit.Before;
import org.junit.Test;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;

import static junit.framework.TestCase.assertEquals;

public class ECSComponentTests {
    private final World world = new World();
    private int[] entities;
    private PositionComponent[] positionComponents;
    private VelocityComponent[] velocityComponents;

    @Before
    public void initialize() {
        /* Initialize arrays and manager */
        world.init(2);
        entities = new int[world.getMaxEntities()];
        positionComponents = new PositionComponent[world.getMaxEntities()];
        velocityComponents = new VelocityComponent[world.getMaxEntities()];

        /* Register components */
        world.registerComponent(PositionComponent.class);
        world.registerComponent(VelocityComponent.class);

        /* Add components */
        for (int i = 0; i < entities.length; i++) {
            entities[i] = world.createEntity();
            positionComponents[i] = new PositionComponent();
            velocityComponents[i] = new VelocityComponent();

            positionComponents[i].x += (i + 1);
            velocityComponents[i].y += 145 * (i + 1);

            world.addComponent(entities[i], positionComponents[i]);
            world.addComponent(entities[i], velocityComponents[i]);
        }
    }

    @Test
    public void checkGetComponents_shouldMatch() {
        for (int i = 0; i < entities.length; i++) {
            assertEquals("Position components should match.", positionComponents[i], world.getComponent(entities[i], PositionComponent.class));
            assertEquals("Velocity components should match.", velocityComponents[i], world.getComponent(entities[i], VelocityComponent.class));
        }
    }

    @Test
    public void checkComponentRegisteredCount_shouldMatchAdded() {
        assertEquals("Component count should match the size of components created.", 2, world.getComponentManager().getRegisteredComponentCount());
    }

    @Test(expected = IllegalStateException.class)
    public void tryGetComponent_afterRemovalFromEntity() {
        world.removeComponent(entities[0], PositionComponent.class);

        // Should throw IllegalStateException
        world.getComponent(entities[0], PositionComponent.class);
    }

    @Test(expected = IllegalStateException.class)
    public void tryGetComponent_afterAssociatedEntityRemoval() {
        world.destroyEntity(entities[0]);

        // Should throw IllegalStateException
        world.getComponent(entities[0], PositionComponent.class);
    }

    @Test
    public void checkGetComponentAfterRemovingOtherEntity_shouldMatchOriginal() {
        world.destroyEntity(entities[0]);
        assertEquals("Position components should match.", positionComponents[1], world.getComponent(entities[1], PositionComponent.class));
        assertEquals("Velocity components should match.", velocityComponents[1], world.getComponent(entities[1], VelocityComponent.class));
    }
}
