package ru.orus.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.Validation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

class MessageSubscription<T> implements Subscription {
    private static final int QUEUE_MAX_SIZE = 256;

    private final static Logger log = LoggerFactory.getLogger(MessageSubscription.class);
    private final Subscriber<? super T> subscriber;
    private boolean isCancel = false;
    private AtomicLong demand = new AtomicLong(0);
    private BlockingQueue<T> queue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    MessageSubscription(Subscriber<? super T> subscriber) {
        Validation.validateNonNull("", subscriber);
        this.subscriber = subscriber;
    }

    @Override
    public void cancel() {
        isCancel = true;
    }

    public synchronized void submit(T mess) {
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
        trySend();
    }

    private void trySend() {
        while (demand.get() > 0 && queue.size() > 0) {
            final T poll = queue.poll();
            if (poll != null)
                subscriber.onNext(this, poll);
            demand.decrementAndGet();
        }
    }
}

