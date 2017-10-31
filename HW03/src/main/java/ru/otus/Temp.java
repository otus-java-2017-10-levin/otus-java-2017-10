package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Temp {


    public static void main(String[] args) {
        List<Integer> ints = new MyArrayList<>();
//        List<Integer> ints = new ArrayList<>();


        System.out.println(Integer.MAX_VALUE);
        int max = 100;
        for (int i = 0; i < max; i++) {
            ints.add(i+1);
        }

        ListIterator<Integer> iter = ints.listIterator(100);

        while (iter.hasPrevious()) {
            System.out.print(String.format("NEXT INDEX:\t%-10d", iter.previousIndex()));
            int val = iter.previous();
            System.out.print(String.format("NEXT:\t%-10d\n", val));
        }
        System.out.println(iter.previousIndex());

        System.out.println(ints.toString());
        System.out.println(String.format("\nSIZE:\t%-10d", ints.size()));

    }
}
