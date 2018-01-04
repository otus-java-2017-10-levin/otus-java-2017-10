package ru.otus.persistence.fields;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void isPrimitiveArrayField() {
        assertEquals(true, Field.isArrayField(boolean[].class));
        assertEquals(true, Field.isArrayField(byte[].class));
        assertEquals(true, Field.isArrayField(char[].class));
        assertEquals(true, Field.isArrayField(short[].class));
        assertEquals(true, Field.isArrayField(int[].class));
        assertEquals(true, Field.isArrayField(float[].class));
        assertEquals(true, Field.isArrayField(long[].class));
        assertEquals(true, Field.isArrayField(double[].class));
    }

    @Test
    void isWrapperArrayField() {

        assertEquals(true, Field.isArrayField(Boolean[].class));
        assertEquals(true, Field.isArrayField(Byte[].class));
        assertEquals(true, Field.isArrayField(Character[].class));
        assertEquals(true, Field.isArrayField(Short[].class));
        assertEquals(true, Field.isArrayField(Integer[].class));
        assertEquals(true, Field.isArrayField(Float[].class));
        assertEquals(true, Field.isArrayField(Long[].class));
        assertEquals(true, Field.isArrayField(Double[].class));
    }

    @Test
    void isCollectionArrayField() {
        assertEquals(true, Field.isArrayField(Set.class));
        assertEquals(true, Field.isArrayField(List.class));
        assertEquals(true, Field.isArrayField(Queue.class));
        assertEquals(true, Field.isArrayField(SortedSet.class));
    }

    @Test
    void isPrimitiveSimpleField() {
        assertEquals(true, Field.isSimpleField(boolean.class));
        assertEquals(true, Field.isSimpleField(byte.class));
        assertEquals(true, Field.isSimpleField(char.class));
        assertEquals(true, Field.isSimpleField(short.class));
        assertEquals(true, Field.isSimpleField(int.class));
        assertEquals(true, Field.isSimpleField(float.class));
        assertEquals(true, Field.isSimpleField(long.class));
        assertEquals(true, Field.isSimpleField(double.class));
    }

    @Test
    void isWrapperSimpleField() {
        assertEquals(true, Field.isSimpleField(Boolean.class));
        assertEquals(true, Field.isSimpleField(Byte.class));
        assertEquals(true, Field.isSimpleField(Character.class));
        assertEquals(true, Field.isSimpleField(Short.class));
        assertEquals(true, Field.isSimpleField(Integer.class));
        assertEquals(true, Field.isSimpleField(Float.class));
        assertEquals(true, Field.isSimpleField(Long.class));
        assertEquals(true, Field.isSimpleField(Double.class));
    }

    @Test
    void isSimpleField() {
        assertEquals(true, Field.isSimpleField(String.class));
        assertEquals(true, Field.isSimpleField(java.util.Date.class));
        assertEquals(true, Field.isSimpleField(java.sql.Date.class));
        assertEquals(true, Field.isSimpleField(BigDecimal.class));
        assertEquals(true, Field.isSimpleField(BigInteger.class));
    }


}