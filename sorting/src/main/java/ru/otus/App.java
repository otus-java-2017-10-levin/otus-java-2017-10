package ru.otus;

import ru.otus.sort.SortManager;

public class App
{
    public static void main( String[] args )
    {
        int[] arr = {5,4,3,2,1,0};
        new ParallelSorterImpl(SortManager.getSorter(SortManager.SortType.INSERTION)).sort(arr);
        printArray(arr);
    }

    private static <T> void printArray(T[] arr) {
        for (T t: arr) {
            System.out.print(t + " ");
        }
        System.out.println();
    }

    private static void printArray(int[] arr) {
        for (int t: arr) {
            System.out.print(t + " ");
        }
        System.out.println();
    }
}