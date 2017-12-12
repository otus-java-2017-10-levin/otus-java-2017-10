package ru.otus.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 *  Copyright by Flow on 12.12.2017.
 
    Description here
 */
class PlainWrappersTest {

    @Test
    void isEqual() {
        PlainWrappers obj1 = new PlainWrappers();
        PlainWrappers obj2 = new PlainWrappers();

        assertEquals(true, obj1.equals(obj2));
        obj1.init();
        assertEquals(false, obj1.equals(obj2));

        obj2.init();
        assertEquals(true, obj1.equals(obj2));

    }
}