package ru.otus.jdbc;

import ru.otus.xml.PersistenceParams;

import java.util.Map;
import java.util.Set;

public final class JdbcTestParams {
    public static final String persistanceUnit = "otusJPAH2";
    public static final String persistancePath = "META-INF/persistence.xml";

    public static final PersistenceParams persistenceXml = new PersistenceParams(persistanceUnit, persistancePath);

    public static final Map<String, String> properties = persistenceXml.getConnectionData();
    public static final Set<String> classes = persistenceXml.getEntityClasses();


    public static final String DB_DRIVER_KEY = "javax.persistence.jdbc.driver";
    public static final String DB_URL_KEY = "javax.persistence.jdbc.url";

}
