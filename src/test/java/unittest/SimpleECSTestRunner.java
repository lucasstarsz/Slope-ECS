package unittest;

import unittest.testcases.ECSComponentTests;
import unittest.testcases.ECSInitialStateTests;
import unittest.testcases.ECSSystemTests;
import unittest.testcases.EntityIDTests;
import unittest.testcases.EntityObjectTests;

public class SimpleECSTestRunner {
    public static void main(String[] args) {
        TestUtil.test(
                ECSInitialStateTests.class,
                EntityIDTests.class,
                EntityObjectTests.class,
                ECSComponentTests.class,
                ECSSystemTests.class
        );
    }
}
