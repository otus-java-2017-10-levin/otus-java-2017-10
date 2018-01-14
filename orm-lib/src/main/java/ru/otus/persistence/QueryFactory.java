package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
class QueryFactory {
    private static final String CREATE_IF_NOT_EXIST = "create table if not exists ";
    private static final String ID_FIELD = "bigint";
    private static final String AUTO_INCREMENT = "auto_increment";
    private static final String PRIMARY_KEY = "primary key (";

    private enum State {
        INSERT,
        CREATE,
        NONE,
        SELECT,
        DROP,
        UPDATE;
    }
    private static State state = State.NONE;
    @NotNull
    public static String createTableQuery(@NotNull AnnotationManager manager,
                                          @NotNull Class<?> entityClass) {

        state = State.CREATE;
        StringBuilder query = new StringBuilder(CREATE_IF_NOT_EXIST);

        query.append(manager.getAnnotatedClass(entityClass).getSimpleName()).append(" (");
        query.append(getFieldNames(manager, entityClass, true));

        query.append(", ").append(PRIMARY_KEY);
        int counter = 0;
        AnnotatedClass annotatedClass = manager.getAnnotatedClass(entityClass);
        for (AnnotatedField af : annotatedClass.getFields(manager.getIdAnnotation())) {
            if (counter != 0)
                query.append(",");

            query.append(af.getName());
            counter++;
        }

        query.append("))");
        return query.toString().toUpperCase();
    }

    @NotNull
    public static String getInsertQuery(@NotNull AnnotationManager manager,
                                        @NotNull Class<?> entityClass, boolean hasId) {

        state = State.INSERT;
        AnnotatedClass annotatedClass = manager.getAnnotatedClass(entityClass);
        String INSERT = "INSERT INTO %s (%s) VALUES (%s)";

        StringBuilder values = new StringBuilder();
        int count = 0;
        for (AnnotatedField f : annotatedClass.getFields()) {
            if (!hasId && f.contains(manager.getIdAnnotation())) {
                continue;
            }
            if (f.contains(OneToMany.class))
                continue;
            if (count++ != 0) {
                values.append(",");
            }
//            if (f.contains(manager.getIdAnnotation())) {
//                values.append("NULL");
//            }
//            else {
                values.append("?");
//            }
        }

        return String.format(INSERT,
                annotatedClass.getSimpleName(),
                getFieldNames(manager, entityClass, hasId),
                values).toUpperCase();
    }

    @NotNull
    public static String getDropTableQuery(@NotNull AnnotationManager manager, Class<?> entityClass) {

        state = State.DROP;
        String DROP = "DROP TABLE %s IF EXISTS";
        return String.format(DROP, manager.getAnnotatedClass(entityClass).getSimpleName()).toUpperCase();
    }

    public static String getFKey(@NotNull Constraint constraint) {
        state = State.NONE;
        String ALTER = "alter table %s add constraint %s foreign key (%s) references %s";
        String constraintName = "FK" + constraint.getTable().getSimpleName()
                + constraint.getForeignTable().getSimpleName();

        return String.format(ALTER,
                constraint.getTable().getSimpleName(),
                constraintName,
                constraint.getFieldName(),
                constraint.getForeignTable().getSimpleName()).toUpperCase();
    }

    public static String getUpdateKeysQuery(@NotNull AnnotationManager manager,
                                            @NotNull Class<?> cl) {
        state = State.UPDATE;
        String UPDATE = "update %s set %s where %s=?";

        StringBuilder fields = new StringBuilder();
        AnnotatedClass annotatedClass1 = manager.getAnnotatedClass(cl);
        int count = 0;
        for (AnnotatedField f : annotatedClass1.getFields(OneToOne.class)) {
            if (count++ > 0)
                fields.append(" and ");
            fields.append(f.getName()).append("=?");
        }

        return String.format(UPDATE,
                cl.getSimpleName(),
                fields,
                manager.getId(cl).getName()
        ).toUpperCase();

    }

    @NotNull
    public static String getSelectQuery(@NotNull AnnotationManager man,
                                        @NotNull Class<?> entityClass,
                                        long key) {
        state = State.SELECT;
        AnnotatedClass ac = man.getAnnotatedClass(entityClass);
        String SELECT = "SELECT %s FROM %s WHERE " + ac.getSimpleName() + ".ID = %s";

        return String.format(SELECT,
                getFieldNames(man, entityClass, true),
                ac.getSimpleName(),
                key).toUpperCase();
    }

