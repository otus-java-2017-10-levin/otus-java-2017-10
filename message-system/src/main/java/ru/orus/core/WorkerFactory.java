package ru.orus.core;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public class WorkerFactory {

    public static MessageWorker getWorker(@NotNull Socket socket) {
        return new ClientSocket(socket);
    }
}
