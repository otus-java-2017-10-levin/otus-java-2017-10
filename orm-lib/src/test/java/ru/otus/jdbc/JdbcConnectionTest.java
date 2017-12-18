package ru.otus.jdbc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcConnectionTest {

    private final String create = "create table if not exists user (id bigint auto_increment, name varchar(256), primary key (id))";
    private final DBConnection connection = DbManagerFactory.createDataBaseManager(new HashMap<>()).createConnection();
    @Test
    void executeQueryWithoutAnswer() {
        assertEquals(true, connection.execQuery(create));
    }

    @Test
    void createQueryWithWrongSyntax() {
        assertThrows(IllegalArgumentException.class, ()-> connection.execQuery("select"));
    }


}