package examples.guessinggame.game;

import examples.guessinggame.components.NumberHolder;
import examples.guessinggame.systems.NumberSystem;
import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.system.ECSSystemBuilder;

import java.io.IOException;

public class GuessingGame {

    private final int entityAmount = 10;

    private int guesses;
    private final int specialEntity = (int) ((Math.random() * entityAmount) + 1);

    private final World guessingWorld;
    private NumberSystem numberSystem;

    public GuessingGame() {
        guessingWorld = new World();
    }

    public void reset() {
        // TODO: choose entity amount after playing more than once

        // reset guesses
        guesses = 0;

        // reset ECS
        guessingWorld.init(entityAmount);
        guessingWorld.registerComponent(NumberHolder.class);

        numberSystem = new ECSSystemBuilder<>(guessingWorld, NumberSystem.class)
                .withComponent(NumberHolder.class)
                .build();

        for (int i = 0; i < entityAmount; i++) {
            int entity = guessingWorld.createEntity();
            NumberHolder holder = new NumberHolder();

            if (i == specialEntity) {
                holder.number = 13;
            }

            guessingWorld.addComponent(entity, holder);
        }
    }

    public void play() throws IOException {
        while (true) {
            int[] guess = numberSystem.guess();
            guesses++;

            if (guess[1] == 13) {
                System.out.println("You got it! Only took you " + guesses + (guesses == 1 ? " try!" : " tries!"));
                break;
            } else {
                System.out.println("Hmm, not quite! To make it easier for you, I'll remove that number.");
                System.out.println("(Ironically, this means you need to keep track of what entities were removed, which makes this harder...)" + System.lineSeparator().repeat(3));
                guessingWorld.destroyEntity(guess[0]);
            }
        }
    }
}
