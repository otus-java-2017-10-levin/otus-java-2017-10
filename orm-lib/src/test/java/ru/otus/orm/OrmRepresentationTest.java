package ru.otus.orm;

import org.junit.jupiter.api.Test;
import ru.otus.base.UsersDataSet;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class OrmRepresentationTest {
//
//    private UsersDataSet usersDataSet = new UsersDataSet(1, "Flow");
//    private HashSet<OrmField> ormFields = new HashSet<>();
//
//    {
//        ormFields.add(new OrmField<>("USER_ID", Long.class, usersDataSet::getId));
//        ormFields.add(new OrmField<>("USER_NAME", String.class, usersDataSet::getName));
//    }
//
//    @Test
//    void createTest() {
//        OrmRepresentation ormRepresentation = new OrmRepresentation(UsersDataSet.class);
//        for (OrmField field : ormFields) {
//            ormRepresentation.add(field);
//        }
//
//        assertEquals(UsersDataSet.class, ormRepresentation.getEntityClass());
//        Set<OrmField> fields = ormRepresentation.getFields();
//        assertEquals(true, fields.equals(ormFields));
//    }
//
//    @Test
//    void createTestWithWrongParam() {
//        Exception e = assertThrows(IllegalArgumentException.class, () -> new OrmRepresentation(null));
//        assertEquals("entity class is null", e.getMessage());
//
//        OrmRepresentation ormRepresentation = new OrmRepresentation(UsersDataSet.class);
//        ormRepresentation.add(new OrmField<>("USER_ID", Long.class, usersDataSet::getId));
//
//        e = assertThrows(IllegalArgumentException.class, () ->
//                ormRepresentation.add(new OrmField<>("USER_ID", Long.class, usersDataSet::getId)));
//        assertEquals("field already exist", e.getMessage());
//    }

}