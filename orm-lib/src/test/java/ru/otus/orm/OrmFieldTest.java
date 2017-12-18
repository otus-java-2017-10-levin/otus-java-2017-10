package ru.otus.orm;

import org.junit.jupiter.api.Test;
import ru.otus.base.UsersDataSet;

import static org.junit.jupiter.api.Assertions.*;

class OrmFieldTest {

    private UsersDataSet usersDataSet = new UsersDataSet(1, "Flow");

    @Test
    void createTest() {
//        OrmField ormField = new OrmField<>("USER_ID", Long.class, usersDataSet::getId);
//        assertEquals("USER_ID", ormField.getName());
//        assertEquals(true, ormField.getType().equals(Long.class));
//        assertEquals(true, ormField.getMethod().apply().equals(1L));
    }

    @Test
    void compareTest() {
//        OrmField ormField = new OrmField<>("USER_ID", Long.class, usersDataSet::getId);
//        OrmField ormField1 = new OrmField<>("USER_NAME", String.class, usersDataSet::getName);
//        OrmField ormField2 = new OrmField<>("USER_ID", Long.class, usersDataSet::getId);
//
//        assertEquals(false, ormField.equals(ormField1));
//        assertEquals(true, ormField.equals(ormField2));
    }
}