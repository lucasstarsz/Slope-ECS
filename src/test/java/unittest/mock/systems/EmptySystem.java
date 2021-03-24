package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;

import java.util.Set;

public class EmptySystem extends ECSSystem {

    @Override
    public Set<Class<? extends Component>> getComponentsList() {
        return Set.of();
    }

    @Override
    public void update(Set<Integer> entities) {

    }
}
