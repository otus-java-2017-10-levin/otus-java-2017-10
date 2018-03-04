package ru.orus.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.messages.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

class MessageWorkerImpl extends AbstractMessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(MessageWorkerImpl.class);
    private static final int WORKERS_COUNT = 2;
    private static final long TIMEOUT = 2L;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;

    MessageWorkerImpl(Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    @Override
    public void onNext(Subscription subscription, Message item) {
        subscription.request(1);
        output.add(item);
    }

    @Override
    public void onComplete() {
        log.info("onComplete(). Close socket");
        close();
    }

    @Override
    public void onError(Throwable throwable) {
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
                Message msg = output.take();
                out.writeObject(msg);
                log.debug("message sent: " + msg);
            }
        } catch (InterruptedException | SocketException e) {
            log.info("Closing socket.(cause:" + e.getCause() + ")");
            onError(e);
        } catch (IOException e) {
            onError(e);
        }
    }

    private void receiveMessage() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            while (socket.isConnected()) {
                Message mes = (Message) (in.readObject());
                submit(mes);
                log.debug("message received: " + mes);
            }
        } catch (SocketException e) {
            log.info("Closing socket.(cause:" + e.getCause() + ")");
            onError(e);
        } catch (IOException | ClassNotFoundException e) {
            onError(e);
        }
    }
}