package ru.otus.controller.dao;

import javax.persistence.EntityManager;

abstract class GenericDAO<T> {
    private final Class<T> daoClass;
    final EntityManager manager;

    GenericDAO(Class<T> daoClass, EntityManager manager) {
        this.daoClass = daoClass;
        this.manager = manager;
    }


    public <R> T load(R id) {
        return manager.find(daoClass, id);
    }
}
