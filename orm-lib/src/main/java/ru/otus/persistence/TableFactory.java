package ru.otus.persistence;

import ru.otus.annotations.AnnotatedClass;
import ru.otus.base.UsersDataSet;

import java.lang.reflect.Field;

public class TableFactory {
    private static final String CREATE_IF_NOT_EXIST = "create table if not exists ";
    private static final String ID_FIELD = " bigint auto_increment";
    private static final String PRIMARY_KEY = "primary key (";

    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = "VALUES ";

    private static final String DROP_TABLE = "DROP TABLE ";
    private static final String IF_EXIST = " IF EXISTS";

    public static String getQuery(AnnotatedClass annotatedClass) {
        if (annotatedClass == null)
            throw new IllegalArgumentException();

        StringBuilder query = new StringBuilder(CREATE_IF_NOT_EXIST);

        query.append(annotatedClass.getSimpleName().toUpperCase()).append(" (");

        Field id = annotatedClass.getId();
        if (id == null)
            throw new IllegalArgumentException("no @Id field");

        query.append(id.getName()).append(ID_FIELD).append(", ");
        query.append(getFieldNames(annotatedClass, true));


        query.append(", ").append(PRIMARY_KEY).append(id.getName()).append("))");
        return query.toString().toUpperCase();
    }

    private static String getFieldNames(AnnotatedClass annotatedClass, boolean isTypeInfo) {
        StringBuilder query = new StringBuilder();
        int count = 0;
        for (Field f: annotatedClass.getFields()) {
            if (count++ != 0) {
                query.append(", ");
            }
            if (isTypeInfo) {
                query.append(getFieldString(f));
            } else {
                query.append(f.getName());
            }
        }
        return query.toString();
    }

    //TODO: support all plain classes from JPA spec
    private static String getFieldString(Field field) {
        if (field == null)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(field.getName());
        if (field.getType() == String.class) {
            sb.append(" varchar(256)");
            return sb.toString();
        }

        throw new IllegalStateException("no supported type " + field.getType() );
    }

    public static String getInsertQuery(AnnotatedClass annotatedClass) {

        StringBuilder sb = new StringBuilder(INSERT);
        sb.append(annotatedClass.getSimpleName()).append(" (");
        sb.append(getFieldNames(annotatedClass, false)).append(") ");
        sb.append(VALUES).append("(");

        int count = 0;
        for (int i =0; i < annotatedClass.getFields().size(); i++) {
            if (count++ != 0)
                sb.append(",");
            sb.append("?");
        }

        sb.append(")");

        return sb.toString().toUpperCase();
    }

    public static String getDropTableQuery(AnnotatedClass annotatedClass) {
        StringBuilder sb = new StringBuilder(DROP_TABLE);
        sb.append(annotatedClass.getSimpleName()).append(IF_EXIST);

        return sb.toString().toUpperCase();
    }
}
