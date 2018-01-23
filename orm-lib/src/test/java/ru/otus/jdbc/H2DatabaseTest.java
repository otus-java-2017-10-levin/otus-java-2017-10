package ru.otus.jdbc;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.junit.Ignore;

@Ignore
public abstract class H2DatabaseTest extends DBTestCase {

    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String SCHEMA = "";

    public H2DatabaseTest(String name) {
        super(name);
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, JDBC_DRIVER );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, JDBC_URL );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, USER );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, PASSWORD );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, SCHEMA );
    }
}