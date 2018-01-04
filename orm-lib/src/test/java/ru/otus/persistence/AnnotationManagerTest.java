package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.UserDataSet;
import ru.otus.classes.AddressDataSet;
import ru.otus.classes.EmployeeDataSet;
import ru.otus.classes.MultipleIdDataSet;
import ru.otus.classes.PhoneDataSet;

import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationManagerTest {

    private final AnnotationManager man = new AnnotationManager(Id.class, UserDataSet.class);

    @Test
    void getId() {
        assertEquals("id", man.getId(UserDataSet.class).getName());
        assertEquals(long.class, man.getId(UserDataSet.class).getType());
    }

    @Test
    void getIdAnnotation() {
        assertEquals(Id.class, man.getIdAnnotation());
    }

    @Test
    void validateClasses() {
        new AnnotationManager(Id.class, EmployeeDataSet.class, PhoneDataSet.class, AddressDataSet.class);
    }
}