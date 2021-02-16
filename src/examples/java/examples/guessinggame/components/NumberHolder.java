package examples.guessinggame.components;

import io.github.lucasstarsz.slopeecs.component.IComponent;

public class NumberHolder implements IComponent {
    public int number = (int) ((Math.random() * 10) + 1);
}
