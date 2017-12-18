package ru.otus.orm;

import java.lang.annotation.Annotation;

public class AnnotationProcessorHandler implements AnnotationProcessor {

    @Override
    public OrmField processId(Annotation[] annotations) throws IllegalArgumentException {
//        OrmField.builder().
        return null;
    }

    @Override
    public OrmField processColumn(Annotation[] annotations) throws IllegalArgumentException {
        return null;
    }
}
