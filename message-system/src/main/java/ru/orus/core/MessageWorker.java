package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.core.handlers.MessageHandler;
import ru.orus.core.handlers.MessageHandlerFactory;
import ru.orus.messages.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

class MessageWorker extends AbstractMessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(MessageWorker.class);
    private static final int WORKERS_COUNT = 2;
    private static final long TIMEOUT = 2L;

    private final BlockingQueue<Message<?>> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message<?>> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;
    private final MessageHandler handler = MessageHandlerFactory.getHandler();

    MessageWorker(@NotNull Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }


    @Override
    public void onNext(@NotNull Subscription subscription, @NotNull Message item) {
        log.debug("mess received: " + item);
        if (item.getHeader().getType() == Message.Type.COMMAND) {
            final Object body = item.getBody();
            if (Command.class.isAssignableFrom(body.getClass())) {
                onReceiveCommandMessage((Command) body);
            } else throw new ClassCastException();
        } else {
            output.add(item);
        }
        subscription.request(1);
    }

    @Override
    public void onComplete() {
        log.info("onComplete(). Close socket");
        close();
    }

    @Override
    public void onError(@NotNull Throwable throwable) {
        log.error("", throwable);
        close();
    }

    private void close() {
        log.info("Shutting down worker..");
        try {
            socket.close();
        } catch (IOException e) {
            log.error("", e);
        }
        ExecutorHelper.shutdownAndAwaitTermination(executor, TIMEOUT);
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
            if (msg != null && Message.Type.COMMAND != msg.getHeader().getType()) {
                log.debug("sending message: " + msg);
                out.writeObject(msg);
            }
        }
    }

    private void receiveMessage() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            while (socket.isConnected()) {
                Message mes = (Message) (in.readObject());
                log.debug("message received: " + mes);
                submit(handler.handleMessage(mes, this));
            }
        } catch (SocketException e) {
            log.info("Closing socket.(cause:" + e.getCause() + ")");
            onError(e);
        } catch (IOException | ClassNotFoundException e) {
            onError(e);
        }
    }

    @Override
    protected void onReceiveCommandMessage(@NotNull Command message) {
        message.execute(this);
    }
}