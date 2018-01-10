package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.persistence.fields.*;

import javax.persistence.OneToOne;
import java.sql.ResultSetMetaData;

public class PersisterImpl implements Persister {

    private final AnnotationManager annotationManager;
    private final EntityVisitor visitor;

    private PersisterImpl(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.visitor = new SQLEntityVisitor(annotationManager, dbManager);
    }

    private Field<?> getObjectIdField(@NotNull Object object) {
        AnnotatedField id = annotationManager.getId(object.getClass());
        Field<?> field1 = null;
        try {
            field1 = Fields.getField(id, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return field1;
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

        return entityStructure.apply(visitor);
    }

    @Override
    public void updateKeys(@NotNull Object object) throws IllegalAccessException {
        AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(object.getClass());

        ForeignKeys key = new ForeignKeys(getId(object), object.getClass());

        for (AnnotatedField f: annotatedClass.getFields(OneToOne.class)) {
            Object value = f.getFieldValue(object);
            if (value == null)
                throw new IllegalAccessException();
            long id = getId(value);
            key.addKey(f.getName(), id);
        }
        key.apply(visitor);
    }

    private long getId(@NotNull Object object) throws IllegalAccessException {
        AnnotatedField f = annotationManager.getId(object.getClass());
        return  (long)f.getFieldValue(object);
    }

    @Override
    public <T> @Nullable T find(@NotNull Class<T> entityClass, long primaryKey) {

        T cls = null;
        visitor.load(entityClass, primaryKey);
        return cls;
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
