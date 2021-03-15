package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.IComponent;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;

import java.util.Set;

public class EmptySystem implements ECSSystem {

    @Override
    public Set<Class<? extends IComponent>> getComponentsList() {
        return Set.of();
    }

    @Override
    public void update(World world, Set<Integer> entities) {

    }
}
