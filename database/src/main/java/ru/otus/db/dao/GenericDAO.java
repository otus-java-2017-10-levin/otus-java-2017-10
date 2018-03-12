package ru.otus.db.dao;

import org.hibernate.Session;
import ru.otus.db.entities.DataSet;

public class GenericDAO<T> {
    private final Class<T> daoClass;
    private final Session manager;

    private GenericDAO(Class<T> daoClass, Session manager) {
        this.daoClass = daoClass;
        this.manager = manager;
    }

    public static <T extends DataSet> GenericDAO<T> of(Class<T> daoClass, Session manager) {
        if (daoClass == null || manager == null)
            throw new IllegalArgumentException();

        return new GenericDAO<>(daoClass, manager);
    }

    public <R> T load(R id) {
        return manager.find(daoClass, id);
    }

    public void save(T object) {
        manager.save(object);
    }
}
