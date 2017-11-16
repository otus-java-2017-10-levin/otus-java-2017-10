package ru.otus.common;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class PathClassLoader {

    private URLClassLoader cl;
    private final Set<Path> classes = new HashSet<>();
    private final Set<Path> classpath = new HashSet<>();

    public PathClassLoader(Path cp) {
        loadClassPath(cp);

        if (classpath.size() > 0) {
            URL[] urls = new URL[classpath.size()];
            int i = 0;
            try {
                for (Path p : classpath) {
                    urls[i] = p.toUri().toURL();
                    i++;
                }
                cl = URLClassLoader.newInstance(urls);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("Wrong classpath");
        }
    }

    public Class<?> loadClass(Path path) {
        Class<?> res = null;
        try {
            String p = path.toString().replace(".class", "")
                    .replace("\\", ".");
            res = cl.loadClass(p);
            if (res != null)
                System.out.println("Loaded: " + res.getCanonicalName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void loadClassPath(Path path) {
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

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    classpath.add(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<? extends Path> getClasses() {
        return classes;
    }
}