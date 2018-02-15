package ru.otus;

import java.util.Random;

public class App {

    public static void main(String[] args) {
//        int[] arr = {5, 4, 3, 2, 1, 0};
//        SortManager.getSorter(SortType.INSERTION).threads(3).sort(arr);
//        printArray(arr);
    }

    private static <T> void printArray(T[] arr) {
        for (T t : arr) {
            System.out.print(t + " ");
        }
        System.out.println();
    }

    private static void printArray(int[] arr) {
        for (int t : arr) {
            System.out.print(t + " ");
        }
        System.out.println();
    }
}