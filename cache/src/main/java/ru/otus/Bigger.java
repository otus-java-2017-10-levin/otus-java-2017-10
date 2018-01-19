package ru.otus;

public class Bigger {
    private byte[] arr;

    public Bigger(int size) {
        if (size <= 1)
            throw new IllegalArgumentException();
        this.arr = new byte[size];
    }
}
