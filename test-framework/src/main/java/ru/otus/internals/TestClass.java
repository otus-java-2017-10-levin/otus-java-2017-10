package ru.otus.internals;

import ru.otus.annotations.AnnotationType;
import ru.otus.common.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Keeps information for testing class
 */

public class TestClass {

    private final List<TestMethod> methods = new ArrayList<>();
    private Class<?> clazz;
    private int ignoreTests = 0;
    private int successTests = 0;
    private int failedTests = 0;

    TestClass(Class<?> clazz) {

        if (clazz == null)
            throw new IllegalArgumentException("Class is null");

        this.clazz = clazz;

        for (String name : ReflectionHelper.getMethods(clazz)) {
            Annotation[] annotations = ReflectionHelper.getMethodAnnotations(clazz, name);

            if (TestAnnotations.containTests(annotations))
                methods.add(new TestMethod(clazz, name));
        }
    }

    public void test(Class<?> clazz) {
        System.out.println("Test class (" + clazz.getCanonicalName() + ") stats:");
        Object instance = ReflectionHelper.instantiate(clazz);
        testMethods(instance, AnnotationType.BEFORE);

        for (TestMethod method : getMethods(AnnotationType.TEST)) {

            if (!method.hasAnnotation(AnnotationType.SKIP)) {
                testMethods(instance, AnnotationType.BEFORE_EACH);
                testMethod(instance, method);
                testMethods(instance, AnnotationType.AFTER_EACH);
            } else {
                testMethod(instance, method);
            }
        }
        testMethods(instance, AnnotationType.AFTER);

        System.out.println(String.format("Success: %d; Failed: %d; Ignore: %d", successTests, failedTests, ignoreTests));
    }

    private void testMethod(Object instance, TestMethod method) {

        System.out.print("\t\t" + method.getName() + " : ");
        if (!method.hasAnnotation(AnnotationType.SKIP)) {
            try {
                method.test(instance);
                System.out.println("succeeded.\n");
            } catch (Exception e) {
                System.out.println("failed.");
                System.out.println(e.getCause() + "\n");
                failedTests = increment(failedTests, method);
                return;
            }
        } else {
            System.out.println("skipped.\n");
            ignoreTests = increment(ignoreTests, method);
            return;
        }
        successTests = increment(successTests, method);
    }

    private List<TestMethod> getMethods(AnnotationType type) {
        return methods.stream().filter(testMethod -> testMethod.hasAnnotation(type)).collect(Collectors.toList());
    }

    private void testMethods(Object instance, AnnotationType type) {
        for (TestMethod testMethod : getMethods(type)) {
            testMethod(instance, testMethod);
        }
    }

    private int increment(int counter, TestMethod method) {
        if (method.hasAnnotation(AnnotationType.TEST))
            counter++;
        return counter;
    }
}
