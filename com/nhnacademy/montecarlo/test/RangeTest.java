package com.nhnacademy.montecarlo.test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nhnacademy.montecarlo.Iterators;
import com.nhnacademy.montecarlo.Mathx;
import com.nhnacademy.montecarlo.Range;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RangeTest {

    @Test
    void rangeTest(){
        Range range = new Range(1,10);

        assertEquals(1, range.min());
        assertEquals(9, range.max());
        assertEquals(10, range.end());
        assertNotEquals(-1,range.min());

        Iterator<Long> iterator = range.iterator();
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());

        Exception e = assertThrows(Exception.class, () -> new Range(10, 1));
    }

    @Test
    void rangeEndTest(){
        Range range = new Range(10);

        assertEquals(1, range.min());
        assertEquals(9, range.max());
        assertEquals(10, range.end());
        assertNotEquals(0,range.min());

        Iterator<Long> iterator = range.iterator();
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());

    }
    @Test
    void rangeClosedTest(){
        Range range = Range.closed(1,10);
        assertEquals(1, range.min());
        assertEquals(10, range.max());
        assertEquals(11, range.end());
        assertNotEquals(9,range.max());

        Iterator<Long> iterator = range.iterator();
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        Exception e = assertThrows(Exception.class, () -> new Range(10, 1));
    }


    @Test
    void rangeMax() {
        Range range = new Range(1, 10);
        assertEquals(9, range.max());
    }

    @Test
    void rangeMin() {
        Range range = new Range(1, 10);
        assertEquals(1, range.min());
    }

    @Test
    void rangeEnd() {
        Range range = new Range(1, 10);
        assertEquals(10, range.end());
    }

    @Test
    void rangeSize() {
        Range range = new Range(1, 10);
        assertEquals(9, range.size());
    }

    @Test
    void classInvariantTest() {
        assertThrows(IllegalArgumentException.class, () -> new Range(10, 1));
    }

    @Test
    void iteratorTest() {
        Range range = new Range(1, 4);
        Iterator<Long> iterator = range.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
