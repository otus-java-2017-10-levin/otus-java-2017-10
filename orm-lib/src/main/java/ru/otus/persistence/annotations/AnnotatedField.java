package ru.otus.persistence.annotations;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public final class AnnotatedField {
    private final Field field;
    private final Map<Class<? extends Annotation>, ? extends Annotation> annotations;

    AnnotatedField(Field field) {
        if (field == null)
            throw new IllegalArgumentException();

        annotations = Arrays.stream(field.getDeclaredAnnotations())
                .collect(Collectors.toMap(Annotation::annotationType, annotation -> annotation));

        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Field getField() {
        return field;
    }

    public boolean contains(Class<? extends Annotation> annotation) {
        return annotations.containsKey(annotation);
    }

    public int getAnnotationCount() {
        return annotations.size();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return annotationClass.cast(annotations.get(annotationClass));
    }
}