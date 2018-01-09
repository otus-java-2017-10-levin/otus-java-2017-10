package ru.otus.persistence.annotations;

import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.Constraint;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 *
 */
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
