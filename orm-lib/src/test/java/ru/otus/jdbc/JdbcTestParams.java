package ru.otus.jdbc;

import ru.otus.persistence.xml.PersistenceParams;

import java.util.Map;

public final class JdbcTestParams {
    private static final String persistenceUnit = "otusJPAH2";
    private static final String persistencePath = "persistenceTest.xml";

    public static final PersistenceParams persistenceXml = new PersistenceParams(persistenceUnit, persistencePath);

    public static final Map<String, String> properties = persistenceXml.getConnectionData();

    public static final String DB_DRIVER_KEY = "javax.persistence.jdbc.driver";
}
