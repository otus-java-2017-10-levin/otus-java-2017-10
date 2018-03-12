package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer extends AbstractMessageSubject implements MessageWorker {
    private final static Logger log = LoggerFactory.getLogger(SimpleServer.class);

    private final ExecutorService executor;
    private final int port;
    private final List<MessageObserver> clients = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;

    public SimpleServer(int port) throws IOException {
        this.port = port;
        executor = Executors.newFixedThreadPool(2);
        serverSocket = new ServerSocket(port);
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
            final MessageObserver client = new SocketMessageSubject(socket, this);
            clients.add(client);
        }
    }


    @SuppressWarnings("WeakerAccess")
    public void stop() throws IOException {
        log.info("Shutting down server..");
        ExecutorHelper.shutdownAndAwaitTermination(executor, 2L);
        serverSocket.close();
        clients.forEach(MessageObserver::close);
        clients.clear();
        log.info("done");
    }

    @Override
    public void send(@NotNull Message<?> msg) {
        log.debug("server sends " + msg);
        notifyObservers(msg);
    }

    @Override
    public void close() {
        try {
            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void notifyObservers(@NotNull Message<?> msg) {
        final String topic = msg.getHeader().getTopic();
        final TopicObservers<MessageObserver> set = observers.get(topic);
        if (set == null)
            return;
        log.debug("notify next observer");
        final Optional<MessageObserver> next = set.next();

        next.ifPresent(messageObserver -> messageObserver.update(msg));
    }
}
