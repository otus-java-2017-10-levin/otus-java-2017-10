package ru.otus.leaks;

import java.util.logging.Level;
import java.util.logging.Logger;

class FinalizeLeak implements Leak{

    private final int SIZE = 1000;

    private class AbstractObject {

        @Override
        protected void finalize() throws Throwable {
            Integer[] a = new Integer[SIZE];
        }
    }

    @Override
    public void execute(int n) {
        for (int i = 0; i < n; i++) {
            new AbstractObject();
        }
    }
}
