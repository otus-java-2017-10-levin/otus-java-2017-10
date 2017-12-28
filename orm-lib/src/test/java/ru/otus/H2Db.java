package ru.otus;

import org.h2.tools.Server;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class H2Db {

    public static void start() throws SQLException {

        Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
    }
}
