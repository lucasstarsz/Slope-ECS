package unittest.mock.systems;

import io.github.lucasstarsz.slopeecs.system.ECSSystem;

public class EmptySystem extends ECSSystem {
    public int getEntityCount() {
        return entities.size();
    }
}
