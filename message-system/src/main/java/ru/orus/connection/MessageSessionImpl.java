package ru.orus.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.core.AbstractMessageProcessor;
import ru.orus.core.Subscription;
import ru.orus.messages.Header;
import ru.orus.messages.Message;
import ru.orus.messages.Messages;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

final class MessageSessionImpl extends AbstractMessageProcessor implements MessageSession {
    private final static Logger log = LoggerFactory.getLogger(MessageSessionImpl.class);
    private String topicName;
    private final Map<String, Consumer<Message>> filters = new ConcurrentHashMap<>();
    private boolean isClosed;

    MessageSessionImpl() {
        isClosed = false;
    }

    @Override
    public void declareTopic(String topicName) {
        if (this.topicName != null)
            throw new IllegalStateException("queue name is already set");

        this.topicName = topicName;
    }

    @Override
    public void sendMessage(String message) {
        final Header build = new Messages.BasicProperties.
                Builder().topic(topicName).build();
        submit(Messages.newMessage(message, build));
    }

    @Override
    public void sendMessage(String message, Header props) {
        submit(Messages.newMessage(message, props));
    }

    @Override
    public void addFilter(String topic, Consumer<Message> consumer) {
        filters.put(topic, consumer);
        log.info("add filter: " + filters);
    }

    @Override
    public String getTopic() {
        return topicName;
    }

    @Override
    public void close() {
        subscriptions.forEach(Subscription::cancel);
        subscriptions.clear();
        filters.clear();
        isClosed = true;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }


    @Override
    public void onNext(Subscription subscription, Message item) {
        subscription.request(1);
        applyFilters(item);
    }

    private void applyFilters(Message mes) {
        final String topic = mes.getHeader().getTopic();
        log.info("filters: " + filters);
        if (filters.containsKey(topic)) {
            filters.get(topic).accept(mes);
        }
    }

    @Override
    public void onComplete() {
        log.info("onComplete(). Close session");
        close();
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("", throwable);
        close();
    }
}
