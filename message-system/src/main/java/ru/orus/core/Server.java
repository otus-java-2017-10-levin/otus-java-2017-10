package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.messages.Header;
import ru.orus.messages.Message;
import ru.orus.messages.Messages;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@SuppressWarnings("SameParameterValue")
public final class Server extends AbstractMessageProcessor {
    private final static Logger log = LoggerFactory.getLogger(Server.class);

    private final ExecutorService executor;
    private final int port;
    private final List<AbstractMessageProcessor> clients = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
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
            client.subscribe(Messages.getSystemChannelName(),this);
            this.subscribe(Messages.getSystemChannelName(), client);
            clients.add(client);
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
    public void onError(@NotNull Throwable throwable) {
        log.error("", throwable);
    }

    @Override
    public void onNext(@NotNull Subscription subscription, @NotNull Message item) {
        if (Message.Type.SYSTEM == item.getHeader().getType()) {
            final String id = item.getId();
            final Header header = new Messages.BasicProperties.Builder()
                    .setType(Message.Type.SYSTEM)
                    .topic(Messages.getSystemChannelName())
                    .id(id).build();

            submit(Messages.newMessage("", header));
            subscription.request(1);
        } else {
            super.onNext(subscription, item);
        }
    }

    @Override
    protected void onReceiveCommandMessage(@NotNull Command message) {
        message.execute(this);
    }

    /**
     * Round robin message delivery.
     * @param item
     * @return
     */
    @Override
    public boolean submit(@NotNull Message<?> item) {
        if (Message.Type.STRING != item.getHeader().getType()) {
            return super.submit(item);
        } else {
            return roundRobinSubmit(item);
        }
    }

    private boolean roundRobinSubmit(Message<?> item) {
        final String topic = item.getHeader().getTopic();

        final TopicSubscription subscriptions = subscribers.get(topic);
        final Optional<AbstractMessageSubscription> next = subscriptions.next();
        if (next.isPresent()) {
            next.get().submit(item);
            return true;
        } else {
            return false;
        }
    }
}