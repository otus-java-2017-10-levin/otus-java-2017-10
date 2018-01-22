package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PersisterImpl implements Persister {

    private final AnnotationManager annotationManager;
    private final EntityVisitor visitor;
    private final Map<Class<?>, DBConnection> connections = new HashMap<>();
    private final DbManager dbManager;

    private PersisterImpl(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.visitor = new SQLEntityVisitor(annotationManager);
        this.dbManager = dbManager;

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
        Class<?> entityClass = object.getClass();
        if (!connections.containsKey(entityClass)) {
            connections.put(entityClass, dbManager.getConnection());
        }

        AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(entityClass);

        EntityStructure entityStructure = new EntityStructure(object, annotatedClass);

        return entityStructure.save(visitor, connections.get(entityClass));
    }

    @Override
    public void updateKeys(@NotNull Object object) throws IllegalAccessException {
        Class<?> entityClass = object.getClass();
        AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(entityClass);

        ForeignKeys key = new ForeignKeys(getId(object), entityClass);

        for (AnnotatedField field: annotatedClass.getFields(Arrays.asList(OneToOne.class, ManyToOne.class))) {
            Object value = field.getFieldValue(object);

            if (value == null)
                    throw new IllegalAccessException();

            long id = getId(value);
            key.addKey(field.getName(), id);
        }
        if (key.getKeys().size() != 0)
            key.save(visitor, connections.get(entityClass));
    }

    private long getId(@NotNull Object object) throws IllegalAccessException {
        AnnotatedField f = annotationManager.getId(object.getClass());
        return  (long)f.getFieldValue(object);
    }

    @Override
    public <T> @Nullable T find(@NotNull Class<T> entityClass, long primaryKey) {
        if (!connections.containsKey(entityClass)) {
            connections.put(entityClass, dbManager.getConnection());
        }

        return visitor.load(entityClass, primaryKey, connections.get(entityClass));
    }

    @Override
    public void commit() throws Exception {
        for (Map.Entry<Class<?>, DBConnection> entry: connections.entrySet()) {
            entry.getValue().commit();
        }
    }

    @Override
    public void rollback() throws Exception {
        for (Map.Entry<Class<?>, DBConnection> entry: connections.entrySet()) {
            entry.getValue().rollback();
        }
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
