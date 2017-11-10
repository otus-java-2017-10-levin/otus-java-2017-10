package ru.otus.Tests;


import ru.otus.annotations.Before;
import ru.otus.annotations.BeforeEach;
import ru.otus.annotations.Skip;
import ru.otus.annotations.Test;
import ru.otus.internals.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class New1 {
    private List<Integer> list = new ArrayList<>();

    @Before()
    public void before() {
//        Collections.addAll(list, 1, 2, 3);
    }

    @BeforeEach
    public void beforeEach() {
        Collections.addAll(list, 1, 2, 3);
    }

    @Test("test_true")
    public void testTrue() {
        Assert.assertEquals(list, Arrays.asList(1, 2, 3, 4));
    }

    @Test("test_false")
    public void testFalse() {
        Assert.assertTrue(false);
    }

    @Test("test skip")
    @Skip
    public void testSkip() {
        Assert.assertTrue(false);
    }


}
