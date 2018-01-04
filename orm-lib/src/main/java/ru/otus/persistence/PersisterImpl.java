package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.persistence.fields.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class PersisterImpl implements Persister {

    private final AnnotationManager annotationManager;
    private final DbManager dbManager;

    private PersisterImpl(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.dbManager = dbManager;
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
    public void saveOrUpdate(@NotNull Object object) {
        Field<?> field = getObjectIdField(object);
        AnnotatedField id = annotationManager.getId(object.getClass());

        if (field != null && field.isNullValue()) {
            try {
                saveObject(object, field, id);
            } catch (SQLException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            updateObject(object);
        }
    }

    private void updateObject(@NotNull Object object) {

    }

    private void saveObject(@NotNull Object object, @NotNull Field<?> field, @NotNull AnnotatedField idField) throws SQLException, IllegalAccessException {
        try (DBConnection connection = dbManager.getConnection()) {

            AnnotatedClass annotatedClass = annotationManager.getAnnotatedClass(object.getClass());
//                long id = connection.execQuery(QueryFactory.getNextInSequenceQuery(), result -> {
//                    long res = -1;
//                    result.next();
//                    res = result.getLong(1);
//                    return res;
//                });

//                idField.setFieldValue(object, id);

            connection.execQuery(QueryFactory.getInsertQuery(annotationManager, object.getClass()), statement -> {
                int pos = 1;
                for (AnnotatedField f : annotatedClass.getFields()) {
                    try {
                        if (!f.contains(annotationManager.getIdAnnotation())) {
                            statement.setString(pos++, Objects.requireNonNull(f.getFieldValue(object)).toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                statement.addBatch();
                int id[] = statement.executeBatch();
                try {
                    idField.setFieldValue(object, id[0]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> @Nullable T find(@NotNull Class<T> entityClass, long primaryKey) {

        T cls = null;
        try {
            try (DBConnection connection = dbManager.getConnection()) {
                cls = connection.execQuery(QueryFactory.getSelectQuery(annotationManager, entityClass, primaryKey), result -> {

                    ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
                    ResultSetMetaData rsmd = result.getMetaData();

                    result.next();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String name = rsmd.getColumnName(i);
                        AnnotatedField field = annotationManager.getField(entityClass, name);
                        builder.set(field, result.getObject(i));
                    }
                    return builder.build();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
