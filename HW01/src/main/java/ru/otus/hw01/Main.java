package ru.otus.hw01;

import com.google.common.math.BigIntegerMath;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

/**
 * Code homework for the first lecture (14.10.2017).
 *
 * Using BigIntegerMath.log2 func from com.google.common.math package.
 * Measuring time for 10^6-1 BifIntegerMath.log2 calls and printing it.
 * Using Java 8 and lambdas.
 */

public class Main
{
    private static final int MEASURE_COUNT = 1;

    public static void main( String[] args )
    {
        List<BigInteger> ints = new ArrayList<>();

        for (int i = 1; i < 1_000_000; i++) {
            ints.add(BigInteger.valueOf(i));
        }

        calcTime(() ->
                ints.forEach((item) ->
                    {
                        item = BigInteger.valueOf(BigIntegerMath.log2(item, RoundingMode.FLOOR));
                    })
        );
    }

    private static void calcTime(Runnable run) {
        long startTime =  System.nanoTime();
        for (int i = 0; i < MEASURE_COUNT; i++) {
            run.run();
        }

        long finishTime = System.nanoTime();

        long timeNs = (finishTime - startTime) / MEASURE_COUNT;
        System.out.println("Time spent: " + timeNs + "ns ( " + timeNs / 1_000_000 + "ms)");
    }
}
