package ru.otus.classes;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 *  Copyright by Flow on 12.12.2017.
 
    Description here
 */
class PlainTest {

    @Test
    void isEqual() {
        Plain obj1 = new Plain();
        Plain obj2 = new Plain();

        assertEquals(true, obj1.equals(obj2));
        obj1.init();
        assertEquals(false, obj1.equals(obj2));

        obj2.init();
        assertEquals(true, obj1.equals(obj2));
    }
}