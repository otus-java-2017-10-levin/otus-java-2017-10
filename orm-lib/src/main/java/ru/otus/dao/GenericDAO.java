package ru.otus.dao;

import javax.persistence.EntityManager;

public abstract class GenericDAO<T> {
    protected Class<T> daoClass;
    protected EntityManager manager;

    protected GenericDAO(Class<T> daoClass, EntityManager manager) {
        this.daoClass = daoClass;
        this.manager = manager;
    }

    public void save(T user) {
        manager.persist(user);
        manager.flush();
    }

    public <R> T load(R id) {
        return manager.find(daoClass, id);
    }
}
