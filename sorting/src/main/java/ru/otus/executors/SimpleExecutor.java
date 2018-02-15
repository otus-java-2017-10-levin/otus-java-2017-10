package ru.otus.executors;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

final  class SimpleExecutor implements ExecutorService {

    private boolean isShutdown = false;
    private final List<Thread> workers = new ArrayList<>();

    @Override
    public void shutdown() {
        isShutdown = true;
    }

    @NotNull
    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        throw new UnsupportedOperationException();
    }

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return {@code true} if this executor terminated and
     *         {@code false} if the timeout elapsed before termination
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        long elapsed = System.nanoTime();
        while (!isAllThreadsTerminated()) {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();

            nanos -= System.nanoTime() - elapsed;

            if (nanos < 0L)
                return false;
        }
        return true;
    }

    private boolean isAllThreadsTerminated() {
        for (Thread worker : workers) {
            if (worker.getState() == Thread.State.TERMINATED)
                return false;
        }
        return true;
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Future<?> submit(@NotNull Runnable task) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks,
                                         long timeout,
                                         @NotNull TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks,
                           long timeout,
                           @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(@NotNull Runnable command) {
        if (isShutdown)
            throw new RejectedExecutionException();

        Thread thread = new Thread(command);
        thread.run();
        workers.add(thread);
    }
}
