package unittest.mock.components;

import io.github.lucasstarsz.slopeecs.component.Component;

public class PositionComponent implements Component {
    public float x;
    public float y;

    @Override
    public Component copy() {
        PositionComponent positionComponent = new PositionComponent();
        positionComponent.x = this.x;
        positionComponent.y = this.y;
        return positionComponent;
    }
}
