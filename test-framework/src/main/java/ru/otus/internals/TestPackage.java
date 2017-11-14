package ru.otus.internals;

/*
        Realise testing packages
 */

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class TestPackage {

    private List<Path> classes = new ArrayList<>();
    URLClassLoader cl;

    public TestPackage(Path packagePath) throws Exception {
        getClasses(packagePath);

        if (classes.size() > 0) {
            cl = URLClassLoader.newInstance(new URL[]{packagePath.toUri().toURL()});
            for (Path path: classes) {
                Class<?> a = null;
                try {
                    String p = path.toString().replace(".class", "")
                            .replace("\\", ".");
                    a = cl.loadClass(p);
                    if (a != null)
                        System.out.println("Loaded: " + a.getCanonicalName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (a != null) {
                    TestClass ts = new TestClass(a);
                    ts.test();
                    System.out.println();
                }
            }
        }
    }

    private Class<?> loadClass(Path path) {

        return null;
    }

    private void getClasses(Path path) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.class");
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (matcher.matches(file)) {
                        classes.add(path.relativize(file));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
