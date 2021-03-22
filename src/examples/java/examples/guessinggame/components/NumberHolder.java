package examples.guessinggame.components;

import io.github.lucasstarsz.slopeecs.component.Component;

public class NumberHolder implements Component {
    public int number;

    @Override
    public Component copy() {
        NumberHolder holder = new NumberHolder();
        holder.number = this.number;
        return holder;
    }
}
