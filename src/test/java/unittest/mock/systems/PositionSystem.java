package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.IComponent;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import unittest.mock.components.PositionComponent;

import java.util.Set;

public class PositionSystem implements ECSSystem {

//    public void update(boolean printEntityInfo) {
//    }

    @Override
    public Set<Class<? extends IComponent>> getComponentsList() {
        return Set.of(PositionComponent.class);
    }

    @Override
    public void update(World world, Set<Integer> entities) {
        for (int entity : entities) {

            // get components
            PositionComponent positionComponent = world.getComponent(entity, PositionComponent.class);

            // increase position by 1
            positionComponent.x++;
            positionComponent.y++;

        }
    }
}
