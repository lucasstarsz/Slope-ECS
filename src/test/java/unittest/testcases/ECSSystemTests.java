package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.system.ECSSystemBuilder;
import org.junit.Before;
import org.junit.Test;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;
import unittest.mock.systems.EmptySystem;
import unittest.mock.systems.GravitySystem;
import unittest.mock.systems.PositionSystem;
import unittest.mock.systems.UniqueSystem;

import java.util.BitSet;

import static junit.framework.TestCase.assertEquals;

public class ECSSystemTests {
    private final World world = new World();

    private int singleEntity;
    PositionComponent singlePositionComponent;
    VelocityComponent singleVelocityComponent;

    private int[] entities;
    private PositionComponent[] positionComponents;

    private GravitySystem gravitySystem;
    private PositionSystem positionSystem;
    private EmptySystem emptySystem;
    private UniqueSystem uniqueSystem;

    @Before
    public void initialize() {
        /* Initialize ecs world */
        world.init(4);

        /* Initialize arrays */
        entities = new int[world.getMaxEntities() - 1];
        positionComponents = new PositionComponent[world.getMaxEntities() - 1];

        /* Register systems */
        gravitySystem = world.registerSystem(GravitySystem.class);
        positionSystem = world.registerSystem(PositionSystem.class);
        emptySystem = world.registerSystem(EmptySystem.class);
        uniqueSystem = world.registerSystem(UniqueSystem.class);

        /* Create entities/add components */
        for (int i = 0; i < entities.length; i++) {
            entities[i] = world.createEntity();
            positionComponents[i] = new PositionComponent();

            positionComponents[i].x += (i + 1);

            world.addComponent(entities[i], positionComponents[i]);
        }

        singleEntity = world.createEntity();
        singlePositionComponent = new PositionComponent();
        singleVelocityComponent = new VelocityComponent();

        singlePositionComponent.x += 5;
        singleVelocityComponent.y += 5;

        world.addComponent(singleEntity, singlePositionComponent);
        world.addComponent(singleEntity, singleVelocityComponent);
    }

    @Test
    public void checkGetSystemAndSignature_shouldMatchOriginal() {
        world.init();
        world.registerComponent(PositionComponent.class);

        PositionSystem positionSystem = new ECSSystemBuilder<>(world, PositionSystem.class)
                .withComponent(PositionComponent.class)
                .build();

        BitSet positionSystemSignature = new BitSet();
        positionSystemSignature.set(world.getComponentType(PositionComponent.class));
        world.setSystemSignature(PositionSystem.class, positionSystemSignature);

        assertEquals("System signatures should match.", positionSystemSignature, world.getSystemManager().getSystemSignature(positionSystem.getClass()));
        assertEquals("Systems should match.", positionSystem, world.getSystemManager().getSystem(positionSystem.getClass()));
    }

    @Test(expected = IllegalStateException.class)
    public void tryCreateSystem_whenSystemAlreadyExistsInWorld() {
        /* this should fail on:
         * new ECSSystemBuilder<>(world, GravitySystem.class)
         * because there is already a GravitySystem in the world. */
        new ECSSystemBuilder<>(world, GravitySystem.class)
                .withComponent(PositionComponent.class)
                .withComponent(VelocityComponent.class)
                .build();
    }

    @Test
    public void checkSystemEntityCount_whenSystemHasSomeComponents() {
        assertEquals("All entities with the PositionComponent component should be found in PositionSystem.", world.getMaxEntities(), positionSystem.getEntityCount(world));
    }

    @Test
    public void checkSystemEntityCount_whenSystemHasNoComponents() {
        assertEquals("All entities should be found in EmptySystem.", world.getMaxEntities(), emptySystem.getEntityCount(world));
    }

    @Test
    public void checkSystemEntityCount_whenSystemHasAllComponents() {
        assertEquals("All entities with PositionComponent and VelocityComponent components should be found in GravitySystem.", 1, gravitySystem.getEntityCount(world));
    }

    @Test
    public void checkSystemEntityCount_whenSystemRequiresComponentNoEntityHas() {
        assertEquals("No entities should be found in UniqueSystem, since it requires UniqueComponent which no entity has.", 0, uniqueSystem.getEntityCount(world));
    }

    @Test
    public void checkSystemEntityCount_afterEntityIsRemoved() {
        world.destroyEntity(singleEntity);
        assertEquals("Entity count in GravitySystem should be 0 after its only entity was destroyed.", 0, gravitySystem.getEntityCount(world));
    }

    @Test
    public void checkGetComponentsAfterModification_shouldMatchSingleEntity() {
        world.runSystem(GravitySystem.class);
        assertEquals("Components should match after modification.", singlePositionComponent, world.getComponent(singleEntity, PositionComponent.class));
        assertEquals("Components should match after modification.", singleVelocityComponent, world.getComponent(singleEntity, VelocityComponent.class));
    }

    @Test
    public void checkGetComponentsAfterModification_shouldMatchEntities() {
        world.runSystem(PositionSystem.class);
        for (int i = 0; i < entities.length; i++) {
            assertEquals("Components should match after modification.", positionComponents[i], world.getComponent(entities[i], PositionComponent.class));
        }
    }
}
