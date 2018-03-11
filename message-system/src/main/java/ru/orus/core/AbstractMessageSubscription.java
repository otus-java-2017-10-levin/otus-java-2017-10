package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.Validation;
import ru.orus.messages.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

abstract class AbstractMessageSubscription implements Subscription {
    private static final int QUEUE_MAX_SIZE = 256;

    private final static Logger log = LoggerFactory.getLogger(AbstractMessageSubscription.class);
    private final Subscriber<? super Message<?>> subscriber;
    private boolean isCancel = false;
    private AtomicLong demand = new AtomicLong(0);
    private BlockingQueue<Message<?>> queue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);
    private ExecutorService service = Executors.newFixedThreadPool(2);


    AbstractMessageSubscription(Subscriber<? super Message<?>> subscriber) {
        Validation.validateNonNull("", subscriber);
        this.subscriber = subscriber;
    }

    public Subscriber<? super  Message<?>> getSubscriber() {
        return subscriber;
    }

    @Override
    public void cancel() {
        isCancel = true;
    }

    public synchronized void submit(@NotNull Message<?> mess) {
        if (isCancel)
            throw new IllegalStateException();

        Validation.validateNonNull("mess is null", mess);
        queue.add(mess);

        trySend();
    }

    @Override
    public void request(long n) {
        if (n <= 0L)
            throw new IllegalArgumentException();

        demand.addAndGet(n);
        service.execute(this::trySend);
    }

    private void trySend() {
        while (demand.get() > 0 && queue.size() > 0) {
            final Message<?> poll = queue.poll();
            if (poll != null) {
                log.debug("sending: " + subscriber);
                service.execute(() -> subscriber.onNext(this, poll));
            }
            demand.decrementAndGet();
        }
    }
}

