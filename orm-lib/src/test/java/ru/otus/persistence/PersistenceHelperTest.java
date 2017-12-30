package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.UsersDataSet;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceHelperTest {

    @Test
    void getMethodFromFieldName() {
        String expected = "getId";

        assertEquals(expected, PersistenceHelper.setterFromField(UsersDataSet.class, "id", true));
    }

    @Test
    void getMethodFromFieldNameErrors() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> PersistenceHelper.setterFromField(null, "a", true));
        assertEquals(null, e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> PersistenceHelper.setterFromField(UsersDataSet.class, null, true));
        assertEquals(null, e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> PersistenceHelper.setterFromField(UsersDataSet.class, "a", true));
        assertEquals("No setter for field a", e.getMessage());
    }

    @Test
    void getMethodFromFieldNameWithoutCheck() {
        String expected = "getId";

        assertEquals(expected, PersistenceHelper.setterFromField(UsersDataSet.class, "id", false));
    }

    @Test
    void getMethodFromFieldNameErrorsWithoutCheck() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> PersistenceHelper.setterFromField(null, "a", false));
        assertEquals(null, e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> PersistenceHelper.setterFromField(UsersDataSet.class, null, false));
        assertEquals(null, e.getMessage());
    }


//    private <T> T loadObject(AnnotatedClass annotatedClass, long id, T object) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        T obj = (T)object.getClass().getConstructor().newInstance();
//
//        for (Field field: annotatedClass.getFields()) {
//            String methodName = PersistenceHelper.setterFromField()
//        }
//
//        return obj;
//    }
}