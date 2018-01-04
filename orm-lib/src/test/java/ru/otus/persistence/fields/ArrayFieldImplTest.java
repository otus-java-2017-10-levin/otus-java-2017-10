package ru.otus.persistence.fields;

import org.junit.jupiter.api.Test;
import ru.otus.persistence.fields.ArrayField;
import ru.otus.persistence.fields.ArrayFieldImpl;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ArrayFieldImplTest {

    private Integer[] arr = new Integer[]{0,1,2,3,4,5};
    private ArrayField<Integer> intField = new ArrayFieldImpl<>("name", arr);

    @Test
    void getArray() {
        assertArrayEquals(arr, intField.getArray());
    }

    @Test
    void getName() {
        assertEquals("name", intField.getName());
    }

    @Test
    void getFieldClass() {
        assertEquals(Integer[].class, intField.getFieldClass());
    }
}