package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import unittest.mock.components.PositionComponent;
import unittest.mock.components.VelocityComponent;

import java.util.Set;

public class GravitySystem extends ECSSystem {

    public static final float gravity = 9.81f;

    @Override
    public Set<Class<? extends Component>> getComponentsList() {
        return Set.of(PositionComponent.class, VelocityComponent.class);
    }

    @Override
    public void update(World world, Set<Integer> entities) {
        for (int entity : entities) {
            PositionComponent positionComponent = world.getComponent(entity, PositionComponent.class);
            VelocityComponent velocityComponent = world.getComponent(entity, VelocityComponent.class);

            velocityComponent.y = velocityComponent.y - gravity;
            positionComponent.x = positionComponent.x + velocityComponent.x;
            positionComponent.y = positionComponent.y + velocityComponent.y;
        }
    }
}
