package ru.otus.persistence;

import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

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

        AnnotatedField id = annotatedClass.getId();
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
        for (AnnotatedField f: annotatedClass.getFields()) {
            if (count++ != 0)
                sb.append(",");
            if (f.isPrimaryKey())
                sb.append("null");
            else
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
        for (AnnotatedField f : annotatedClass.getFields()) {
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

    private static String getFieldString(AnnotatedField field) {
        if (field == null)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(field.getName());

        String sqlType = sqlTypes.get(field.getType());

        if (field.isPrimaryKey()) {
            sb.append(ID_FIELD);
        } else {

            if (sqlType != null)
                sb.append(" ").append(sqlType);
            else
                throw new IllegalStateException("no supported type " + field.getType());
        }
        return sb.toString();
    }

    private static final Map<Class<?>, String> sqlTypes = new HashMap<>();

    static {
        sqlTypes.put(boolean.class, "BIT");
        sqlTypes.put(Boolean.class, "BIT");
        sqlTypes.put(byte.class, "tinyint");
        sqlTypes.put(Byte.class, "tinyint");
        sqlTypes.put(short.class, "SMALLINT");
        sqlTypes.put(Short.class, "SMALLINT");
        sqlTypes.put(char.class, "VARCHAR");
        sqlTypes.put(Character.class, "VARCHAR");
        sqlTypes.put(int.class, "INT");
        sqlTypes.put(Integer.class, "INT");
        sqlTypes.put(long.class, "BIGINT");
        sqlTypes.put(Long.class, "BIGINT");
        sqlTypes.put(float.class, "FLOAT");
        sqlTypes.put(Float.class, "FLOAT");
        sqlTypes.put(double.class, "DOUBLE");
        sqlTypes.put(Double.class, "DOUBLE");
        sqlTypes.put(String.class, "VARCHAR(256)");
    }
}