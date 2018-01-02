package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.annotations.AbstractAnnotationManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class AnnotationManager extends AbstractAnnotationManager {

    private final Class<? extends Annotation> id;

    public AnnotationManager(Class<? extends Annotation> idAnnotation, Class<?>... classes) {
        super(classes);
        this.id = idAnnotation;
    }

    public AnnotationManager(Class<? extends Annotation> idAnnotation, String... classes) throws ClassNotFoundException {
        super(classes);
        this.id = idAnnotation;
    }

    public AnnotatedField getId(Class<?> cl) {
        AnnotatedClass ac = getAnnotatedClass(cl);

        List<AnnotatedField> fields = ac.getFields(id);

        assert fields.size() > 0;
        return fields.get(0);
    }

    @Override
    protected void validateClass(@NotNull AnnotatedClass ac) {

    }

    @Override
    protected void validateClasses() {

    }
}
