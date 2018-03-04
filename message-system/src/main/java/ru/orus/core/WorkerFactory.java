package ru.orus.core;

import java.net.Socket;

public class WorkerFactory {

    public static AbstractMessageProcessor getWorker(Socket socket) {
        final MessageWorkerImpl worker = new MessageWorkerImpl(socket);
        worker.init();
        return worker;
    }
}
