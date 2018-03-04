package ru.orus.connection;

import ru.orus.core.WorkerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public final class ConnectionFactory {
    private MessageConnection connection;
    private volatile boolean isConnected;

    public MessageConnection connect(String uri) throws IOException {
        if (!isConnected) {
            synchronized (this) {
                if (!isConnected) {
                    final int PORT = 5050;
                    Socket socket = new Socket(uri, PORT);
                    this.connection = new MessageConnectionImpl(WorkerFactory.getWorker(socket));
                    isConnected = true;
                }
            }
        }
        return connection;
    }
}
