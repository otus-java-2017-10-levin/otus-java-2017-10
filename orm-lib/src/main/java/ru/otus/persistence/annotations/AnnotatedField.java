package ru.otus.persistence.annotations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface AnnotatedField {
    @NotNull
    String getName();

    @NotNull
    Class<?> getType();

    @NotNull
    Field getField();

    boolean contains(@NotNull Class<? extends Annotation> annotation);

    int getAnnotationCount();

    @Nullable
    <S extends Annotation> S getAnnotation(Class<S> annotationClass);

    @Nullable
    Object getFieldValue(@NotNull Object object) throws IllegalAccessException;

    void setFieldValue(@NotNull Object target, @NotNull Object value) throws IllegalAccessException;
}
