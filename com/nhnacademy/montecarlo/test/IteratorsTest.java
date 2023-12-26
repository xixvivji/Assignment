package com.nhnacademy.montecarlo.test;


import static com.nhnacademy.montecarlo.Iterators.*;
import static com.nhnacademy.montecarlo.Mathx.fibonacci;
import static org.junit.jupiter.api.Assertions.*;
import com.nhnacademy.montecarlo.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;


class IteratorsTest {

    @Test
    void checkIsInfiniteTest() {
        InfiniteIterator infiniteIterator = () -> true;

        assertNotNull(infiniteIterator, "not null");
        assertThrows(IllegalArgumentException.class,
                () -> checkIsInfinite(infiniteIterator, "Infinite"));

    }

    @Test
    void checkIsNull() {
        assertNull(null, "null");
        assertThrows(NullPointerException.class, () -> Iterators.checkIsNull(null, "null"));
    }

    @Test
    void reduceIterableTest() {
        BiFunction<Integer, Integer, Integer> biFunction = (r, e) -> r + e;
        int init = 0;
        Iterator<Integer> es = limit(iterate(1, x -> x + 1), 10);
        InfiniteIterator infiniteIterator = () -> true;

        assertNotNull(es, "not null");
        assertNotNull(biFunction, "not null");
        assertNotNull(init, "not null");

        assertNotNull(infiniteIterator, "not null");
        assertTrue(infiniteIterator instanceof InfiniteIterator);

        assertThrows(IllegalArgumentException.class,
                () -> Iterators.reduce(infiniteIterator, biFunction, init));
        assertThrows(NullPointerException.class,
                () -> Iterators.reduce((Iterable<Integer>) null, biFunction, init));
        assertThrows(NullPointerException.class, () -> Iterators.reduce(es, null, init));
        assertThrows(NullPointerException.class, () -> Iterators.reduce(es, biFunction, null));

        int result = Iterators.reduce(es, biFunction, init);
        assertEquals(55, result);

    }

    @Test
    void equalsTest() {
        Iterator<Integer> es1 = limit(iterate(1, x -> x + 1), 10);
        Iterator<Integer> es2 = limit(iterate(1, x -> x + 1), 10);
        Iterator<Integer> es3 = limit(iterate(1, x -> x + 1), 11);
        InfiniteIterator infiniteIterator = () -> true;

        assertTrue(infiniteIterator instanceof InfiniteIterator);

        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
                Iterators.limit(infiniteIterator, 10));

        assertThrows(NullPointerException.class, () -> Iterators.equals(null, es1));

