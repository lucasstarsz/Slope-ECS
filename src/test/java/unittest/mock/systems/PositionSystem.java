package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import unittest.mock.components.PositionComponent;

public class PositionSystem extends ECSSystem {

    public void update(boolean printEntityInfo) {
        for (int entity : entities) {

            // get components
            PositionComponent positionComponent = world.getComponent(entity, PositionComponent.class);

            // increase position by 1
            positionComponent.x++;
            positionComponent.y++;

            // print results
            if (printEntityInfo) {
                System.out.println(
                        "Entity: " + entity
                                + System.lineSeparator()
                                + "Position: " + positionComponent.x + ", " + positionComponent.y
                );
            }
        }
    }
}
