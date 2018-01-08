package ru.otus.persistence;

import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Objects;

public class SQLEntityVisitor implements EntityVisitor {
    private final AnnotationManager annotationManager;
    private final DbManager dbManager;

    public SQLEntityVisitor(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.dbManager = dbManager;
    }

    @Override
    public long visit(VisitableEntity visitable) throws IllegalAccessException {
        AnnotatedClass annotatedClass = visitable.getEntityClass();
        Object entity = visitable.getEntity();
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
}
