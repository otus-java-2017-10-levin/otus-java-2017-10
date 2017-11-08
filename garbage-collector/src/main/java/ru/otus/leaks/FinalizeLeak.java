package ru.otus.leaks;

class FinalizeLeak implements Leak{
        private class AbstractObject {

        @Override
        protected void finalize() throws Throwable {
            int size = 50000;
            Integer[] a = new Integer[size];
        }
    }

    @Override
    public void execute(int n) throws OutOfMemoryError {
        for (int i = 0; i < n; i++) {
            new AbstractObject();
        }
    }
}
