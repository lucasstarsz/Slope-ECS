package examples.guessinggame;

import examples.guessinggame.game.GuessingGame;
import examples.guessinggame.game.GuessingGameHelper;

import java.io.IOException;

public class GuessingGameExample {
    private static int timesPlayed;

    public static void main(String[] args) throws IOException {
        GuessingGame guessingGame = new GuessingGame();
        System.out.println("""
                Welcome to Guessing Game!
                Your goal is to find the secret entity with the special number.
                """);

        while (shouldPlay()) {
            // add to times played
            timesPlayed++;
            System.out.println("Alllllright, let's get into it!" + System.lineSeparator());

            guessingGame.reset();
            guessingGame.play();
        }

        System.out.println("Thank you for playing " + timesPlayed + (timesPlayed == 1 ? " time today." : " times today."));
    }

    private static boolean shouldPlay() throws IOException {
        System.out.println("Would you like to play a" + (timesPlayed > 1 ? "nother" : "") + " round? (y/N): ");
        String response = GuessingGameHelper.input.readLine();

        // unnecessary switch, but it makes things nice and readable for me :)
        return switch (response) {
            case "y", "Y" -> true;
            default -> false;
        };
    }
}
