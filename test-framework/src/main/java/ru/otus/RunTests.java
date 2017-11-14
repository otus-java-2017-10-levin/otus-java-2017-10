package ru.otus;

import ru.otus.Tests.New1;
import ru.otus.internals.TestClass;
import ru.otus.internals.TestPackage;

import java.nio.file.Paths;

/**
 *  Driver class for run tests
 *
 */

public class RunTests {

    private void testClass(Class<?> clazz) {
        TestClass testClass = new TestClass(clazz);

        testClass.test();
    }

    public static void main(String[] args) {
        RunTests rt = new RunTests();
        rt.run();
    }

    private void run() {

        testClass(New1.class);

        try {
            TestPackage pack = new TestPackage(Paths.get("C:\\Users\\Flow\\Dropbox\\Java\\OTUS\\homeworks\\test-framework\\target\\classes"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
