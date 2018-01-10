package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SQLEntityVisitor implements EntityVisitor {
    private final AnnotationManager annotationManager;
    private final DbManager dbManager;

    SQLEntityVisitor(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.dbManager = dbManager;
    }

    @Override
    public long visit(EntityStructure structure) throws IllegalAccessException {
        AnnotatedClass annotatedClass = structure.getEntityClass();
        Object entity = structure.getEntity();
        AnnotatedField idField = annotationManager.getId(annotatedClass.getAnnotatedClass());
        try (DBConnection connection = dbManager.getConnection()) {
            connection.execQuery(QueryFactory.getInsertQuery(annotationManager, annotatedClass.getAnnotatedClass()), statement -> {
                int pos = 1;
                for (AnnotatedField f : annotatedClass.getFields()) {
                    try {
                        Object value = Objects.requireNonNull(f.getFieldValue(entity));
                        if (!f.contains(OneToOne.class) &&
                                !f.contains(OneToMany.class) &&
                                !f.contains(ManyToOne.class) &&
                                !f.contains(annotationManager.getIdAnnotation())) {
                            statement.setString(pos++, value.toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                statement.addBatch();
                int id[] = statement.executeBatch();
                try {
                    idField.setFieldValue(entity, id[0]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (long) idField.getFieldValue(entity);
    }

    @Override
    public <T> T load(@NotNull Class<T> entityClass, long primaryKey) {


        try {
            try (DBConnection connection = dbManager.getConnection()) {

                String selectQuery = QueryFactory.getSelectQuery(annotationManager, primaryKey, entityClass);
                return connection.execQuery(selectQuery, result -> {

                    ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
                    ResultSetMetaData rsmd = result.getMetaData();

                    result.next();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        rsmd.getColumnName(i);

                    }
                    return builder.build();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("object not found");
    }

    private <T> T buildObject(@NotNull Class<T> entityClass, Map<String, Object> params) {
        ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
        params.forEach((s, object) ->{
            AnnotatedField field = annotationManager.getField(entityClass, s);
            builder.set(field, object);
        });
        return builder.build();
    }

    @Override
    public void visit(ForeignKeys keys) {
        try (DBConnection connection = dbManager.getConnection()) {
            connection.execQuery(QueryFactory.getUpdateKeysQuery(annotationManager, keys.getEntityClass()), statement -> {
                int pos = 1;
                for (Long l: keys.getKeys().values())
                        statement.setLong(pos++, l);
                statement.setLong(pos, keys.getId());
                statement.execute();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}