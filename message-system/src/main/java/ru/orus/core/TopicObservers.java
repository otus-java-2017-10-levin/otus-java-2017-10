package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import ru.orus.common.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

class TopicObservers<T> {
    private final String topic;
    private final List<T> subscribers;
    private int current = -1;

    TopicObservers(@NotNull String topic) {
        this.topic = topic;
        subscribers = new ArrayList<>();
    }

    void subscribe(@NotNull T observer) {
        Validation.validateNonNull("", observer);
        subscribers.add(observer);
    }

    void unsubscribe(@NotNull T observer) {
        Validation.validateNonNull("", observer);
        subscribers.remove(observer);
    }

    @NotNull
    Optional<T> next() {
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

    void forEach(@NotNull BiConsumer<String, T> consumer) {
        subscribers.forEach(subscriber -> consumer.accept(topic, subscriber));
    }
}