package ru.otus.persistence.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotatedClass {
    @NotNull
    Class<?> getAnnotatedClass();

    @NotNull
    List<AnnotatedField> getFields();

    boolean is(@NotNull Object entity);

    @NotNull
    String getSimpleName();

    @NotNull
    AnnotatedField getField(@NotNull String fieldName);

    @NotNull
    List<AnnotatedField> getFields(@NotNull Class<? extends Annotation> annotation);
}
