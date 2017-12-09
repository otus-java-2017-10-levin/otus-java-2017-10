package ru.otus.hw02;

import ru.otus.hw02.memory.MeasureObject;
import ru.otus.hw02.memory.Measurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Main
{
    public static void main( String[] args )
    {
        Main main = new Main();
        main.run(args);
    }

    public void run(String[] args) {

////        measure(() -> new String(new char[0]));
//        measure(() -> new String(new char[0]));
//
        measure(() -> new String(""));
//        measure(() -> new int[10]);
//
//
//        List<Object> list = new ArrayList<>();
//        measure(() -> {
//            return new ArrayList<>(list);
//        });
//      16
//      40+16
//      8

//        Collections.addAll(list, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        measure(() -> {
            List<Object> l = new ArrayList<Object>();
            Collections.addAll(l, new Object(), new Object(), new Object(), new Object(), new Object(), new Object(), new Object(), new Object(), new Object());
            return l;
        });

//        Collections.addAll(list, 0, 1, 2, 3, 4);
//        measure(() -> new ArrayList<>(list));
    }

    private void measure(MeasureObject obj) {
        long size = Measurer.getObjectSize(obj, Measurer.MeasureType.INSTRUMENTATION);
        System.out.println("Instrumentation result:");
        System.out.println("Class: "+obj.createObject().getClass().getCanonicalName()+". Memory usage: " + size + " bytes (" + size / (1024*1024f) + " Mb).");

        size = Measurer.getObjectSize(obj, Measurer.MeasureType.GARBAGE_COLLECTOR);
        System.out.println("GC result:");
        System.out.println("Class: "+obj.createObject().getClass().getCanonicalName()+". Memory usage: " + size + " bytes (" + size / (1024*1024f) + " Mb).");
        System.out.println("---");
    }
}
