package ru.otus.dao;

import ru.otus.base.UserDataSet;

import javax.persistence.EntityManager;

public class UserDataSetDAO extends GenericDAO<UserDataSet> {

    public UserDataSetDAO(EntityManager manager) {
        super(UserDataSet.class, manager);
    }

    @Override
    public void save(UserDataSet user) {
        manager.persist(user);
        manager.persist(user.getAddress());
        manager.flush();
    }
}