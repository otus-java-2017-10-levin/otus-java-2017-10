package ru.otus.sort;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class SortUtilsTest {

}