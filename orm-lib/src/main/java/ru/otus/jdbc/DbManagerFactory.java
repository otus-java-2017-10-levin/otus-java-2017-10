package ru.otus.jdbc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DbManagerFactory {

    private static final String DRIVER_NAME = "javax.persistence.jdbc.driver";

    public static DbManager createDataBaseManager(Map<String, String> props) throws IllegalArgumentException {
        if (props == null || props.size() == 0)
            throw new IllegalArgumentException("Cannot create database manager with empty map");

        String name = props.get(DRIVER_NAME);
        try {
            Class.forName(name);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Driver "+ name + " is not supported");
        }

        return new JdbcManager(props);
    }
}
