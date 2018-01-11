package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.PhonesDataSet;
import ru.otus.base.UserDataSet;

import static org.junit.jupiter.api.Assertions.*;

/*
 *  Copyright by Flow on 11.01.2018.
 
    Description here
 */
class CacheUnitTest {

    @Test
    void equals() {
        CacheUnit c1 = new CacheUnit(1, UserDataSet.class);
        CacheUnit c2 = new CacheUnit(1, UserDataSet.class);
        CacheUnit c3 = new CacheUnit(1, PhonesDataSet.class);
        CacheUnit c4 = new CacheUnit(2, UserDataSet.class);


        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, c4);
    }
}