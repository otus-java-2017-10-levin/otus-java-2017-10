package ru.otus.persistence.fields;


import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.AnnotationManager;
import ru.otus.persistence.annotations.AnnotatedClass;

public interface EntityInstanceBuilder {

    @NotNull
    <T> EntityInstance<T> build(@NotNull T object, @NotNull AnnotationManager manager);
}
