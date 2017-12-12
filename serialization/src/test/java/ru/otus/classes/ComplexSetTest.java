package ru.otus.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 *  Copyright by Flow on 12.12.2017.
 
    Description here
 */
class ComplexSetTest {

    @Test
    void isEqual() {
        ComplexSet obj1 = new ComplexSet();
        ComplexSet obj2 = new ComplexSet();

        assertEquals(true, obj1.equals(obj2));
        obj1.init();
        assertEquals(false, obj1.equals(obj2));

        obj2.init();
        assertEquals(true, obj1.equals(obj2));
    }

}