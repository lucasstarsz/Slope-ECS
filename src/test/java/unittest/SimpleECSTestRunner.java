package unittest;

import unittest.testcases.ComponentTests;
import unittest.testcases.InitialStateTests;
import unittest.testcases.SystemTests;
import unittest.testcases.EntityIDTests;
import unittest.testcases.EntityObjectTests;

public class SimpleECSTestRunner {
    public static void main(String[] args) {
        TestUtil.test(
                InitialStateTests.class,
                EntityIDTests.class,
                EntityObjectTests.class,
                ComponentTests.class,
                SystemTests.class
        );
    }
}
