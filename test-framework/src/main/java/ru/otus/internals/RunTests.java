package ru.otus.internals;

import ru.otus.Tests.New1;
import ru.otus.common.ReflectionHelper;

/**
 *  Driver class for run tests
 *
 */

public class RunTests {

//TODO: logger!!

    private void testClass(Class<?> clazz) {
        TestClass testClass = new TestClass(clazz);

        testClass.test(clazz);
    }

    public static void main(String[] args) {
        RunTests rt = new RunTests();
        rt.run();
    }

    private void run() {

        testClass(New1.class);
    }
}
