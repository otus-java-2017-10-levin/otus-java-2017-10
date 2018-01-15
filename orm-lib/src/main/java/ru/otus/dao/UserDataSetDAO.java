package ru.otus.dao;

import ru.otus.base.Phone;
import ru.otus.base.UserDataSet;

import javax.persistence.EntityManager;

public class UserDataSetDAO extends GenericDAO<UserDataSet> {

    public UserDataSetDAO(EntityManager manager) {
        super(UserDataSet.class, manager);
    }

    public void save(UserDataSet user) {
        manager.getTransaction().begin();
        manager.persist(user.getAddress());
        manager.persist(user);
        for (Phone ph : user.getPhones()) {
            manager.persist(ph);
        }
        manager.getTransaction().commit();
    }
}