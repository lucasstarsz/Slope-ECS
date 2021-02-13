package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;

public class GravitySystem extends ECSSystem {

    public static final float gravity = 9.81f;

    public void update(boolean printEntityInfo) {
        for (int entity : entities) {
            PositionComponent positionComponent = world.getComponent(entity, PositionComponent.class);
            VelocityComponent velocityComponent = world.getComponent(entity, VelocityComponent.class);

            if (printEntityInfo) {
                System.out.println("Before gravitational force: ("
                        + positionComponent.x + ", " + positionComponent.y + "), ("
                        + velocityComponent.x + ", " + velocityComponent.y + ")"
                );
            }

            velocityComponent.y = velocityComponent.y - gravity;
            positionComponent.x = positionComponent.x + velocityComponent.x;
            positionComponent.y = positionComponent.y + velocityComponent.y;

            if (printEntityInfo) {
                System.out.println("After gravitational force: ("
                        + positionComponent.x + ", " + positionComponent.y + "), ("
                        + velocityComponent.x + ", " + velocityComponent.y + ")"
                );
            }
        }
    }
}
