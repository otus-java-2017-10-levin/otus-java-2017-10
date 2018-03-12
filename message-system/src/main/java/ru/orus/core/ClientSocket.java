package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.messages.Header;
import ru.orus.messages.Message;
import ru.orus.messages.Messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

class ClientSocket extends AbstractMessageSubject implements MessageWorker {
    private static final Logger log = LoggerFactory.getLogger(ClientSocket.class);
    private static final int WORKERS_COUNT = 2;
    private static final long AWAIT_TIME = 2L;

    private final BlockingQueue<Message<?>> output = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;

    ClientSocket(@NotNull Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        init();
    }

    @Override
    public void close() {
        output.clear();
        observers.clear();
        ExecutorHelper.shutdownAndAwaitTermination(executor, AWAIT_TIME);
    }

    @Override
    public void send(@NotNull Message<?> msg) {
        output.add(msg);
    }

    @Override
    public synchronized void subscribe(@NotNull String topic, @NotNull MessageObserver o) {
        super.subscribe(topic, o);
        final Header header = new Messages.BasicProperties.Builder()
                .topic(topic)
                .setType(Message.Type.SYSTEM)
                .addAttribute("action", "subscribe")
                .build();

        send(Messages.newMessage("", header));
    }

    @Override
    public synchronized void unsubscribe(@NotNull String topic, @NotNull MessageObserver o) {
        super.unsubscribe(topic, o);
        final Header header = new Messages.BasicProperties.Builder()
                .topic(topic)
                .setType(Message.Type.SYSTEM)
                .addAttribute("action", "unsubscribe")
                .build();

        send(Messages.newMessage("", header));
    }

    private void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    private void sendMessage() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            while (socket.isConnected()) {
                trySend(out);
            }
        } catch (InterruptedException | SocketException e) {
            log.info("Closing socket.(cause:" + e.getCause() + ")");
            onError(e);
        } catch (IOException e) {
            onError(e);
        }
    }

    private void trySend(ObjectOutputStream out) throws InterruptedException, IOException {
        Message msg = output.take();
        if (msg != null) {
            log.info("sending message: " + msg);
            out.writeObject(msg);
        }
    }

    private void receiveMessage() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            while (socket.isConnected()) {
                Message msg = (Message) (in.readObject());
                if (Message.Type.STRING == msg.getHeader().getType())
                    log.info("receive message: " + msg);
                else
                    log.debug("receive message: " + msg);
                notifyObservers(msg);
            }
        } catch (SocketException e) {
            log.info("Closing socket.(cause:" + e.getCause() + ")");
            onError(e);
        } catch (IOException | ClassNotFoundException e) {
            onError(e);
        }
    }

    private void onError(Exception e) {
        log.error("", e);
        close();
    }
}
