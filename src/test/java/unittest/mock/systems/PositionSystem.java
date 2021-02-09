package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;

public class PositionSystem extends ECSSystem {
    public void update(boolean printEntityInfo) {
        for (int entity : entities) {

            // get components
            PositionComponent positionComponent = world.getComponent(entity, PositionComponent.class);
            VelocityComponent velocityComponent = world.getComponent(entity, VelocityComponent.class);

            // increase position by velocity
            positionComponent.x += velocityComponent.x;
            positionComponent.y += velocityComponent.y;

            // increase velocity by 1
            velocityComponent.x++;
            velocityComponent.y++;

            // print results
            if (printEntityInfo) {
                System.out.println(
                        "Entity: " + entity
                                + System.lineSeparator()
                                + "Position: " + positionComponent.x + ", " + positionComponent.y
                                + System.lineSeparator()
                                + "Velocity: " + velocityComponent.x + ", " + velocityComponent.y
                );
            }
        }
    }

    public int getEntityCount() {
        return entities.size();
    }
}
