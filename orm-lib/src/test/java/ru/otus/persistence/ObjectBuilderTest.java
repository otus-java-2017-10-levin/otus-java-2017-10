package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.UserDataSet;

import static org.junit.jupiter.api.Assertions.*;

class ObjectBuilderTest {

    @Test
    void addFields() {
        UserDataSet res  = new ObjectBuilder<>(UserDataSet.class)
                .set("id", 1L)
                .set("name", "Flow")
                .build();

        assertEquals(1L, res.getId());
        assertEquals("Flow", res.getName());
    }
}