package ru.otus.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Measures {

    private static final int SIZE = 10_000;

    private int[] arr;

    @BeforeEach
    void setup() {
        arr = Generator.generateArray(SIZE);
    }

    @Test
    void measureInsert() {
        SortStrategy insert = new InsertionSort();
        long t1 = System.nanoTime();
        insert.sort(arr);
        System.out.println(System.nanoTime() - t1);
    }

    @Test
    void measureShell() {
        SortStrategy insert = new ShellSort();
        long t1 = System.nanoTime();
        insert.sort(arr);
        System.out.println(System.nanoTime() - t1);
    }
}
