package ru.otus;

import ru.otus.common.ArgParser;
import ru.otus.internals.TestClass;
import ru.otus.internals.TestPackage;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 *  Driver class for run tests
 *
 */

public class RunTests {

    private final String CLASSPATH = "cp";
    private final String CLASSES = "classes";

    private void testClass(Path cp, Path file) {
        try {
            TestClass testClass = new TestClass(cp, file);
            testClass.test();
        } catch (InstantiationException | InvalidPathException e) {
            e.printStackTrace();
        }
    }

    private void testPackage(Path path) {
        try {
           new TestPackage(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkArgs(ArgParser parser) {
        return (parser.hasOption(CLASSPATH));
    }


    public static void main(String[] args) {
        RunTests rt = new RunTests();
        rt.run(args);
    }

    private void createOptions(ArgParser parser) {

        parser.addOption(CLASSPATH, true, "classpath for class or package");
        parser.addOption(CLASSES, true, "classes to be tested. If empty run tests through all *.class files if classpath.\nFor several classes use pattern: \"class1 class2 ... classN\"");
    }


    private void run(String[] args) {
        ArgParser parser = new ArgParser();
        createOptions(parser);

        parser.parse(args);

        if (!checkArgs(parser)) {
            parser.printUsage();
            return;
        }

        Path classpath = Paths.get(parser.getOption(CLASSPATH));

        if (parser.hasOption(CLASSES)) {
            List<String> classes = Arrays.asList(parser.getOption(CLASSES).split("\\s"));

            if (classes.size() > 0) {
                for (String path: classes) {
                    testClass(classpath, Paths.get(path));
                }
            } else {
                parser.printUsage();
            }

        } else {
            testPackage(classpath);
        }
    }
}
