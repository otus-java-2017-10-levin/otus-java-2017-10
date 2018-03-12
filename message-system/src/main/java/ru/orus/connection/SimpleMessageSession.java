package ru.orus.connection;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.common.Validation;
import ru.orus.core.MessageObserver;
import ru.orus.core.MessageWorker;
import ru.orus.messages.Header;
import ru.orus.messages.Message;
import ru.orus.messages.Messages;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

class SimpleMessageSession implements MessageSession, MessageObserver {
    private static final Logger log = LoggerFactory.getLogger(SimpleMessageSession.class);
    private BlockingQueue<Message<?>> queue;
    private final Map<String, Consumer<Message<?>>> filters = new HashMap<>();
    private final ExecutorService service = Executors.newFixedThreadPool(1);
    private final MessageWorker worker;
    private String topic;
    private boolean isClosed;

    SimpleMessageSession(MessageWorker worker) {
        this.worker = worker;
        isClosed = false;
        queue = new LinkedBlockingQueue<>();

        service.execute(this::loop);
    }

    private void loop() {
        while (!isClosed) {
            try {
                final Message<?> msg = queue.take();
                final Consumer<Message<?>> consumer = filters.getOrDefault(msg.getHeader().getTopic(), message -> {});
                consumer.accept(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void declareTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void sendMessage(String message) {
        final Header header = new Messages.BasicProperties.Builder().topic(getTopic()).build();
        sendMessage(message, header);
    }

    @Override
    public void sendMessage(String message, Header props) {
        worker.send(Messages.newMessage(message, props));
    }


    @Override
    public void addFilter(String topic, Consumer<Message<?>> consumer) {
        Validation.validateNonNull("", topic, consumer);
        filters.put(topic, consumer);
    }

    @Override
    public void subscribe(String topic) {
        log.debug("subscribe for topic: " + topic);
        worker.subscribe(topic, this);
    }

    @Override
    public void unsubscribe(String topic) {
        worker.unsubscribe(topic, this);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void close() {
        isClosed = true;
        ExecutorHelper.shutdownAndAwaitTermination(service, 2L);
        queue.clear();
        queue = null;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void update(@NotNull Message<?> msg) {
        queue.add(msg);
    }
}
