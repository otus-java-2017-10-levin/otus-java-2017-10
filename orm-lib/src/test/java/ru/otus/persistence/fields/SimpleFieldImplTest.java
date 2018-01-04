package ru.otus.persistence.fields;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleFieldImplTest {

    @Test
    void simple() {
        Field<String> field = new SimpleFieldImpl<>("name", "Flow", String.class);

        assertEquals("name", field.getName());
        assertEquals("Flow", field.getValue());
        assertEquals(String.class, field.getFieldClass());
    }
}