    public static String getSelectQuery(AnnotationManager man, long i, Class<?> main) {
        AnnotatedClass ac = man.getAnnotatedClass(main);
        AnnotatedField id = man.getId(main);
        String SELECT = "SELECT %s FROM %s WHERE %s = %s";

        state = State.SELECT;

        StringBuilder fields = new StringBuilder(getFieldNames(man, main, true));
        List<Class<?>> classes = ac.getFields(OneToOne.class).stream().map(AnnotatedField::getType).collect(Collectors.toList());

        for (Class<?> cl : classes) {
            fields.append(", ").append(getFieldNames(man, cl, true));
        }

        String join = " left outer join %s on %s = %s";

        StringBuilder joiner = new StringBuilder();
        for (AnnotatedField f : ac.getFields(OneToOne.class)) {
            AnnotatedClass foreign = man.getAnnotatedClass(f.getType());

            foreign.getFields(OneToOne.class).stream().filter(field -> field.getType().equals(main)).findFirst().ifPresent(foreignField ->
                    joiner.append(String.format(join,
                            foreign.getSimpleName(),
                            main.getSimpleName() + "." + id.getName(),
                            foreign.getSimpleName() + "." + foreignField.getName())));
        }

        return String.format(SELECT,
                fields,
                ac.getSimpleName() + joiner,
                ac.getSimpleName() + "." + id.getName(),
                i).toUpperCase();
    }

    public static String getSelectQuery(@NotNull final AnnotationManager man,
                                        @NotNull final Class<?> entityClass,
                                        @NotNull final AnnotatedField field, long id) {
        AnnotatedClass ac = man.getAnnotatedClass(entityClass);

        String SELECT = "SELECT %s FROM %s WHERE %s = %s";

        state = State.SELECT;

        StringBuilder fields = new StringBuilder(getFieldNames(man, entityClass, true));
        List<Class<?>> classes = ac.getFields(OneToOne.class).stream().map(AnnotatedField::getType).collect(Collectors.toList());

        for (Class<?> cl : classes) {
            fields.append(", ").append(getFieldNames(man, cl, true));
        }

        String join = " left outer join %s on %s = %s";

        StringBuilder joiner = new StringBuilder();
        for (AnnotatedField f : ac.getFields(OneToOne.class)) {
            AnnotatedClass foreign = man.getAnnotatedClass(f.getType());

            foreign.getFields(OneToOne.class).stream().filter(field1 -> field1.getType().equals(entityClass)).findFirst().ifPresent(foreignField ->
                    joiner.append(String.format(join,
                            foreign.getSimpleName(),
                            entityClass.getSimpleName() + "." + field.getName(),
                            foreign.getSimpleName() + "." + foreignField.getName())));
        }

        return String.format(SELECT,
                fields,
                ac.getSimpleName() + joiner,
                ac.getSimpleName() + "." + field.getName(),
                id).toUpperCase();
    }

    @NotNull
    private static String getFieldNames(@NotNull AnnotationManager manager,
                                        @NotNull Class<?> entityClass, boolean hasId) {

        StringBuilder query = new StringBuilder();
        AnnotatedClass cl = manager.getAnnotatedClass(entityClass);

        int count = 0;

        for (AnnotatedField f : cl.getFields()) {
            if (f.contains(OneToMany.class))
                continue;

            if (!hasId && f.contains(manager.getIdAnnotation()))
                continue;

            if (count++ != 0) {
                query.append(", ");
            }


            if (state == State.INSERT) {
                query.append(f.getName());
            }
            else if (state == State.CREATE) {
                query.append(getFieldString(manager, entityClass, f.getName()));
            } else {
                query.append(f.getFullName("."));
                if (state == State.SELECT)
                    query.append(" as ").append(f.getFullName("_"));
            }
        }
        return query.toString();
    }

    @NotNull
    private static String getFieldString(@NotNull AnnotationManager manager,
                                         @NotNull Class<?> entityClass,
                                         @NotNull String name) {

        AnnotatedField field = manager.getField(entityClass, name);
        StringBuilder sb = new StringBuilder(field.getName());

        String sqlType = sqlTypes.get(field.getType());

        if (field.contains(manager.getIdAnnotation())) {
            sb.append(" ").append(ID_FIELD).append(" ").append(AUTO_INCREMENT);
        } else {

            if (sqlType != null)
                sb.append(" ").append(sqlType);
            else if (manager.contains(entityClass)) {
                sb.append(" ").append(ID_FIELD);
            } else
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

/*
SELECT  USERDATASET.NAME AS USERDATASET_NAME,
        USERDATASET.AGE AS USERDATASET_AGE,
        USERDATASET.ADDRESS AS USERDATASET_ADDRESS,
        USERDATASET.ID AS USERDATASET_ID,
        ADDRESS.ADDRESS AS ADDRESS_ADDRESS,
        ADDRESS.USER AS ADDRESS_USER,
        ADDRESS.ID AS ADDRESS_ID
FROM USERDATASET LEFT OUTER JOIN ADDRESS ON USERDATASET.ID = ADDRESS.USER WHERE USERDATASET.ID = 1

 */