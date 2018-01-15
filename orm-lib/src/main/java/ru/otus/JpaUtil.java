package ru.otus;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {
//    private static final String PERSISTENCE_UNIT_NAME = "HibernateMysql";
//    private static final String PERSISTENCE_UNIT_NAME = "mysql";
    private static final String PERSISTENCE_UNIT_NAME = "otusJPAH2";
//    private static final String PERSISTENCE_UNIT_NAME = "otusHibernate";
    private static EntityManagerFactory factory;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory;
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}
