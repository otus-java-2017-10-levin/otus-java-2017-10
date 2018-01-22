package ru.otus.dao;

import ru.otus.base.Phone;

import javax.persistence.EntityManager;

public class PhoneDAO extends GenericDAO<Phone> {
    public PhoneDAO(EntityManager manager) {
        super(Phone.class, manager);
    }
}
