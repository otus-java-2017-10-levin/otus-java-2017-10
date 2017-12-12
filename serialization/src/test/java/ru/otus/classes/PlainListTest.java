package ru.otus.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 *  Copyright by Flow on 12.12.2017.
 
    Description here
 */
class PlainListTest {

    @Test
    void isEquals() {
        PlainList obj1 = new PlainList();
        PlainList obj2 = new PlainList();

        assertEquals(true, obj1.equals(obj2));
        obj1.init();
        assertEquals(false, obj1.equals(obj2));

        obj2.init();
        assertEquals(true, obj1.equals(obj2));
    }
}