package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.messages.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class AbstractMessageSubject implements MessageSubject {
    private static final Logger log = LoggerFactory.getLogger(AbstractMessageSubject.class);

    final Map<String, TopicObservers<MessageObserver>> observers = new ConcurrentHashMap<>();

    @Override
    public synchronized void subscribe(@NotNull String topic, @NotNull MessageObserver o) {
        final TopicObservers<MessageObserver> set = observers.getOrDefault(topic, new TopicObservers<>(topic));
        set.subscribe(o);
        log.debug("subscribe " + topic +" for " + o);
        observers.put(topic, set);
    }

    @Override
    public synchronized void unsubscribe(@NotNull String topic, @NotNull MessageObserver o) {
        final TopicObservers<MessageObserver> topicObservers = observers.getOrDefault(topic, null);
        if (topicObservers == null)
            return;
        topicObservers.unsubscribe(o);
        log.debug("unsubscribe " + topic + " for " + o);
    }

    @Override
    public synchronized void notifyObservers(@NotNull Message<?> msg) {
        final String topic = msg.getHeader().getTopic();
        final TopicObservers<MessageObserver> set = observers.get(topic);
        if (set == null)
            return;
        log.debug("notify observers");
        set.forEach((s, messageObserver) -> messageObserver.update(msg));
    }

    @Override
    public void close() {
        observers.clear();
    }
}
