package ru.otus.utils;

import ru.orus.connection.ConnectionFactory;
import ru.orus.connection.MessageConnection;

import java.io.IOException;

public class MSUtil {

    private static final String HOST = "localhost";
    private volatile static MessageConnection localhost;

    public static synchronized MessageConnection getConnection() throws IOException {
        if (localhost == null)
            localhost = new ConnectionFactory().connect(HOST);
        return localhost;
    }
}
