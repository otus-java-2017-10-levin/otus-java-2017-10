package ru.orus.core;
/*
 *  @author Flow
 */

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.Validation;
import ru.orus.messages.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@SuppressWarnings("WeakerAccess")
abstract class AbstractMessagePublisher implements Publisher<Message<?>>, Producer<Message<?>> {
    private final static Logger log = LoggerFactory.getLogger(AbstractMessagePublisher.class);
    protected final Map<String, TopicSubscription> subscribers = new ConcurrentHashMap<>();

    @Override
    public void subscribe(@NotNull String topic, @NotNull Subscriber<? super Message<?>> subscriber) {
        Validation.validateNonNull("", topic, subscriber);

        if (isSubscribed(topic, subscriber)) {
            log.debug(subscriber + " already subscribed for " + topic);
            return;
        }

        TopicSubscription t = subscribers.containsKey(topic) ?
                subscribers.get(topic) : new TopicSubscription(topic);

        final MessageSubscription subscription = new MessageSubscription(subscriber);
        t.addSubscription(subscription);
        subscribers.put(topic, t);
        subscriber.onSubscribe(topic, subscription);
    }

    private boolean isSubscribed(@NotNull String topic, @NotNull Subscriber<? super Message<?>> subscriber) {
        final TopicSubscription topicSubscription = subscribers.get(topic);
        return topicSubscription != null && topicSubscription.contains(subscriber);
    }

    @Override
    public boolean submit(@NotNull Message<?> item) {
        log.debug("submitting: " + item);
        final TopicSubscription topicSubscription = subscribers.get(item.getHeader().getTopic());
        if (topicSubscription != null) {
            topicSubscription.forEach((s, messageSubscription) -> messageSubscription.submit(item));
        }
        return topicSubscription != null && topicSubscription.size() != 0;
    }

    protected class TopicSubscription {
        private final String topic;
        private final List<MessageSubscription> subscribers;
        private int current = -1;

        TopicSubscription(@NotNull String topic) {
            this.topic = topic;
            subscribers = new ArrayList<>();
        }

        void addSubscription(@NotNull MessageSubscription subscriber) {
            Validation.validateNonNull("", subscriber);
            subscribers.add(subscriber);
        }

        Optional<AbstractMessageSubscription> next() {
            final int size = subscribers.size();
            if (size > 0) {
                current++;
                if (current >= size) {
                    current = 0;
                }
                return Optional.of(subscribers.get(current));
            }
            return Optional.empty();
        }

        int size() {
            return subscribers.size();
        }

        void forEach(@NotNull BiConsumer<String, MessageSubscription> consumer) {
            subscribers.forEach(subscriber -> {
                log.debug(topic + " -> " + subscriber);
                consumer.accept(topic, subscriber);});

        }

        boolean contains(Subscriber<? super Message<?>> subscriber) {
            for (MessageSubscription subscription : subscribers) {
                if (subscription.getSubscriber().equals(subscriber))
                    return true;
            }
            return false;
        }
    }
}
