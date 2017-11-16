package ru.otus.internals;

import ru.otus.common.PathClassLoader;

import java.nio.file.*;

public class TestPackage {

    public TestPackage(Path packagePath) throws Exception {
        PathClassLoader classLoader = new PathClassLoader(packagePath);

        for (Path path : classLoader.getClasses()) {
            Class<?> a = classLoader.loadClass(path);

            if (a != null) {
                TestClass ts = new TestClass(a);

                if (ts.hasMethods()) {
                    ts.test();
                    System.out.println();
                }
            }
        }
    }
}

