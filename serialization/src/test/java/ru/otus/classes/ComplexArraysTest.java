package ru.otus.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ComplexArraysTest {

    @Test
    void isEqual() {
        ComplexArrays array = new ComplexArrays();
        ComplexArrays array1 = new ComplexArrays();

        assertEquals(true, array.equals(array1));
        array.init();
        assertEquals(false, array.equals(array1));

        array1.init();
        assertEquals(true, array.equals(array1));
    }
}