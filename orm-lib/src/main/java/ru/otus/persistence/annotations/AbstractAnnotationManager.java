package ru.otus.persistence.annotations;

/*
 * Keeps information about all annotated classes for persistence
 * Validate annotations on annotated classes
 *
 */

import org.jetbrains.annotations.NotNull;
import java.lang.annotation.Annotation;
import java.util.*;

public abstract class AbstractAnnotationManager {
    protected Map<Class<?>, AnnotatedClass> annotatedClasses;
    protected final Class<? extends Annotation> idAnnotation;

    protected AbstractAnnotationManager(@NotNull Class<? extends Annotation> idAnnotation,
                                        @NotNull Class<?>... classes) {
        this.idAnnotation = idAnnotation;
        for (Class<?> cl : classes) {
            addClass(cl);
        }
        validateClasses();
    }

    protected AbstractAnnotationManager(@NotNull Class<? extends Annotation> idAnnotation,
                                        @NotNull String... classes) throws ClassNotFoundException {
        this.idAnnotation = idAnnotation;
        for (String cl : classes) {
            Class<?> loadedClass = Class.forName(cl);
            addClass(loadedClass);
        }
        validateClasses();
    }

    @NotNull
    public Class<? extends Annotation> getIdAnnotation() {
        return idAnnotation;
    }

    @NotNull
    public <T> AnnotatedClass getAnnotatedClass(@NotNull Class<T> cl) {
        if (!annotatedClasses.containsKey(cl))
            throw new IllegalArgumentException("class is not in annotated classes list");

        return annotatedClasses.get(cl);
    }

    public boolean contains(@NotNull Class<?> cl) {
        return annotatedClasses.containsKey(cl);
    }

    public Set<Class<?>> getAllClasses() {
        return new HashSet<>(annotatedClasses.keySet());
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
