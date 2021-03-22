package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.entity.Entity;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import unittest.mock.components.UniqueComponent;

import java.util.Set;

public class UniqueSystem extends ECSSystem {
    @Override
    public Set<Class<? extends Component>> getComponentsList() {
        return Set.of(UniqueComponent.class);
    }

    @Override
    public void update(World world, Set<Integer> entities) {

    }
}
