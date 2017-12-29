package ru.otus.persistence;

import ru.otus.annotations.AnnotatedClass;
import ru.otus.annotations.AnnotationField;
import ru.otus.base.UsersDataSet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class QueryFactory {
    private static final String CREATE_IF_NOT_EXIST = "create table if not exists ";
    private static final String ID_FIELD = " bigint auto_increment";
    private static final String PRIMARY_KEY = "primary key (";

    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = "VALUES ";

    private static final String DROP_TABLE = "DROP TABLE ";
    private static final String IF_EXIST = " IF EXISTS";

    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String WHERE_ID = "WHERE ID = ";


    public static String getSelectQuery(AnnotatedClass annotatedClass, long key) {
        if (annotatedClass == null || key < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(SELECT).append(" ");
        sb.append(getFieldNames(annotatedClass, false)).append(" ");

        sb.append(FROM).append(" ").append(annotatedClass.getSimpleName()).append(" ");
        sb.append(WHERE_ID).append(key);
        return sb.toString().toUpperCase();
    }

    public static String createTableQuery(AnnotatedClass annotatedClass) {
        if (annotatedClass == null)
            throw new IllegalArgumentException();

        StringBuilder query = new StringBuilder(CREATE_IF_NOT_EXIST);

        query.append(annotatedClass.getSimpleName()).append(" (");

        AnnotationField id = annotatedClass.getId();
        if (id == null)
            throw new IllegalArgumentException("no @Id field");

        query.append(getFieldNames(annotatedClass, true));


        query.append(", ").append(PRIMARY_KEY).append(id.getName()).append("))");
        return query.toString().toUpperCase();
    }

    public static String getInsertQuery(AnnotatedClass annotatedClass) {

        StringBuilder sb = new StringBuilder(INSERT);
        sb.append(annotatedClass.getSimpleName()).append(" (");
        sb.append(getFieldNames(annotatedClass, false)).append(") ");
        sb.append(VALUES).append("(");

        int count = 0;
        for (int i = 0; i < annotatedClass.getFields().size(); i++) {
            if (count++ != 0)
                sb.append(",");
            sb.append("?");
        }

        sb.append(")");

        return sb.toString().toUpperCase();
    }

    public static String getDropTableQuery(AnnotatedClass annotatedClass) {

        return (DROP_TABLE + annotatedClass.getSimpleName() + IF_EXIST).toUpperCase();
    }

    private static String getFieldNames(AnnotatedClass annotatedClass, boolean isTypeInfo) {
        StringBuilder query = new StringBuilder();
        int count = 0;
        for (AnnotationField f : annotatedClass.getFields()) {
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

    private static String getFieldString(AnnotationField field) {
        if (field == null)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(field.getName());
        Class<?> type = field.getType();

        if (field.isPrimaryKey()) {
            sb.append(ID_FIELD);
        } else {
            String sqlType = SQLtypes.get(type);

            if (sqlType != null)
                sb.append(" ").append(sqlType);
            else
                throw new IllegalStateException("no supported type " + field.getType());
        }
        return sb.toString();
    }

    private static Map<Class<?>, String> SQLtypes = new HashMap<>();

    static {
        SQLtypes.put(boolean.class, "BIT");
        SQLtypes.put(Boolean.class, "BIT");
        SQLtypes.put(byte.class, "SMALLINT");
        SQLtypes.put(Byte.class, "SMALLINT");
        SQLtypes.put(short.class, "SMALLINT");
        SQLtypes.put(Short.class, "SMALLINT");
        SQLtypes.put(char.class, "CHAR");
        SQLtypes.put(Character.class, "CHAR");
        SQLtypes.put(int.class, "INT");
        SQLtypes.put(Integer.class, "INT");
        SQLtypes.put(long.class, "BIGINT");
        SQLtypes.put(Long.class, "BIGINT");
        SQLtypes.put(float.class, "FLOAT");
        SQLtypes.put(Float.class, "FLOAT");
        SQLtypes.put(double.class, "DOUBLE");
        SQLtypes.put(Double.class, "DOUBLE");
        SQLtypes.put(String.class, "VARCHAR(256)");
    }
}