package ru.otus;
/*
 *  Copyright by Flow on 01.11.2017.
 
    Test for MyArrayList in JUnit 5
 */

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("My ArrayList test")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(TimingExtension.class)
class MyArrayListTest {

    private List<Integer> source;
    private List<Integer> dest;
    private List<Integer> emptyList;

    private <T> void checkArray(List<T> list, T... expected) {
        assertArrayEquals(expected, list.toArray());
    }

    @BeforeEach
    void init() {
        emptyList = new MyArrayList<>();
        source = new MyArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        dest = new MyArrayList<>(Arrays.asList(5, 4, 3, 2, 1, 0, -1));
    }

    @Nested
    @DisplayName("Collections")
    class CollectionsTest {

        @Test
        @DisplayName("Check constructors")
        void testConstructor() {
            source.add(1);

            checkArray(source, 0, 1, 2, 3, 4, 5, 1);
            checkArray(new MyArrayList<Integer>(11));
            checkArray(emptyList);
        }

        @Test
        @DisplayName("Collections.addAll")
        void testCollectionsAddAll() {
            Collections.addAll(emptyList, 0, 1, 2, 3, 4, 5);
            checkArray(emptyList, 0, 1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("Collections.copy")
        void testCopy() {
            Collections.copy(dest, source);
            checkArray(source, 0, 1, 2, 3, 4, 5);
            checkArray(dest, 0, 1, 2, 3, 4, 5, -1);
        }
    }

    @Test
    @DisplayName("Collections.sort")
    void testSort() {
        Collections.sort(source, Comparator.reverseOrder());
        checkArray(source,5, 4, 3, 2, 1, 0);
    }

    @Nested
    @DisplayName("ListIterator")
    class ListIteratorTest {

        @Test
        @DisplayName("next()")
        void next() {
            ListIterator<Integer> it = source.listIterator();

            int counter = 5;
            while (it.hasNext()) {
                it.next();
                it.set(counter--);
            }

            checkArray(source, 5, 4, 3, 2, 1, 0);
        }

        @Test
        @DisplayName("previous()")
        void previous() {
            ListIterator<Integer> it = source.listIterator(source.size());

            int counter = source.size()-1;
            while(it.hasPrevious()) {
                it.previous();
                it.set(counter--);
            }

            checkArray(source, 0, 1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("nextIndex()")
        void nextIndex() {
            ListIterator<Integer> it = source.listIterator();
            int counter = 0;
            while(it.hasNext()) {
                assertTrue(counter == it.nextIndex());
                it.next();
                counter++;
            }
        }

        @Test
        @DisplayName("previousIndex()")
        void previousIndex() {
            ListIterator<Integer> it = source.listIterator();
            int counter = -1;
            while(it.hasNext()) {
                assertTrue(counter == it.previousIndex());
                it.next();
                counter++;
            }
        }

        @Test
        @DisplayName("set(T t)")
        void set() {
            ListIterator<Integer> it = source.listIterator();

            while (it.hasNext()) {
                it.next();
                it.set(-2);
            }

            checkArray(source, -2, -2, -2, -2, -2, -2);
        }

        @Test
        @DisplayName("remove()")
        void remove() {
            ListIterator<Integer> it = source.listIterator();

            while (it.hasNext()) {
                it.next();
                it.remove();
            }
            checkArray(source);
        }

        @Test
        @DisplayName("add(T)")
        void add() {
            ListIterator<Integer> it = source.listIterator();

            if (it.hasNext()) {
                it.next();
                it.add(-1);
                it.add(-1);
                it.add(-1);
            }
            checkArray(source, -1, -1, -1, 0, 1, 2, 3, 4, 5);
        }
    }


    @Test
    @DisplayName("contains()")
    void testContains() {
        Integer digit = 7;

        assertEquals(false, source.contains(null));
        assertEquals(true, source.contains(4));
        assertEquals(false, source.contains(digit));

    }

    @Test
    @DisplayName("toArray(T[])")
    void testToArray() {
        Number[] result = {0, 1, 2, 3, 4, 5};
        Number[] a = new Number[1];

        a = source.toArray(a);
        assertEquals(true, Arrays.equals(result, a));

        a = new Number[source.size()];
        a = source.toArray(a);
        assertEquals(true, Arrays.equals(result, a));
    }

    @Test
    @DisplayName("containsAll()")
    void testContainsAll() {
        List<Integer> ints2 = Arrays.asList(1, 2, 3, 0);
        List<Integer> ints3 = Arrays.asList(1, 2, 7, 0);

        assertEquals(true, source.containsAll(ints2));
        assertEquals(false, source.containsAll(ints3));
    }

    @Test
    @DisplayName("indexOf()")
    void testIndexOf() {
        source.add(2);
        // 0, 1, 2, 3, 4, 5, 2
        assertEquals(2, source.indexOf(2));
        assertEquals(-1, source.indexOf(null));
        assertEquals(6, source.lastIndexOf(2));
        assertEquals(-1, source.lastIndexOf(null));
    }

    @Test
    @DisplayName("Add and Remove")
    void testAddRemove() {
        int size = 100_000;

        emptyList.add(1);    emptyList.add(1);    emptyList.add(1);
        for (int i=0; i < size; i++) {
            emptyList.add(i);
            emptyList.remove(new Integer(i)); // ints.remove(Object) calls ints.remove(i)
        }
        checkArray(emptyList, 1, 1, 1);
    }

    @Test
    @DisplayName("addAll()")
    void testAddAll() {
        List<Integer> ints = new MyArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> toAdd = new MyArrayList<>(Arrays.asList(null, 4, 5));

        boolean res = ints.addAll(toAdd);
        checkArray(ints, 1, 2, 3, null, 4, 5);
        assertEquals(true, res);

        ints = new MyArrayList<>();
        res = ints.addAll(Arrays.asList(1, 2, 3));
        checkArray(ints, 1, 2, 3);
        assertEquals(true, res);


        ints = new MyArrayList<>(Arrays.asList(1, 2, 3));
        ints.addAll(3, Arrays.asList(4, 5, 6));
        checkArray(ints, 1, 2, 3, 4, 5, 6);

        ints = new MyArrayList<>();
        ints.addAll(0, Arrays.asList(4, 5, 6));
        checkArray(ints, 4, 5, 6);
    }

    @Test
    @DisplayName("add(T)")
    void testAdd() {
        source.add(0, 0);
        checkArray(source, 0, 0, 1, 2, 3, 4, 5);

        source = new MyArrayList<>();
        for (int i=0; i < 10; i++) {
            source.add(0, i);
        }

        checkArray(source, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
    }

    @Test
    @DisplayName("removeAll()")
    void testRemoveAll() {
        List<Integer> ints = new MyArrayList<>(Arrays.asList(1, 1, 2, 3));
        boolean res = ints.removeAll(Arrays.asList(1, 1));

        checkArray(ints, 2, 3);
        assertEquals(true, res);

        res = ints.removeAll(emptyList);
        checkArray(ints, 2, 3);
        assertEquals(false, res);

        res = emptyList.removeAll(Arrays.asList(1, 2));
        checkArray(emptyList);
        assertEquals(false, res);

    }

    @Test
    @DisplayName("retainAll()")
    void testRetainAll() {
        List<Integer> ints = new MyArrayList<>(Arrays.asList(1,1, 2, 3));

        boolean res = ints.retainAll(Arrays.asList(2, 3));
        checkArray(ints, 2, 3);
        assertEquals(true, res);

        res = ints.retainAll(emptyList);
        checkArray(ints);
        assertEquals(true, res);
    }
}