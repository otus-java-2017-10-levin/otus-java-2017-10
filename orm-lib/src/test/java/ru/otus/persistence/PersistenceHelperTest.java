package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.UserDataSet;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceHelperTest {

    @Test
    void getMethodFromFieldName() {
        String expected = "getId";

        assertEquals(expected, PersistenceHelper.setterFromField(UserDataSet.class, "id", true));
    }

    @Test
    void getMethodFromFieldNameErrors() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> PersistenceHelper.setterFromField(UserDataSet.class, "a", true));
        assertEquals("No setter for field a", e.getMessage());
    }

    @Test
    void getMethodFromFieldNameWithoutCheck() {
        String expected = "getId";

        assertEquals(expected, PersistenceHelper.setterFromField(UserDataSet.class, "id", false));
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