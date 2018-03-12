package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


class SocketMessageSubject implements MessageObserver {
    private static final Logger log = LoggerFactory.getLogger(ClientSocket.class);
    private static final int WORKERS_COUNT = 2;
    private static final long AWAIT_TIME = 2L;

    private final BlockingQueue<Message<?>> output = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;
    private final MessageWorker subject;

    SocketMessageSubject(@NotNull Socket socket, MessageWorker subject) {
        this.socket = socket;
        this.subject = subject;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        init();
    }

    private void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    @Override
    public void close() {
        output.clear();
        ExecutorHelper.shutdownAndAwaitTermination(executor, AWAIT_TIME);
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
                log.info("receive message: " + msg);
                handleMessage(msg);
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
    }

    private void handleMessage(Message<?> msg) {
        if (Message.Type.SYSTEM == msg.getHeader().getType()) {
            final Optional<String> action = msg.getHeader().getAttribute("action");
            if (action.isPresent()) {
                final String topic = msg.getHeader().getTopic();
                final String a = action.get();
                if ("subscribe".equals(a)) {
                    subject.subscribe(topic, this);
                    return;
                }
                if ("unsubscribe".equals(a)) {
                    subject.unsubscribe(topic, this);
                }
            }
        } else
            subject.send(msg);
    }

    @Override
    public void update(@NotNull Message<?> msg) {
        output.add(msg);
    }
}