        assertTrue(Iterators.equals(es1, es2));
        assertFalse(Iterators.equals(es1, es3));
        assertFalse(Iterators.equals(es1, infiniteIterator));

    }


    @Test
    void toStringTest() {
        Iterator<Integer> es1 = limit(iterate(1, x -> x + 1), 10);
        String separator = " ";

        assertNotNull(es1, "not null");
        assertNotNull(separator, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.toString(null, separator));
        assertThrows(NullPointerException.class, () -> Iterators.toString(es1, null));

        assertEquals("1 2 3 4 5 6 7 8 9 10", Iterators.toString(es1, separator));

    }

    @Test
    void mapTest() {
        Iterator<Integer> es = limit(iterate(1, x -> x + 1), 3);
        Function<Integer, String> function = Object::toString;
        Iterator<String> result = Iterators.map(es, function);
        assertNotNull(es, "not null");
        assertNotNull(function, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.map(null, function));
        assertThrows(NullPointerException.class, () -> Iterators.map(es, null));

        assertNotNull(result);

        assertTrue(result.hasNext());
        assertEquals("1", result.next());
        assertTrue(result.hasNext());
        assertEquals("2", result.next());
        assertTrue(result.hasNext());
        assertEquals("3", result.next());
        assertFalse(result.hasNext());

    }

    @Test
    void filterTest() {
        Iterator<Integer> es1 = limit(iterate(1, x -> x + 1), 5);
        Predicate<Integer> predicate1 = x -> x % 2 == 0;

        assertNotNull(es1, "not null");
        assertNotNull(predicate1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.filter(null, predicate1));
        assertThrows(NullPointerException.class, () -> Iterators.filter(es1, null));

        Iterator<Integer> result = Iterators.filter(es1, predicate1);
        assertNotNull(result);


        Iterable<Integer> fib = Mathx::fibonacci;
        assertTrue(fibonacci() instanceof InfiniteIterator);
        assertTrue(Iterators.equals(limit(fibonacci(), 10), StreamSupport
                .stream(fib.spliterator(), false).limit(10).iterator()));

    }

    @Test
    void findFirstTest() {
        Iterator<Integer> es1 = limit(iterate(1, x -> x + 1), 5);
        Predicate<Integer> predicate1 = x -> x % 2 == 0;

        assertNotNull(es1, "not null");
        assertNotNull(predicate1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.findFirst(null, predicate1));
        assertThrows(NullPointerException.class, () -> Iterators.findFirst(es1, null));
        Integer result = Iterators.findFirst(es1, predicate1);

        assertNotNull(result);
        assertEquals(2, result);

    }

    @Test
    void iteratorTest() {
        String seed1 = "iterator";
        UnaryOperator<Object> f1 = x -> x;

        assertNotNull(f1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.iterate(seed1, null));

        UnaryOperator<String> function3 = s -> s + " ";
        InfiniteIterator<String> iterator = Iterators.iterate(seed1, function3);

        assertNotNull(iterator);

        assertEquals("iterator", iterator.next());
        assertEquals("iterator ", iterator.next());
    }


    @Test
    void limitTest() {

        Iterator<Integer> es1 = limit(iterate(1, x -> x + 1), 5);

        assertNotNull(es1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.limit(null, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> Iterators.limit(es1, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> Iterators.limit(es1, 0));

        Iterator<Integer> result = Iterators.limit(es1, 3);
        assertNotNull(result);

        assertTrue(result.hasNext());
        assertEquals(1, result.next());
        assertTrue(result.hasNext());
        assertEquals(2, result.next());
        assertTrue(result.hasNext());

    }

    @Test
    void generateTest() {


        assertThrows(NullPointerException.class, () -> Iterators.generate(null));

        Supplier<Integer> supplier1 = () -> 1;

        assertNotNull(supplier1, "not null");
        InfiniteIterator<Integer> result = Iterators.generate(supplier1);

        assertNotNull(result);
        assertEquals(1, result.next());

    }

    @Test
    void zipTest() {

        Iterator<String> yIterator1 = iterate("A", s -> s + "A");
        Iterator<Integer> xIterator1 = limit(iterate(1, x -> x + 1), 5);
        BiFunction<Integer, String, String> biFunction1 = (x, y) -> x + y;

        assertNotNull(xIterator1, "not null");
        assertNotNull(yIterator1, "not null");
        assertNotNull(biFunction1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.zip(null, xIterator1, yIterator1));
        assertThrows(NullPointerException.class, () -> Iterators.zip(biFunction1, null, yIterator1));
        assertThrows(NullPointerException.class, () -> Iterators.zip(biFunction1, xIterator1, null));

        Iterator<String> result = Iterators.zip(biFunction1, xIterator1, yIterator1);

        assertNotNull(result);

        assertTrue(result.hasNext());
        assertEquals("1A", result.next());

    }

    @Test
    void countTest() {
        Iterator<Integer> iterator1 = limit(iterate(1, x -> x + 1), 5);
        assertNotNull(iterator1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.count(null));

        assertEquals(5, Iterators.count(iterator1));

    }

    @Test
    void getTest() {
        Iterator<Integer> iterator1 = limit(iterate(1, x -> x + 1), 5);

        assertNotNull(iterator1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.get(null, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> Iterators.get(iterator1, -1));

        assertEquals(3, Iterators.get(iterator1, 2));

    }


    @Test
    void toListTest() {
        Iterator<Integer> iterator1 = limit(iterate(1, x -> x + 1), 5);

        assertNotNull(iterator1, "not null");

        assertThrows(NullPointerException.class, () -> Iterators.toList(null));

        List<Integer> list1 = Iterators.toList(iterator1);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), list1);

    }

}






