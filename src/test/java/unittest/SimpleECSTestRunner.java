package unittest;

import unittest.testcases.ECSEntityTests;
import unittest.testcases.ECSInitialStateTests;

public class SimpleECSTestRunner {
    public static void main(String[] args) {
        TestUtil.test(ECSInitialStateTests.class, ECSEntityTests.class);
    }
}
