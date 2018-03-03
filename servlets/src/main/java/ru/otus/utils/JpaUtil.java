package ru.otus.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

    private final static String name = "otusJPAH2";
    private static EntityManagerFactory factory;

    public static EntityManagerFactory getFactory() {
        if (factory == null)
            factory = Persistence.createEntityManagerFactory(name);

        return factory;
    }
}