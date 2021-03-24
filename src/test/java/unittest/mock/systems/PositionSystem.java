package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import unittest.mock.components.PositionComponent;

import java.util.Set;

public class PositionSystem extends ECSSystem {

    @Override
    public Set<Class<? extends Component>> getComponentsList() {
        return Set.of(PositionComponent.class);
    }

    @Override
    public void update(Set<Integer> entities) {
        for (int entity : entities) {

            // get components
            PositionComponent positionComponent = world().getComponent(entity, PositionComponent.class);

            // increase position by 1
            positionComponent.x++;
            positionComponent.y++;

        }
    }
}
