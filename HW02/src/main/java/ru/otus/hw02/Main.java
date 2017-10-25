package ru.otus.hw02;

import java.util.ArrayList;
import java.util.List;


public class Main
{

    public static void main( String[] args )
    {
        Main main = new Main();
        main.run(args);
    }

    public void run(String[] args) {

        measure(new String(new char[0]));
        measure(new String(""));
        measure(new int[10]);


        List<Object> list = new ArrayList<>();
        measure(list);

        for (int i=0; i < 10; i++) {
            list.add(i);
        }
        measure(list);

        for (int i=0; i < 5; i++) {
            list.add(i);
        }
        measure(list);
    }

    private void measure(Object obj) {
        long size = Measurer.getObjectSize(obj);
        System.out.println("Class: "+obj.getClass().getCanonicalName()+". Memory usage: " + size + " bytes (" + size / (1024*1024f) + " Mb).");
    }
}
