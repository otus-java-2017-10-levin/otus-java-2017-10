package ru.orus.core;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public final class WorkerFactory {
    private WorkerFactory() {}

    @NotNull
    public static AbstractMessageProcessor getWorker(@NotNull Socket socket) {
        final MessageWorker worker = new MessageWorker(socket);
        worker.init();
        return worker;
    }
}
