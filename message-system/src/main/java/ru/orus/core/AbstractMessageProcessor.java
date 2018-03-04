package ru.orus.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.messages.Message;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMessageProcessor extends AbstractPublisher<Message> implements Processor<Message, Message> {
    private static final Logger log = LoggerFactory.getLogger(AbstractMessageProcessor.class);
    protected Set<Subscription> subscriptions;

    @Override
    public void onNext(Subscription subscription, Message item) {
        log.debug("mess received: " + item);
        subscription.request(1);
        submit(item);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        if (subscriptions == null)
            subscriptions = new HashSet<>();

        subscriptions.add(subscription);
        subscription.request(1);
    }

}
