package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.annotations.AbstractAnnotationManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationManager extends AbstractAnnotationManager {

    AnnotationManager(Class<? extends Annotation> idAnnotation, Class<?>... classes) {
        super(idAnnotation, classes);
    }

    AnnotationManager(Class<? extends Annotation> idAnnotation, String... classes) throws ClassNotFoundException {
        super(idAnnotation, classes);
    }

    AnnotatedField getId(Class<?> cl) {
        AnnotatedClass ac = getAnnotatedClass(cl);

        List<AnnotatedField> fields = ac.getFields(idAnnotation);

        if (fields.size() != 1)
            throw new IllegalArgumentException("@id fields count != 1");
        return fields.get(0);
    }

    @Override
    protected void validateClass(@NotNull AnnotatedClass ac) {
        if (!isSingleId(ac))
            throw new IllegalArgumentException("No or multiple @Id in "+ac.getSimpleName()+" class");
    }

    private boolean isSingleId(@NotNull AnnotatedClass ac) {
        return ac.getFields(idAnnotation).size() == 1;
    }

    @Override
    protected void validateClasses() {
        for (Map.Entry<Class<?>, AnnotatedClass> entry: annotatedClasses.entrySet()) {
            AnnotatedClass cl = entry.getValue();

            validateOneToOne(cl);
            validateManyToOne(cl);
        }
    }

    private void validateOneToOne(@NotNull AnnotatedClass cl) {
        List<AnnotatedField> oneToOne = cl.getFields(OneToOne.class);
        for (AnnotatedField field: oneToOne) {
            Class<?> oneClass = field.getType();
            if (!contains(oneClass))
                throw new IllegalArgumentException(oneClass.getSimpleName()+" is not an entity");
        }
    }

    private void validateManyToOne(AnnotatedClass cl) {
        List<AnnotatedField> manyToOne = cl.getFields(ManyToOne.class);
        for (AnnotatedField field: manyToOne) {
            Class<?> manyClass = field.getType();
            final String name = field.getName();
            if (!contains(manyClass))
                throw new IllegalArgumentException("Class in @ManyToOne relationship is not an entity.");

            /// there can be several one to many relationship
            AnnotatedClass onetoManyClass = getAnnotatedClass(manyClass);

            long count = onetoManyClass.getFields(OneToMany.class).stream().filter(f -> {
                OneToMany anno = f.getAnnotation(OneToMany.class);
                return name.equals(anno.mappedBy());
            }).count();
            if (count != 1)
                throw new IllegalArgumentException("No or duplicate @OneToMany");
        }
    }
}
