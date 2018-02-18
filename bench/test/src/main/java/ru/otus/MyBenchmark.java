package ru.otus;

import org.openjdk.jmh.annotations.*;
import ru.otus.sort.Generator;
import ru.otus.sort.SortManager;
import ru.otus.sort.SortStrategy;
import ru.otus.sort.SortType;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(5)
@State(Scope.Thread)
public class MyBenchmark {

    @Param({"20"})
    private int THRESHOLD;

    @Benchmark
    public Integer[] parallelQSort(RandomData d) {
        SortStrategy sort = SortManager.getSorter(SortType.PARALLEL_QSORT, THRESHOLD);
        Integer[] c = Arrays.copyOf(d.arr, d.arr.length);
        sort.sort(c);
        return c;
    }

    @Benchmark
    public Integer[] parallelMerge(RandomData d) {
        SortStrategy sort = SortManager.getSorter(SortType.PARALLEL_MERGE, THRESHOLD);
        Integer[] c = Arrays.copyOf(d.arr, d.arr.length);
        sort.sort(c);
        return c;
    }

    @Benchmark
    public Integer[] quickSort(RandomData d) {
        SortStrategy sort = SortManager.getSorter(SortType.QSORT, THRESHOLD);
        Integer[] c = Arrays.copyOf(d.arr, d.arr.length);
        sort.sort(c);
        return c;
    }

    @Benchmark
    public Integer[] mergeSort(RandomData d) {
        SortStrategy sort = SortManager.getSorter(SortType.MERGE, THRESHOLD);
        Integer[] c = Arrays.copyOf(d.arr, d.arr.length);
        sort.sort(c);
        return c;
    }

    @State(Scope.Thread)
    public static class RandomData {
        @Param({"1000", "10000"})
        int count;
        Integer[] arr;
        @Param({"random"})
        private String method;

        @Setup
        public void setup() {
            switch (method) {
                case "random":
                    arr = Generator.generateRandom(count, count / 2);
                    return;
                case "few uniques":
                    arr = Generator.generateRandom(count, 10);
                    return;
                case "reversed":
                    arr = Generator.generateReversed(count);
                    return;
                case "nearly sorted":
                    arr = Generator.generateNearlySorted(count, 5);
            }
        }
    }
}
