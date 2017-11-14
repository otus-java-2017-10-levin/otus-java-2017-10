package ru.otus.internals;

import ru.otus.annotations.AnnotationType;
import ru.otus.annotations.Test;
import ru.otus.common.ReflectionHelper;


/**
 *  Keep method annotations, annotation type, method name
 */

class TestMethod {

    private TestAnnotations annotations;
    private Class<?> clazz;
    private String name;

    TestMethod(Class<?> clazz, String name) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class = null");
        }

        this.clazz = clazz;
        this.name = name;
        annotations = new TestAnnotations(ReflectionHelper.getMethodAnnotations(clazz, name));
    }

    private void invoke(Object inst) throws RuntimeException  {

            ReflectionHelper.callMethod(inst, name);
    }

    boolean hasAnnotation(AnnotationType type) {
        return annotations.hasAnnotation(type);
    }

    void test(Object instance) throws RuntimeException {
        invoke(instance);
    }

    String getName(){
        String res = "";
        if (annotations.hasAnnotation(AnnotationType.TEST)) {
                Test anno = ReflectionHelper.getMethodAnnotation(clazz, name, Test.class);
                if (anno != null) {
                    res = anno.value();
                }
        }

        if (res.equals("")) {
            res = this.name;
        }

        return res;
    }
}
