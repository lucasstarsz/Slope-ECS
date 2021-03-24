package unittest.testcases;

import io.github.lucasstarsz.slopeecs.World;
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
    private World world = new World();

    private int singleEntity;
    PositionComponent singlePositionComponent;
    VelocityComponent singleVelocityComponent;

    private int[] entities;
    private final int entityCount = 4;
    private PositionComponent[] positionComponents;

    private GravitySystem gravitySystem;
    private PositionSystem positionSystem;
    private EmptySystem emptySystem;
    private UniqueSystem uniqueSystem;

    @Before
    public void initialize() {
        /* Initialize ecs world */
        world = new World(entityCount);

        /* Add systems */
        gravitySystem = world.addSystem(GravitySystem.class);
        positionSystem = world.addSystem(PositionSystem.class);
        emptySystem = world.addSystem(EmptySystem.class);
        uniqueSystem = world.addSystem(UniqueSystem.class);

        /* Create entities w/ just PositionComponent */
        entities = world.generateEntityIDs(entityCount);

        positionComponents = new PositionComponent[entityCount];
        for (int i = 0; i < entityCount; i++) {
            positionComponents[i] = new PositionComponent();
            positionComponents[i].x += (i + 1);

            world.addComponents(entities[i], positionComponents[i]);
        }

        /* Create entity with PositionComponent and VelocityComponent */
        singlePositionComponent = new PositionComponent();
        singleVelocityComponent = new VelocityComponent();

        singlePositionComponent.x += 5;
        singleVelocityComponent.y += 5;

        singleEntity = world.generateEntityID(singlePositionComponent, singleVelocityComponent);
    }

    @Test
    public void checkGetSystemAndSignature_shouldMatchOriginal() {
        world.reset();
        world.componentManager().registerComponent(PositionComponent.class);

        PositionSystem positionSystem = world.addSystem(PositionSystem.class);
        BitSet positionSystemSignature = world.systemManager().getSystemMetadata(PositionSystem.class).signature();
        positionSystemSignature.set(world.componentManager().getComponentType(PositionComponent.class));

        assertEquals("System signatures should match.", positionSystemSignature, world.systemManager().getSystemMetadata(PositionSystem.class).signature());
        assertEquals("Systems should match.", positionSystem, world.systemManager().getSystem(PositionSystem.class));
    }

    @Test(expected = IllegalStateException.class)
    public void tryCreateSystem_whenSystemAlreadyExistsInWorld() {
        /* This should fail because there is already a GravitySystem in the world. */
        world.addSystem(GravitySystem.class);
    }

    @Test
    public void checkSystemEntityCount_whenSystemHasSomeComponents() {
        assertEquals("All entities with the PositionComponent component should be found in PositionSystem.", world.entityManager().getLivingEntityCount(), positionSystem.entities().size());
    }

    @Test
    public void checkSystemEntityCount_whenSystemHasNoComponents() {
        assertEquals("All entities should be found in EmptySystem.", world.entityManager().getLivingEntityCount(), emptySystem.entities().size());
    }

    @Test
    public void checkSystemEntityCount_whenSystemHasAllComponents() {
        assertEquals("All entities with PositionComponent and VelocityComponent components should be found in GravitySystem.", 1, gravitySystem.entities().size());
    }

    @Test
    public void checkSystemEntityCount_whenSystemRequiresComponentNoEntityHas() {
        assertEquals("No entities should be found in UniqueSystem, since it requires UniqueComponent which no entity has.", 0, uniqueSystem.entities().size());
    }

    @Test
    public void checkSystemEntityCount_afterEntityIsRemoved() {
        world.destroyEntityIDs(singleEntity);
        assertEquals("Entity count in GravitySystem should be 0 after its only entity was destroyed.", 0, gravitySystem.entities().size());
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
