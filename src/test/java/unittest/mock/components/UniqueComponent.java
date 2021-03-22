package unittest.mock.components;

import io.github.lucasstarsz.slopeecs.component.Component;

public class UniqueComponent implements Component {
    @Override
    public Component copy() {
        return new PositionComponent();
    }
}
