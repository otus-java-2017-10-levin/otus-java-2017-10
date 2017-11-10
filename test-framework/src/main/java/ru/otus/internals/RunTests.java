package ru.otus.internals;

import ru.otus.Tests.New1;

import java.lang.reflect.InvocationTargetException;


/**
 *  Driver class for run tests
 *
 */

public class RunTests {


    private void testClass(Class<?> clazz) {
        TestClass testClass = new TestClass(clazz);

        try {
            testClass.test(clazz.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RunTests rt = new RunTests();
        rt.run(args);
    }

    public void run(String[] args) {

        testClass(New1.class);
    }
}
