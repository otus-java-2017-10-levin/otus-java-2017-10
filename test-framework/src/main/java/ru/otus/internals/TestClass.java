package ru.otus.internals;

import ru.otus.annotations.AnnotationType;
import ru.otus.common.PathClassLoader;
import ru.otus.common.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Keeps information for testing class
 */

@SuppressWarnings("ALL")
public class TestClass {

    private final List<TestMethod> methods = new ArrayList<>();
    private Class<?> clazz;
    private final TestSummary summary = new TestSummary();

    public TestClass(Path path, Path classNamePath) throws InstantiationException {

        PathClassLoader pathClassLoader = new PathClassLoader(path);

        Class<?> clazz = pathClassLoader.loadClass(classNamePath);

        createTestClass(clazz);
    }

    public TestClass(Class<?> clazz) throws InstantiationException {
        createTestClass(clazz);
    }

    private void createTestClass(Class<?> clazz) throws InstantiationException {

        if (clazz == null)
            throw new IllegalArgumentException("Class is null");

        this.clazz = clazz;

        for (String name : ReflectionHelper.getMethods(clazz)) {
            Annotation[] annotations = ReflectionHelper.getMethodAnnotations(clazz, name);

            if (TestAnnotations.containTests(annotations))
                methods.add(new TestMethod(clazz, name));
        }

        if (!checkValidTestClass()) {
            throw new InstantiationException("To many @Before or @After");
        }

    }

    /*
        Check:
        1. Less than 2 @Before method
        2. Only one @After method
     */
    private boolean checkValidTestClass() {
        if (getMethods(AnnotationType.BEFORE).size() > 1)
            return false;

        return getMethods(AnnotationType.AFTER).size() <= 1;
    }

    public TestSummary test() {
        System.out.println("Test class (" + clazz.getCanonicalName() + ") stats:");

        for (TestMethod method : getMethods(AnnotationType.TEST)) {
            Object instance = ReflectionHelper.instantiate(clazz);

            testMethods(instance, AnnotationType.BEFORE);
            testMethod(instance, method);
            testMethods(instance, AnnotationType.AFTER);
        }
        System.out.println(summary);
        return summary;
    }

    boolean hasMethods() {
        return methods.size() > 0;
    }

    private void beforeOdAfterTest(Object instance, TestMethod method) {
        if (!method.hasAnnotation(AnnotationType.SKIP)) {
            try {
                method.test(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void testMethod(Object instance, TestMethod method) {
        System.out.print("\t\t" + method.getName() + ": ");
        if (!method.hasAnnotation(AnnotationType.SKIP)) {
            try {
                method.test(instance);
                System.out.println("succeeded.\n");
            } catch (Exception e) {
                System.out.println("failed.");
                System.out.println(e.getCause() + "\n");

                if (method.hasAnnotation(AnnotationType.TEST)) {
                    summary.add(TestSummary.TYPE.FAILED, 1);
                }

                return;
            }
        } else {
            System.out.println("skipped.\n");
            if (method.hasAnnotation(AnnotationType.TEST)) {
                summary.add(TestSummary.TYPE.IGNORE, 1);
            }
            return;
        }

        if (method.hasAnnotation(AnnotationType.TEST)) {
            summary.add(TestSummary.TYPE.SUCCESS, 1);
        }
    }

    private List<TestMethod> getMethods(AnnotationType type) {
        return methods.stream().filter(testMethod -> testMethod.hasAnnotation(type)).collect(Collectors.toList());
    }

    private void testMethods(Object instance, AnnotationType type) {
        for (TestMethod testMethod : getMethods(type)) {
            beforeOdAfterTest(instance, testMethod);
        }
    }
}
