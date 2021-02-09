package unittest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestUtil {
    public static void test(Class<?>... classes) {
        Result result = JUnitCore.runClasses(classes);

        System.out.print(
                System.lineSeparator()
                        + "[Test result]"
                        + System.lineSeparator()
                        + result.getRunCount() + " tests run, "
                        + result.getFailureCount() + " failure(s), "
                        + result.getIgnoreCount() + " tests ignored."
                        + System.lineSeparator()
                        + "Tests ran for: " + result.getRunTime() + "ms."
                        + System.lineSeparator()
        );

        System.exit(0);
    }
}
