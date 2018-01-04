package ru.otus.persistence.fields;

import org.junit.jupiter.api.Test;
import ru.otus.base.UserDataSet;
import ru.otus.classes.AddressDataSet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityInstanceImplTest {

    EntityInstance<UserDataSet> set = new EntityInstanceImpl<>("name", new UserDataSet("Flow"), UserDataSet.class);

    @Test
    void setState() {
        EntityInstance<AddressDataSet> address = new EntityInstanceImpl<>("address", new AddressDataSet(), AddressDataSet.class);
        set.addReferenceEntity(address);
        set.setState(EntityInstance.STATE.DETACHED);
        assertEquals(EntityInstance.STATE.DETACHED, set.getState());

        EntityInstance<?> res = set.getEntitiesFields().get(0);
        assertEquals(EntityInstance.STATE.DETACHED, res.getState());
    }

    @Test
    void getDefaultState() {
        assertEquals(EntityInstance.STATE.TRANSIENT, set.getState());
    }

    @Test
    void getEntityClass() {
        assertEquals(UserDataSet.class, set.getFieldClass());
    }

    @Test
    void getChildren() {
        assertEquals(0, set.getEntitiesFields().size());
    }

    @Test
    void addReferenceEntity() {
        EntityInstance<UserDataSet> user = new EntityInstanceImpl<>("inner", new UserDataSet("Flow1"), UserDataSet.class);
        set.addReferenceEntity(user);
        List<EntityInstance<?>> list = set.getEntitiesFields();
        assertEquals(1, list.size());

        EntityInstance<?> child = list.get(0);
        assertEquals("inner", child.getName());
        assertEquals(0, child.getEntitiesFields().size());
        assertEquals(UserDataSet.class, child.getFieldClass());
        assertEquals(EntityInstance.STATE.TRANSIENT, child.getState());
        assertEquals(UserDataSet.class, child.getFieldClass());
    }

    @Test
    void addSimpleField() {
        SimpleField<String> str = new SimpleFieldImpl<>("string", "FLow", String.class);
        set.addSimpleField(str);
        List<SimpleField<?>> list = set.getSimpleFields();
        assertEquals(1, list.size());

        SimpleField<?> field = list.get(0);
        assertEquals("string", field.getName());
        assertEquals("FLow", field.getValue());
        assertEquals(String.class, field.getFieldClass());
    }

    @Test
    void getSimpleFields() {
        List<SimpleField<?>> list = set.getSimpleFields();
        assertEquals(0, list.size());
    }

    @Test
    void addArrayField() {
        ArrayField<Integer> str = new ArrayFieldImpl<>("array", new Integer[]{0, 1, 2});
        set.addArrayField(str);
        List<ArrayField<?>> list = set.getArrayFields();
        assertEquals(1, list.size());

        ArrayField<?> field = list.get(0);
        assertEquals("array", field.getName());
        assertArrayEquals(new Integer[]{0, 1, 2}, field.getArray());
        assertEquals(Integer[].class, field.getFieldClass());
    }

    @Test
    void getArrayFields() {
        List<ArrayField<?>> list = set.getArrayFields();
        assertEquals(0, list.size());
    }
}