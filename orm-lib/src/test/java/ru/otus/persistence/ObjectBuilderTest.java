package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.UserDataSet;
import ru.otus.persistence.annotations.AnnotatedClass;

import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.*;

class ObjectBuilderTest {

    @Test
    void addFields() {
        AnnotationManager man = new AnnotationManager(Id.class, UserDataSet.class);
        AnnotatedClass annotatedClass = man.getAnnotatedClass(UserDataSet.class);

        UserDataSet res  = new ObjectBuilder<>(UserDataSet.class)
                .set(annotatedClass.getField("id"), 1L)
                .set(annotatedClass.getField("name"), "Flow")
                .build();

        assertEquals(1L, res.getId());
        assertEquals("Flow", res.getName());
    }
}