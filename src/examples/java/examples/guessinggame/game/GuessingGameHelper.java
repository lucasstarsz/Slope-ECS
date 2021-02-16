package examples.guessinggame.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class GuessingGameHelper {
    public static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static int getNumber(Integer[] accepted) throws IOException {
        int result;

        while (true) {
            System.out.println(Arrays.toString(accepted));
            System.out.print("Enter a number from above: ");

            try {
                result = Integer.parseInt(input.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Not an integer.");
                continue;
            }

            int finalResult = result;
            if (Arrays.stream(accepted).anyMatch(n -> n == finalResult)) {
                return result;
            } else {
                System.out.println("Invalid input: Number must be one of the values above.");
            }
        }
    }
}
