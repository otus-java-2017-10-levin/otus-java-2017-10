package ru.otus;

import org.openjdk.jmh.annotations.*;
import ru.otus.sort.ParallelSorter;
import ru.otus.sort.SortManager;
import ru.otus.sort.SortType;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
/*
types of arrays:
    1. random
    2. few different
    3. sorted ascending
    4. sorted descending
 */

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 10, time = 1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(10)
public class MyBenchmark {

    @State(Scope.Thread)
    public static class Data {


//        @Param({"5000", "10000", "1000000"})
        @Param({"100", "1000", "2500", "5000", "7500", "10000", "250000", "500000"})
        int count;

        int[] arr;

        @Setup
        public void setup() {
            final int bound = 10_000_000;
            arr = new int[count];
            Random random = new Random();
            for (int i=0; i < count; i++) {
                arr[i] = random.nextInt(bound);
            }
        }
    }

    @Benchmark
    public int[] insertionSortSingle(Data d) {
        ParallelSorter insertionSorter = SortManager.getSorter(SortType.INSERTION);
        int[] c = Arrays.copyOf(d.arr, d.arr.length);
        insertionSorter.sort(c);
        return c;
    }

    @Benchmark
    public int[] shellSortSingle(Data d) {
        ParallelSorter shellSorter = SortManager.getSorter(SortType.SHELL);
        int[] c = Arrays.copyOf(d.arr, d.arr.length);
        shellSorter.sort(c);
        return c;
    }

    @Benchmark
    public int[] insertionSort4(Data d) {
        ParallelSorter insertionSorter = SortManager.getSorter(SortType.INSERTION);
        int[] c = Arrays.copyOf(d.arr, d.arr.length);
        insertionSorter.threads(4).sort(c);
        return c;
    }

    @Benchmark
    public int[] shellSort4(Data d) {
        ParallelSorter shellSorter = SortManager.getSorter(SortType.SHELL);
        int[] c = Arrays.copyOf(d.arr, d.arr.length);
        shellSorter.threads(4).sort(c);
        return c;
    }
}
