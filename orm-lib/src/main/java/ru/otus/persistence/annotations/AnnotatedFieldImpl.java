package ru.otus.persistence.annotations;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

final class AnnotatedFieldImpl implements AnnotatedField {
    private final Field field;

    private final Map<Class<? extends Annotation>, ? extends Annotation> annotations;

    AnnotatedFieldImpl(@NotNull Field field) {
        annotations = Arrays.stream(field.getDeclaredAnnotations())
                .collect(Collectors.toMap(Annotation::annotationType, annotation -> annotation));

        this.field = field;
    }

    @NotNull
    @Override
    public String getName() {
        return field.getName();
    }

    @NotNull
    @Override
    public Class getType() {
        return field.getType();
    }

    @NotNull
    @Override
    public Field getField() {
        return field;
    }

    @Override
    public boolean contains(@NotNull Class<? extends Annotation> annotation) {
        return annotations.containsKey(annotation);
    }

    @Override
    public int getAnnotationCount() {
        return annotations.size();
    }

    @Nullable
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return annotationClass.cast(annotations.get(annotationClass));
    }

    @Override
    @Nullable
    public Object getFieldValue(@NotNull Object object) throws IllegalAccessException {
        return FieldUtils.readField(field, object, true);
    }

    @Override
    public void setFieldValue(@NotNull Object target, @NotNull Object value) throws IllegalAccessException {
        FieldUtils.writeField(field, target, value, true);
    }
}