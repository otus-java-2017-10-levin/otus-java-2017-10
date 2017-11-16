package ru.otus.internals;


import ru.otus.annotations.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 *  Load annotations for object
 */

class TestAnnotations {
    private final Map<AnnotationType, Annotation> annotationMap = new HashMap<>();

    TestAnnotations(Annotation[] annotations) {
        for (Annotation anno: annotations) {
            AnnotationType type = getAnnotationType(anno);

            if (type != AnnotationType.EMPTY) {
                if (hasAnnotation(type)) {
                    throw new IllegalStateException();
                }
                annotationMap.put(type, anno);

            }
        }
    }

    boolean hasAnnotation(AnnotationType type) {
        return annotationMap.containsKey(type);
    }

    static boolean containTests(Annotation[] annotations) {

        if (annotations != null) {
            for (Annotation anno : annotations) {
                if (getAnnotationType(anno) != AnnotationType.EMPTY)
                    return true;
            }
        }
        return false;
    }

    static AnnotationType getAnnotationType(Annotation annotation) {
        if (annotation == null)
            throw new IllegalArgumentException("Annotation = null");

        if (annotation instanceof After) {

            return AnnotationType.AFTER;
        }
        if (annotation instanceof Before) {

            return AnnotationType.BEFORE;
        }
        if (annotation instanceof Skip) {

            return AnnotationType.SKIP;
        }
        if (annotation instanceof Test) {

            return AnnotationType.TEST;
        }
        return AnnotationType.EMPTY;
    }
}
