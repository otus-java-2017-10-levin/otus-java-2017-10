package ru.otus;


import org.openjdk.jmh.annotations.Benchmark;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    @Benchmark
    public int measureName() {
        int a = 10;
        int b = -11;
        return a+b;
    }
}
