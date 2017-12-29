package ru.otus.persistence;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.otus.annotations.AnnotatedClass;
import ru.otus.annotations.AnnotationField;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManagerFactory;
import ru.otus.xml.PersistenceParams;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

class MyEntityManager implements EntityManager {

    private final DBConnection connection;
    private final PersistenceParams params;
    private final List<AnnotatedClass> annotatedClass = new ArrayList<>();
    private boolean isOpen;
    private final Map<AnnotatedClass, Set<Object>> objects = new HashMap<>();

    MyEntityManager(Map params) {
        this.params = new PersistenceParams(params);
        connection = DbManagerFactory.createDataBaseManager(this.params.getConnectionData()).createConnection();
        try {
            loadEntityClasses();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        isOpen = true;
        dropTables();
        createTables();
    }

    private void loadEntityClasses() throws ClassNotFoundException {
        if (params == null || params.getEntityClasses().size() == 0)
            throw new IllegalStateException("There is no entity classes in persistence.xml");

        for (String className : params.getEntityClasses()) {
            if (className == null)
                throw new IllegalArgumentException("className is null");

            AnnotatedClass cl = AnnotatedClass.of(Class.forName(className));

            annotatedClass.add(cl);
        }
    }

    /**
     * Make an entity instance managed and persistent.
     *
     * @param entity
     * @throws EntityExistsException        if the entity already exists.
     *                                      (The EntityExistsException may be thrown when the persist
     *                                      operation is invoked, or the EntityExistsException or
     *                                      another PersistenceException may be thrown at flush or commit
     *                                      time.)
     * @throws IllegalStateException        if this EntityManager has been closed.
     * @throws IllegalArgumentException     if not an entity
     * @throws TransactionRequiredException if invoked on a
     *                                      container-managed entity manager of type
     *                                      PersistenceContextType.TRANSACTION and there is
     *                                      no transaction.
     */
    @Override
    public void persist(Object entity) {
        if (!isOpen)
            throw new IllegalStateException("EntityManager is closed");

        AnnotatedClass cl = checkEntity(entity);

        Set<Object> set = objects.containsKey(cl) ? objects.get(cl) : new HashSet<>();

        if (set.contains(entity))
            throw new EntityExistsException();

        set.add(entity);
        objects.put(cl, set);
    }

    private AnnotatedClass checkEntity(Object entity) {
        for (AnnotatedClass cl : annotatedClass) {
            if (cl.is(entity)) {
                return cl;
            }
        }
        throw new IllegalArgumentException("is not an entity");
    }

    /**
     * Merge the state of the given entity into the
     * current persistence context.
     *
     * @param entity
     * @return the instance that the state was merged to
     * @throws IllegalStateException        if this EntityManager has been closed.
     * @throws IllegalArgumentException     if instance is not an
     *                                      entity or is a removed entity
     * @throws TransactionRequiredException if invoked on a
     *                                      container-managed entity manager of type
     *                                      PersistenceContextType.TRANSACTION and there is
     *                                      no transaction.
     */
    @Override
    public <T> T merge(T entity) {
        return null;
    }

    /**
     * Remove the entity instance.
     *
     * @param entity
     * @throws IllegalStateException        if this EntityManager has been closed.
     * @throws IllegalArgumentException     if not an entity
     *                                      or if a detached entity
     * @throws TransactionRequiredException if invoked on a
     *                                      container-managed entity manager of type
     *                                      PersistenceContextType.TRANSACTION and there is
     *                                      no transaction.
     */
    @Override
    public void remove(Object entity) {

    }

    /**
     * Find by primary key.
     *
     * @param entityClass
     * @param primaryKey
     * @return the found entity instance or null
     * if the entity does not exist
     * @throws IllegalStateException    if this EntityManager has been closed.
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second
     *                                  argument is not a valid type for that
     *                                  entity's primary key
     */
    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        if (!isOpen)
            throw new IllegalStateException();

        final AnnotatedClass cl = AnnotatedClass.of(entityClass);

        Class<?> cl1 = cl.getId().getType();
        if (cl1.isPrimitive())
            cl1 = ClassUtils.primitiveToWrapper(cl1);
        Class<?> cl2 = primaryKey.getClass();
        if (!annotatedClass.contains(cl) ||
                !cl1.equals(cl2)) {
            throw new IllegalArgumentException();
        }

        long id = -1;
        try {
            id = ((Long) primaryKey);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        T cls = null;
        try {
            cls = connection.execQuery(QueryFactory.getSelectQuery(cl, id), result -> {

                ObjectBuilder<T> builder = new ObjectBuilder<T>(entityClass);
                ResultSetMetaData rsmd = result.getMetaData();
                result.next();

                int count = rsmd.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String name = rsmd.getColumnName(i);
                    builder.set(cl.getField(name).getName(), result.getObject(i));
                }
                return builder.build();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * Get an instance, whose state may be lazily fetched.
     * If the requested instance does not exist in the database,
     * throws {@link EntityNotFoundException} when the instance state is
     * first accessed. (The persistence provider runtime is permitted to throw
     * {@link EntityNotFoundException} when {@link #getReference} is called.)
     * <p>
     * The application should not expect that the instance state will
     * be available upon detachment, unless it was accessed by the
     * application while the entity manager was open.
     *
     * @param entityClass
     * @param primaryKey
     * @return the found entity instance
     * @throws IllegalStateException    if this EntityManager has been closed.
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second
     *                                  argument is not a valid type for that
     *                                  entity's primary key
     * @throws EntityNotFoundException  if the entity state
     *                                  cannot be accessed
     */
    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return null;
    }

    /**
     * Synchronize the persistence context to the
     * underlying database.
     *
     * @throws IllegalStateException        if this EntityManager has been closed.
     * @throws TransactionRequiredException if there is
     *                                      no transaction
     * @throws PersistenceException         if the flush fails
     */
    @Override
    public void flush() {
        if (!isOpen)
            throw new IllegalStateException();

        writeObjects();
    }

    /**
     * Set the flush mode that applies to all objects contained
     * in the persistence context.
     *
     * @param flushMode
     * @throws IllegalStateException if this EntityManager has been closed.
     */
    @Override
    public void setFlushMode(FlushModeType flushMode) {

    }

    /**
     * Get the flush mode that applies to all objects contained
     * in the persistence context.
     *
     * @return flush mode
     * @throws IllegalStateException if this EntityManager has been closed.
     */
    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    /**
     * Set the lock mode for an entity object contained
     * in the persistence context.
     *
     * @param entity
     * @param lockMode
     * @throws IllegalStateException        if this EntityManager has been closed.
     * @throws PersistenceException         if an unsupported lock call
     *                                      is made
     * @throws IllegalArgumentException     if the instance is not
     *                                      an entity or is a detached entity
     * @throws TransactionRequiredException if there is no
     *                                      transaction
     */
    @Override
    public void lock(Object entity, LockModeType lockMode) {
        throw new UnsupportedOperationException();

    }

    /**
     * Refresh the state of the instance from the database,
     * overwriting changes made to the entity, if any.
     *
     * @param entity
     * @throws IllegalStateException        if this EntityManager has been closed.
     * @throws IllegalArgumentException     if not an entity
     *                                      or entity is not managed
     * @throws TransactionRequiredException if invoked on a
     *                                      container-managed entity manager of type
     *                                      PersistenceContextType.TRANSACTION and there is
     *                                      no transaction.
     * @throws EntityNotFoundException      if the entity no longer
     *                                      exists in the database.
     */
    @Override
    public void refresh(Object entity) {

    }

    /**
     * Clear the persistence context, causing all managed
     * entities to become detached. Changes made to entities that
     * have not been flushed to the database will not be
     * persisted.
     *
     * @throws IllegalStateException if this EntityManager has been closed.
     */
    @Override
    public void clear() {

    }

    /**
     * Check if the instance belongs to the current persistence
     * context.
     *
     * @param entity
     * @return <code>true</code> if the instance belongs to
     * the current persistence context.
     * @throws IllegalStateException    if this EntityManager has been closed.
     * @throws IllegalArgumentException if not an entity
     */
    @Override
    public boolean contains(Object entity) {
        return false;
    }

    /**
     * Create an instance of Query for executing a
     * Java Persistence query language statement.
     *
     * @param qlString a Java Persistence query language query string
     * @return the new query instance
     * @throws IllegalStateException    if this EntityManager has been closed.
     * @throws IllegalArgumentException if query string is not valid
     */
    @Override
    public Query createQuery(String qlString) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of Query for executing a
     * named query (in the Java Persistence query language or in native SQL).
     *
     * @param name the phone of a query defined in metadata
     * @return the new query instance
     * @throws IllegalStateException    if this EntityManager has been closed.
     * @throws IllegalArgumentException if a query has not been
     *                                  defined with the given phone
     */
    @Override
    public Query createNamedQuery(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of Query for executing
     * a native SQL statement, e.g., for update or delete.
     *
     * @param sqlString a native SQL query string
     * @return the new query instance
     * @throws IllegalStateException if this EntityManager has been closed.
     */
    @Override
    public Query createNativeQuery(String sqlString) {
        return null;
    }

    /**
     * Create an instance of Query for executing
     * a native SQL query.
     *
     * @param sqlString   a native SQL query string
     * @param resultClass the class of the resulting instance(s)
     * @return the new query instance
     * @throws IllegalStateException if this EntityManager has been closed.
     */
    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return null;
    }

    /**
     * Create an instance of Query for executing
     * a native SQL query.
     *
     * @param sqlString        a native SQL query string
     * @param resultSetMapping the phone of the result set mapping
     * @return the new query instance
     * @throws IllegalStateException if this EntityManager has been closed.
     */
    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return null;
    }

    /**
     * Indicate to the EntityManager that a JTA transaction is
     * active. This method should be called on a JTA application
     * managed EntityManager that was created outside the scope
     * of the active transaction to associate it with the current
     * JTA transaction.
     *
     * @throws IllegalStateException        if this EntityManager has been closed.
     * @throws TransactionRequiredException if there is
     *                                      no transaction.
     */
    @Override
    public void joinTransaction() {

    }

    /**
     * Return the underlying provider object for the EntityManager,
     * if available. The result of this method is implementation
     * specific.
     *
     * @throws IllegalStateException if this EntityManager has been closed.
     */
    @Override
    public Object getDelegate() {
        return null;
    }

    /**
     * Close an application-managed EntityManager.
     * After the close method has been invoked, all methods
     * on the EntityManager instance and any Query objects obtained
     * from it will throw the IllegalStateException except
     * for getTransaction and isOpen (which will return false).
     * If this method is called when the EntityManager is
     * associated with an active transaction, the persistence
     * context remains managed until the transaction completes.
     *
     * @throws IllegalStateException if the EntityManager
     *                               is container-managed or has been already closed..
     */
    @Override
    public void close() {
        if (!isOpen)
            throw new IllegalStateException();

        writeObjects();

        isOpen = false;
    }

    private void writeObjects() {
        saveObjects();
    }

    private void saveObjects() {
        for (Map.Entry<AnnotatedClass, Set<Object>> entry : objects.entrySet()) {
            saveObject(entry.getKey(), entry.getValue());
        }
    }

    private void saveObject(AnnotatedClass annotatedClass, Set<Object> objects) {
        String query = QueryFactory.getInsertQuery(annotatedClass);
        connection.execQuery(query, statement -> {
            for (Object object : objects) {
                int count = 1;
                for (AnnotationField field : annotatedClass.getFields()) {
                    try {
                        if (field.isPrimaryKey())
                            statement.setNull(count++, Types.BIGINT);
                        else
                            statement.setString(count++, FieldUtils.readField(field.getField(), object, true).toString());

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                statement.execute();
                long id = connection.getLastInsertedId();
                saveObjectId(annotatedClass, object, id);
            }
        });
    }

    private void saveObjectId(AnnotatedClass annotatedClass, Object object, long id) {

        Class<?> cl = annotatedClass.getAnnotatedClass();
        try {
            Method m = cl.getMethod("setId", long.class);
            try {
                m.invoke(object, id);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void dropTables() {
        for (AnnotatedClass cl : annotatedClass) {
            String query = QueryFactory.getDropTableQuery(cl);
            System.out.println(query);
            connection.execQuery(query);
        }
    }

    private void createTables() {
        for (AnnotatedClass cl : annotatedClass) {
            String query = QueryFactory.createTableQuery(cl);
            System.out.println(query);
            connection.execQuery(query);
        }
    }

    /**
     * Determine whether the EntityManager is open.
     *
     * @return true until the EntityManager has been closed.
     */
    @Override
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Returns the resource-level transaction object.
     * The EntityTransaction instance may be used serially to
     * begin and commit multiple transactions.
     *
     * @return EntityTransaction instance
     * @throws IllegalStateException if invoked on a JTA
     *                               EntityManager.
     */
    @Override
    public EntityTransaction getTransaction() {
        throw new UnsupportedOperationException();
    }
}