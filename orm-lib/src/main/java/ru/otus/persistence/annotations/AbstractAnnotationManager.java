package ru.otus.persistence.annotations;

/*
 * Keeps information about all annotated classes for persistence
 * Validate annotations on annotated classes
 *
 */

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractAnnotationManager {
    protected Map<Class<?>, AnnotatedClass> annotatedClasses;

    public AbstractAnnotationManager(@NotNull Class<?>... classes) {
        for (Class<?> cl : classes) {
            addClass(cl);
        }
        validateClasses();
    }

    public AbstractAnnotationManager(@NotNull String... classes) throws ClassNotFoundException {
        for (String cl : classes) {
            Class<?> loadedClass = Class.forName(cl);
            addClass(loadedClass);
        }
        validateClasses();
    }

    @NotNull
    public AnnotatedClass getAnnotatedClass(@NotNull Class<?> cl) {
        if (!annotatedClasses.containsKey(cl))
            throw new IllegalArgumentException("class is not in annotated classes list");

        return annotatedClasses.get(cl);
    }

    public boolean contains(@NotNull Class<?> cl) {
        return annotatedClasses.containsKey(cl);
    }

    public Set<AnnotatedClass> getAllClasses() {
        return new HashSet<>(annotatedClasses.values());
    }

    @NotNull
    public AnnotatedField getField(@NotNull Class<?> cl, @NotNull String name) {
        return getAnnotatedClass(cl).getField(name);
    }

    /*        cl already verified    */
    private void addClass(@NotNull Class<?> cl) {
        if (annotatedClasses == null)
            annotatedClasses = new HashMap<>();

        if (annotatedClasses.containsKey(cl)) {
            throw new IllegalArgumentException("duplicate class: " + cl);
        }
        AnnotatedClass ac = AnnotatedClassImpl.of(cl);
        validateClass(ac);
        annotatedClasses.put(cl, ac);
    }

    protected abstract void validateClass(@NotNull AnnotatedClass ac);

    protected abstract void validateClasses();
}
