package ru.otus.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyEntityManagerFactory implements EntityManagerFactory {


    private List<EntityManager> entities = new ArrayList<>();

    /**
     * Create a new EntityManager.
     * This method returns a new EntityManager instance each time
     * it is invoked.
     * The isOpen method will return true on the returned instance.
     */
    @Override
    public EntityManager createEntityManager() {
        return createEntityManager(null);
    }

    /**
     * Create a new EntityManager with the specified Map of
     * properties.
     * This method returns a new EntityManager instance each time
     * it is invoked.
     * The isOpen method will return true on the returned instance.
     *
     * @param map
     */
    @Override
    public EntityManager createEntityManager(Map map) {
        return new MyEntityManager();
    }

    /**
     * Close the factory, releasing any resources that it holds.
     * After a factory instance is closed, all methods invoked on
     * it will throw an IllegalStateException, except for isOpen,
     * which will return false. Once an EntityManagerFactory has
     * been closed, all its entity managers are considered to be
     * in the closed state.
     */
    @Override
    public void close() {

    }

    /**
     * Indicates whether or not this factory is open. Returns true
     * until a call to close has been made.
     */
    @Override
    public boolean isOpen() {
        return false;
    }
}
