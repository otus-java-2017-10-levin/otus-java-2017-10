package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.messages.Message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMessageProcessor extends AbstractMessagePublisher implements Processor<Message<?>, Message<?>> {
    private static final Logger log = LoggerFactory.getLogger(AbstractMessageProcessor.class);
    protected Map<String, Subscription> subscriptions;

    abstract protected void onReceiveCommandMessage(@NotNull Command message);

    @Override
    public void onNext(@NotNull Subscription subscription,
                       @NotNull Message<?> item) {
        log.debug("mess received: " + item);
        if (item.getHeader().getType() == Message.Type.COMMAND) {
            final Object body = item.getBody();
            if (Command.class.isAssignableFrom(body.getClass())) {
                onReceiveCommandMessage((Command) body);
            } else throw new ClassCastException();
        } else {
            submit(item);
        }
        subscription.request(1);
    }

    @Override
    public void onSubscribe(@NotNull String topic, @NotNull Subscription subscription) {
        if (subscriptions == null)
            subscriptions = new HashMap<>();

        log.info("Adding subscription: " + subscription);
        subscriptions.put(topic, subscription);
        subscription.request(1);
    }
}
