package unittest.mock.components;

import io.github.lucasstarsz.slopeecs.component.Component;

public class VelocityComponent implements Component {
    public float x;
    public float y;

    @Override
    public Component copy() {
        VelocityComponent velocityComponent = new VelocityComponent();
        velocityComponent.x = this.x;
        velocityComponent.y = this.y;
        return velocityComponent;
    }
}
