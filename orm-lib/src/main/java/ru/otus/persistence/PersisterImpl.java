package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;

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


        long id = entityStructure.apply(visitor);

        for (AnnotatedField f: annotatedClass.getFields(OneToMany.class)) {
            Object value = f.getFieldValue(object);
            if (value == null)
                throw new IllegalStateException("OneToMany field is null");

            if (!Collection.class.isAssignableFrom(value.getClass()))
                throw new IllegalStateException("Only collections supported to @OneToMany");

            saveManyToOne((Collection<?>)value, id);
        }
        return id;
    }

    private void saveManyToOne(@NotNull Collection<?> value, long id) {
        value.forEach(object -> {
            Class<?> componentType = object.getClass();
            AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(componentType);
            EntityStructure str = new EntityStructure(object, annotatedClass);
            str.setId(id);
            try {
                str.apply(visitor);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
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

        return visitor.load(entityClass, primaryKey);
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
