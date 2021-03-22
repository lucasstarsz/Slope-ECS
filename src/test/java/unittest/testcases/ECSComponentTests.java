package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import org.junit.Before;
import org.junit.Test;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;

import static junit.framework.TestCase.assertEquals;

public class ECSComponentTests {
    private World world;

    private int[] entities;
    private final int entityCount = 2;

    private PositionComponent[] positionComponents;
    private VelocityComponent[] velocityComponents;

    @Before
    public void initialize() {
        world = new World(entityCount);
        entities = world.generateEntityIDs(entityCount);
        world.componentManager().registerComponent(PositionComponent.class);
        world.componentManager().registerComponent(VelocityComponent.class);

        positionComponents = new PositionComponent[world.entityManager().getLivingEntityCount()];
        velocityComponents = new VelocityComponent[world.entityManager().getLivingEntityCount()];

        for (int i = 0; i < entityCount; i++) {
            positionComponents[i] = new PositionComponent();
            velocityComponents[i] = new VelocityComponent();

            positionComponents[i].x += (i + 1);
            velocityComponents[i].y += 145 * (i + 1);

            world.addComponents(entities[i], positionComponents[i]);
            world.addComponents(entities[i], velocityComponents[i]);
        }
    }

    @Test
    public void checkGetComponents_shouldMatch() {
        try {
            for (int i = 0; i < entities.length; i++) {
                assertEquals("Position components should match.", positionComponents[i], world.getComponent(entities[i], PositionComponent.class));
                assertEquals("Velocity components should match.", velocityComponents[i], world.getComponent(entities[i], VelocityComponent.class));
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkComponentRegisteredCount_shouldMatchAdded() {
        assertEquals("Component count should match the size of components created.", 2, world.componentManager().getRegisteredComponentCount());
    }

    @Test(expected = IllegalStateException.class)
    public void tryGetComponent_afterRemovalFromEntity() {
        world.removeComponents(entities[0], PositionComponent.class);

        // Should throw IllegalStateException
        world.getComponent(entities[0], PositionComponent.class);
    }

    @Test(expected = IllegalStateException.class)
    public void tryGetComponent_afterAssociatedEntityRemoval() {
        world.destroyEntityIDs(entities[0]);

        // Should throw IllegalStateException
        world.getComponent(entities[0], PositionComponent.class);
    }

    @Test
    public void checkGetComponentAfterRemovingOtherEntity_shouldMatchOriginal() {
        world.destroyEntityIDs(entities[0]);
        assertEquals("Position components should match.", positionComponents[1], world.getComponent(entities[1], PositionComponent.class));
        assertEquals("Velocity components should match.", velocityComponents[1], world.getComponent(entities[1], VelocityComponent.class));
    }
}
