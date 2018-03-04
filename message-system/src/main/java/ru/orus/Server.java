package ru.orus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.core.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("SameParameterValue")
final class Server extends AbstractMessageProcessor {
    private final static Logger log = LoggerFactory.getLogger(Server.class);

    private final ExecutorService executor;
    private final int port;
    private final List<AbstractMessageProcessor> clients = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;

    Server(int port) throws IOException {
        int THREADS_NUM = 6;
        executor = Executors.newFixedThreadPool(THREADS_NUM);
        serverSocket = new ServerSocket(port);
        this.port = port;
    }

    public void start() {
        log.info("Started server on " + port + "..");
        executor.submit(this::loop);
    }

    private void loop() {
        while (!executor.isShutdown()) {
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                log.error("", e);
                return;
            }
            log.info("Open connection for new client");
            AbstractMessageProcessor client = WorkerFactory.getWorker(socket);
            client.subscribe(this);
            this.subscribe(client);
            clients.add(client);
            new Thread(((Runnable) client)).start();
        }
    }

    @SuppressWarnings("unused")
    public void stop() throws IOException {
        log.info("Shutting down server..");
        ExecutorHelper.shutdownAndAwaitTermination(executor, 2L);
        clients.forEach(Subscriber::onComplete);
        serverSocket.close();
        log.info("done");
    }


    @Override
    public void onComplete() {
        log.info("onComplete. Close sockets");
        clients.forEach(Subscriber::onComplete);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("", throwable);
    }
}