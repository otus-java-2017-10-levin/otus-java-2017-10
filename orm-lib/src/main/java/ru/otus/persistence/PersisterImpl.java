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
import java.util.*;

public class PersisterImpl implements Persister {

    private final AnnotationManager annotationManager;
    private final DbManager dbManager;
    private final EntityVisitor visitor;
    private EntityStructureQueueBuilder<VisitableEntity> queueBuilder;
    private Set<Object> objectsWrite;

    private PersisterImpl(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.dbManager = dbManager;
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
        objectsWrite = new HashSet<>();
        queueBuilder = new EntityStructureQueueBuilderImpl<>();
        createObjectTree(object);
        EntityStructureQueue<VisitableEntity> queue = queueBuilder.build();

        queue.forEach(visitable -> {
            try {
                visitor.visit(visitable);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        AnnotatedField id = annotationManager.getId(object.getClass());
        try {
            return (long) id.getFieldValue(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /*
        Post order DFS
     */
    private void createObjectTree(@NotNull Object object) {
        if (objectsWrite.contains(object))
            return;

        AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(object.getClass());
        objectsWrite.add(object);

        for (AnnotatedField f : annotatedClass.getFields(OneToOne.class)) {
            try {
                Object value = Objects.requireNonNull(f.getFieldValue(object));
                createObjectTree(value);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        queueBuilder.add(new EntityStructure(object, annotatedClass));
    }

    @Override
    public <T> @Nullable T find(@NotNull Class<T> entityClass, long primaryKey) {

        T cls = null;
//        try {
//            try (DBConnection connection = dbManager.getConnection()) {
//                cls = connection.execQuery(QueryFactory.getSelectQuery(annotationManager, entityClass, primaryKey), result -> {
//
//                    ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
//                    ResultSetMetaData rsmd = result.getMetaData();
//
//                    result.next();
//                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//                        String name = rsmd.getColumnName(i);
//                        AnnotatedField field = annotationManager.getField(entityClass, name);
//                        builder.set(field, result.getObject(i));
//                    }
//                    return builder.build();
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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
