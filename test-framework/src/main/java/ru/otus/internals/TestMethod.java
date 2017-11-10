package ru.otus.internals;

import ru.otus.annotations.AnnotationType;
import ru.otus.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  Keep method annotations, annotation type, method name
 */

class TestMethod {

    private TestAnnotations annotations;
    private Method method;

    TestMethod(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Method = null");
        }

        this.method = method;
        annotations = new TestAnnotations(method.getDeclaredAnnotations());
    }

    void invoke(Object inst) throws InvocationTargetException {

        try {
            method.invoke(inst);
        } catch (RuntimeException e) {
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    TestAnnotations getAnnotations() {
        return annotations;
    }

    boolean hasAnnotation(AnnotationType type) {
        return annotations.hasAnnotation(type);
    }

    void test(Object instance) throws InvocationTargetException {
        invoke(instance);
    }

    String getName(){
        String res = "";

        if (method.isAnnotationPresent(Test.class)) {
            Test anno = method.getDeclaredAnnotation(Test.class);
            res = anno.value();
        }

        if (res.equals("")) {
            res = method.getName();
        }

        return res;
    }
}
