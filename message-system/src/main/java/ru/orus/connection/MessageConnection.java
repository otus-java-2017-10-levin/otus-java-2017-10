package ru.orus.connection;

@SuppressWarnings("unused")
public interface MessageConnection {

    MessageSession getSession();

    void close();
}
