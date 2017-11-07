package ru.otus.leaks;

public interface Leak {

    /**
     * Execute method that leaks memory n times
     * @param n - execute n times
     */
    void execute(int n);
}
