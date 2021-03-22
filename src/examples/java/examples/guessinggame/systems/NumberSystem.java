package examples.guessinggame.systems;

import examples.guessinggame.components.NumberHolder;
import examples.guessinggame.game.GuessingGameHelper;
import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;

import java.io.IOException;
import java.util.Set;

public class NumberSystem extends ECSSystem {

    /**
     * Gets the guess from the user, and relates it to the corresponding entity.
     *
     * @return The guess in the form of {entity, number from entity}.
     */
    public int[] guess() throws IOException {
        System.out.println("There are " + entities().size() + " entities left in this system.");
        System.out.println("Which do you think carries the special number?");

        int input = GuessingGameHelper.getNumber(entities());

        return new int[]{
                input, // the entity
                world().getComponent(input, NumberHolder.class).number // the guess
        };
    }

    @Override
    public Set<Class<? extends Component>> getComponentsList() {
        return Set.of(NumberHolder.class);
    }

    @Override
    public void update(World world, Set<Integer> entities) {

    }
}
