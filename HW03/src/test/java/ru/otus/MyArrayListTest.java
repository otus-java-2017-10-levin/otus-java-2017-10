package ru.otus;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;

/*
 * Unit test for simple MyArrayList.
 * addAll(Collection<? super T> c, T... elements)

 static <T> void    copy(List<? super T> dest, List<? extends T> src)

 static <T> void    sort(List<T> list, Comparator<? super T> c)
 */


public class MyArrayListTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MyArrayListTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MyArrayListTest.class );
    }

    public void testIsEmpty() {
        List<Integer> list = createMyList();
    }


    private <T> List<T> createMyList() {
        return new MyArrayList<>();
    }

    private <T> List<T> createMyList(int capacity) {
        return new MyArrayList<>(capacity);
    }

    private <T> List<T> createMyList(Collection<? extends T> objs) {
        return new MyArrayList<>(objs);
    }

    public void testConstructor() {
        List<Integer> ints3 = new MyArrayList<>(Arrays.asList(0,1,2,3,4,5));
        ints3.add(1);
        assertEquals("[0, 1, 2, 3, 4, 5, 1]", ints3.toString());
        assertEquals(7, ints3.size());

        assertEquals("[]", createMyList(11).toString());
        assertEquals(0, createMyList(11).size());

        assertEquals("[]", createMyList().toString());
        assertEquals(0, createMyList().size());

    }

    public void testAddAll()
    {
        List<Integer> list = createMyList();
        Collections.addAll(list, 0, 1, 2, 3, 4, 5);
        assertEquals( "[0, 1, 2, 3, 4, 5]", list.toString());
    }

    public void testCopy() {
        List<Integer> source = createMyList(Arrays.asList(0, 1, 2, 3, 4, 5));
        List<Integer> dest = createMyList(Arrays.asList(5, 4, 3, 2, 1, 0, -1));
        Collections.copy(dest, source);

        assertEquals( "[0, 1, 2, 3, 4, 5]", source.toString());
        assertEquals( "[0, 1, 2, 3, 4, 5, -1]", dest.toString());
    }

    public void testSort() {
        List<Integer> list = createMyList();
        Collections.addAll(list, 0, 1, 2, 3, 4, 5);
        Collections.sort(list, Comparator.reverseOrder());
        assertEquals( "[5, 4, 3, 2, 1, 0]", list.toString());
    }

    public void testListIterator() {
        List<Integer> ints = createMyList();
        Collections.addAll(ints, 0, 1, 2, 3, 4, 5);
        ListIterator<Integer> iter = ints.listIterator();

        int counter = 5;
        while (iter.hasNext()) {
            iter.next();
            iter.set(counter--);
        }

        assertEquals("[5, 4, 3, 2, 1, 0]", ints.toString());

        List<Integer> ints1 = createMyList();
        Collections.addAll(ints1, 5, 4, 3, 2, 1, 0);
        ListIterator<Integer> iter1 = ints1.listIterator(ints.size());

        counter = ints1.size()-1;
        while(iter1.hasPrevious()) {
            iter1.previous();
            iter1.set(counter--);
        }

        assertEquals("[0, 1, 2, 3, 4, 5]", ints1.toString());
    }


    public void testContains() {
        List<Integer> ints = new MyArrayList<>(Arrays.asList(0, 1, 2, 3, null, 4, 5));
        Integer digit = 7;

        assertEquals(true, ints.contains(null));
        assertEquals(true, ints.contains(4));
        assertEquals(false, ints.contains(digit));

    }

    public void testToArray() {
        List<Integer> ints = new MyArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        Number[] result = {0, 1, 2, 3, 4, 5};
        Number[] a = new Number[1];

        a = ints.toArray(a);
        assertEquals(true, Arrays.equals(result, a));

        a = new Number[ints.size()];
        a = ints.toArray(a);
        assertEquals(true, Arrays.equals(result, a));
    }

    public void testContainsAll() {
        List<Integer> ints = new MyArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        List<Integer> ints2 = Arrays.asList(1,2,3, 0);
        List<Integer> ints3 = Arrays.asList(1,2,7, 0);

        assertEquals(true, ints.containsAll(ints2));
        assertEquals(false, ints.containsAll(ints3));
    }

    public void testIndexOf() {
        List<Integer> ints = new MyArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 2));

        assertEquals(2, ints.indexOf(2));
        assertEquals(-1, ints.indexOf(null));
        assertEquals(5, ints.lastIndexOf(2));
        assertEquals(-1, ints.lastIndexOf(null));
    }


    public void testAddRemove() {
        List<Integer> ints = createMyList();

        int size = 100_000;

        ints.add(1);    ints.add(1);    ints.add(1);
        for (int i=0; i < size; i++) {
            ints.add(i);
            ints.remove(1);
        }

        assertEquals(3, ints.size());
        assertEquals("[1, 99998, 99999]", ints.toString());

        ints = createMyList();
//        ints.remove(0);
    }


}
