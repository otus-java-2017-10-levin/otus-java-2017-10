package ru.otus;

import org.openjdk.jmh.annotations.*;
import ru.otus.sort.SortManager;
import ru.otus.sort.SortStrategy;
import ru.otus.sort.SortType;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 10, time = 1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(5)
@State(Scope.Thread)
public class MyBenchmark {

    @Benchmark
    public int[] qsortRandom(RandomData d) {
        SortStrategy sort = SortManager.getSorter(SortType.QSORT);
        int[] c = Arrays.copyOf(d.arr, d.arr.length);
        sort.sort(c);
        return c;
    }

    @Benchmark
    public int[] mergeRandom(RandomData d) {
        SortStrategy sort = SortManager.getSorter(SortType.MERGE);
        int[] c = Arrays.copyOf(d.arr, d.arr.length);
        sort.sort(c);
        return c;
    }

//    @Benchmark
//    public int[] qsortNearlySorted(NearlySortedData d) {
//        SortStrategy sort = SortManager.getSorter(SortType.QSORT);
//        int[] c = Arrays.copyOf(d.arr, d.arr.length);
//        sort.sort(c);
//        return c;
//    }
//
//    @Benchmark
//    public int[] qsortReversed(ReversedData d) {
//        SortStrategy sort = SortManager.getSorter(SortType.QSORT);
//        int[] c = Arrays.copyOf(d.arr, d.arr.length);
//        sort.sort(c);
//        return c;
//    }
//
//    @Benchmark
//    public int[] qsortFewUnique(FewUniqueData d) {
//        SortStrategy sort = SortManager.getSorter(SortType.QSORT);
//        int[] c = Arrays.copyOf(d.arr, d.arr.length);
//        sort.sort(c);
//        return c;
//    }

    @State(Scope.Thread)
    public static class RandomData {
        @Param({"1000"})
        int count;
        int[] arr;

        @Setup
        public void setup() {
            final int bound = 10_000_000;
            arr = new int[count];
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                arr[i] = random.nextInt(bound);
            }
        }
    }

    @State(Scope.Thread)
    public static class NearlySortedData {
        @Param({"1000"})
        int count;
        @Param("5")
        int delta;
        int[] arr;

        @Setup
        public void setup() {
            arr = new int[count];
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                int shift = random.nextInt(delta) - delta / 2;
                arr[i] = i + shift;
            }
        }
    }

    @State(Scope.Thread)
    public static class ReversedData {
        @Param({"1000"})
        int count;
        int[] arr;

        @Setup
        public void setup() {
            arr = new int[count];
            for (int i = count-1; i > 0; i--) {
                arr[i] = i;
            }
        }
    }

    @State(Scope.Thread)
    public static class FewUniqueData {
        @Param({"5"})
        int uniqueNum;

        @Param({"1000"})
        int count;
        int[] arr;



        @Setup
        public void setup() {
            arr = new int[count];
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                arr[i] = random.nextInt(uniqueNum);
            }
        }
    }
}
