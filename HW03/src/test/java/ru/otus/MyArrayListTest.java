package ru.otus;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;


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

    private static final List<Integer> EMPTY_LIST = new MyArrayList<>();

    private <T> List<T> createMyList() {
//        return new ArrayList<>();
        return new MyArrayList<>();
    }

    private <T> List<T> createMyList(int capacity) {
//        return new ArrayList<>(capacity);
        return new MyArrayList<>(capacity);
    }

    private <T> List<T> createMyList(Collection<? extends T> objs) {
//        return new ArrayList<>(objs);
        return new MyArrayList<>(objs);
    }

    private <T> void checkArray(Collection<? extends T> c, String expectedOutput, int expectedSize) {
        assertEquals(expectedOutput, c.toString());
        assertEquals(expectedSize, c.size());
    }

    public void testConstructor() {
        List<Integer> ints3 = new MyArrayList<>(Arrays.asList(0,1,2,3,4,5));
        ints3.add(1);

        checkArray(ints3, "[0, 1, 2, 3, 4, 5, 1]",7);

        checkArray(createMyList(11), "[]", 0);

        checkArray(EMPTY_LIST, "[]", 0);


    }

    public void testCollectionsAddAll()
    {
        List<Integer> list = createMyList();
        Collections.addAll(list, 0, 1, 2, 3, 4, 5);
        assertEquals( "[0, 1, 2, 3, 4, 5]", list.toString());
    }

    public void testCopy() {
        List<Integer> source = createMyList(Arrays.asList(0, 1, 2, 3, 4, 5));
        List<Integer> dest = createMyList(Arrays.asList(5, 4, 3, 2, 1, 0, -1));

        Collections.copy(dest, source);

        checkArray(source, "[0, 1, 2, 3, 4, 5]", 6);
        checkArray(dest, "[0, 1, 2, 3, 4, 5, -1]", 7);
    }

    public void testSort() {
        List<Integer> list = createMyList();
        Collections.addAll(list, 0, 1, 2, 3, 4, 5);
        Collections.sort(list, Comparator.reverseOrder());

        checkArray(list,"[5, 4, 3, 2, 1, 0]", 6);
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

        checkArray(ints, "[5, 4, 3, 2, 1, 0]", 6);

        List<Integer> ints1 = createMyList();
        Collections.addAll(ints1, 5, 4, 3, 2, 1, 0);
        ListIterator<Integer> iter1 = ints1.listIterator(ints.size());

        counter = ints1.size()-1;
        while(iter1.hasPrevious()) {
            iter1.previous();
            iter1.set(counter--);
        }

        checkArray(ints1, "[0, 1, 2, 3, 4, 5]", 6);
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
        List<Integer> ints2 = Arrays.asList(1, 2, 3, 0);
        List<Integer> ints3 = Arrays.asList(1, 2, 7, 0);

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
            ints.remove(new Integer(i)); // ints.remove(Object) calls ints.remove(i)
        }


        checkArray(ints, "[1, 1, 1]", 3);

//        ints = createMyList();
//        ints.remove(0);
    }

    public void testAddAll() {
        List<Integer> ints = createMyList(Arrays.asList(1, 2, 3));
        List<Integer> toAdd = createMyList(Arrays.asList(null, 4, 5));

        boolean res = ints.addAll(toAdd);
        checkArray(ints, "[1, 2, 3, null, 4, 5]", 6);
        assertEquals(true, res);

        ints = createMyList();
        res = ints.addAll(Arrays.asList(1, 2, 3));
        checkArray(ints, "[1, 2, 3]", 3);
        assertEquals(true, res);


        ints = createMyList(Arrays.asList(1, 2, 3));
        ints.addAll(3, Arrays.asList(4, 5, 6));
        checkArray(ints, "[1, 2, 3, 4, 5, 6]", 6);

        ints = createMyList();
        ints.addAll(0, Arrays.asList(4, 5, 6));
        checkArray(ints, "[4, 5, 6]", 3);
    }

    public void testAdd() {
        List<Integer> ints = createMyList(Arrays.asList(1, 2, 3, 4, 5));

        ints.add(0, 0);
        assertEquals("[0, 1, 2, 3, 4, 5]", ints.toString());
        assertEquals(6, ints.size());


        // adding to empty list
        ints = createMyList();
        for (int i=0; i < 10; i++) {
            ints.add(0, i);
        }

        checkArray(ints, "[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", 10);
    }

    public void testRemoveAll() {
        List<Integer> ints = createMyList(Arrays.asList(1,1, 2, 3));

        boolean res = ints.removeAll(Arrays.asList(1, 1));

        checkArray(ints, "[2, 3]", 2);
        assertEquals(true, res);

        res = ints.removeAll(EMPTY_LIST);
        checkArray(ints, "[2, 3]", 2);
        assertEquals(false, res);

        res = EMPTY_LIST.removeAll(Arrays.asList(1, 2));
        checkArray(EMPTY_LIST, "[]", 0);
        assertEquals(false, res);

    }

    public void testRetainAll() {
        List<Integer> ints = createMyList(Arrays.asList(1,1, 2, 3));

        boolean res = ints.retainAll(Arrays.asList(2, 3));
        checkArray(ints, "[2, 3]", 2);
        assertEquals(true, res);

        res = ints.retainAll(EMPTY_LIST);
        checkArray(ints, "[]", 0);
        assertEquals(true, res);
    }
}