package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Arrays;

public class PersisterImpl implements Persister {

    private final AnnotationManager annotationManager;
    private final EntityVisitor visitor;

    private PersisterImpl(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.visitor = new SQLEntityVisitor(annotationManager, dbManager);
    }

    @Override
    public long save(@NotNull Object object) {
        try {
            return writeObject(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    private long writeObject(@NotNull Object object) throws IllegalAccessException {

        AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(object.getClass());

        EntityStructure entityStructure = new EntityStructure(object, annotatedClass);

        return entityStructure.save(visitor);
    }

    @Override
    public void updateKeys(@NotNull Object object) throws IllegalAccessException {
        AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(object.getClass());

        ForeignKeys key = new ForeignKeys(getId(object), object.getClass());

        for (AnnotatedField field: annotatedClass.getFields(Arrays.asList(OneToOne.class, ManyToOne.class))) {
            Object value = field.getFieldValue(object);

            if (value == null)
                    throw new IllegalAccessException();

            long id = getId(value);
            key.addKey(field.getName(), id);
        }
        if (key.getKeys().size() != 0)
            key.save(visitor);
    }

    private long getId(@NotNull Object object) throws IllegalAccessException {
        AnnotatedField f = annotationManager.getId(object.getClass());
        return  (long)f.getFieldValue(object);
    }

    @Override
    public <T> @Nullable T find(@NotNull Class<T> entityClass, long primaryKey) {
        return visitor.visit(entityClass, primaryKey);
    }


    public static class Builder implements PersisterBuilder {

        private AnnotationManager annotationManager;
        private DbManager dbManager;

        @Override
        public @NotNull PersisterBuilder setDbManager(@NotNull DbManager manager) {
            dbManager = manager;
            return this;
        }

        @Override
        public @NotNull PersisterBuilder setAnnotationManager(@NotNull AnnotationManager manager) {
            annotationManager = manager;
            return this;
        }

        @Override
        public @NotNull Persister build() {
            return new PersisterImpl(annotationManager, dbManager);
        }
    }
}
