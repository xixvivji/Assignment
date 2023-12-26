package com.nhnacademy.montecarlo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Iterators {
  public static <T> void checkIsInfinite(T t, String message){
    if(t instanceof InfiniteIterator){
      throw new IllegalArgumentException(message);
    }
  }

public static void checkIsNull(Object o, String message){
    if (o == null){
      throw new NullPointerException(message);
    }
}

  public static <E, R> R reduce(Iterable<E> es, BiFunction<R, E, R> biFunction, R init) {
    checkIsInfinite(es, "Iterable<E> es is Infinite");
    checkIsNull(es, "Iterable<E> es is Null");
    checkIsNull(biFunction, "biFunction is Null");
    checkIsNull(init, "init is Null");

    R result = init;
    for (E e : es)
      result = biFunction.apply(result, e);
    return result;
  }

  public static <E, R> R reduce(Iterator<E> es, BiFunction<R, E, R> biFunction, R init) {
    checkIsInfinite(es, "Iterator<E> es is infinite");
    checkIsNull(es, "Iterator<E> es is Null");
    checkIsNull(biFunction, "biFunction is Null");
    checkIsNull(init, "init is Null");

    return reduce(() -> es, biFunction, init);
  }

  public static <T> boolean equals(Iterator<T> xs, Iterator<T> ys) { // TODO: reduce를 써서

    return reduce(zip(Objects::equals, xs, ys), (r, e) -> r && e, true) && (xs.hasNext() == ys.hasNext());
  }


  public static <T> String toString(Iterator<T> es, String separator) { // TODO: reduce를 써서
    checkIsNull(es, "Iterator<T> es is Null");
    checkIsNull(separator, "separator is Null");

    return reduce(es, (r, e) -> r + separator + e, String.valueOf(es.next()));
  }


  public static <E, R> Iterator<R> map(Iterator<E> es, Function<E, R> function) {
    checkIsNull(es,"Iterator<E> es is Null");
    checkIsNull(function,"function is Null");
    return new Iterator<R>() {
      public boolean hasNext() {
        return es.hasNext();
      }

      public R next() {
        return function.apply(es.next());
      }
    };
  }

  public static <E> Iterator<E> filter(Iterator<E> iterator, Predicate<E> predicate) {
    // TODO: Bug를 찾을 수 있는 test code를 IteratorTest.filterTest에 쓰고, Bug 고치기

    checkIsNull(iterator, "Iterator<E> iterator is Null");
    checkIsNull(predicate,"Predicate<E> predicate is NUll");

    return new Iterator<E>() {
      private E current;

      public boolean hasNext() {

        current = findFirst(iterator,predicate);
        return current!= null;
      }

      public E next() {
        if (!hasNext())
          throw new NoSuchElementException("filter");
        return current;
      }
    };
  }
  public static <E> E findFirst(Iterator<E> iterator, Predicate<E> predicate) {

    checkIsNull(iterator, "Iterator<E> iterator is Null");
    checkIsNull(predicate,"Predicate<E> predicate is NUll");

    while (iterator.hasNext()) {
      E first = iterator.next();

      if (predicate.test(first))
        return first;
    }
    return null;
  }

  public static <T> InfiniteIterator<T> iterate(T seed, UnaryOperator<T> f) {

    checkIsNull(f,"UnaryOperator<T> f is Null");
    return new InfiniteIterator<T>() {
      T current = seed;

      @Override
      public T next() {
        T old = current;
        current = f.apply(current);
        return old;
      }
    };
  }

  // TODO:
  public static <T> Iterator<T> limit(Iterator<T> iterator, long maxSize) {
    checkIsNull(iterator,"Iterator<T> iterator is Null");

    if(maxSize <= 0){
      throw new IndexOutOfBoundsException("Illegal");
    }
    return new Iterator<T>() {
      private long size;

      @Override
      public boolean hasNext() {
        return size < maxSize && iterator.hasNext();
      }


      @Override
      public T next() {
        if (!hasNext())
          throw new NoSuchElementException("Iterator.next() : maxSize 값을 초과 했습니다.");

        size++;
        return iterator.next();
      }
    };
  }

  // TODO:
  public static <T> InfiniteIterator<T> generate(Supplier<T> supplier) {
    checkIsNull(supplier, "Supplier<T> supplier is Null");
    return new InfiniteIterator<T>() {
      @Override
      public T next() {
        return supplier.get();
      }
    };

  }

  public static <X, Y, Z> Iterator<Z> zip(BiFunction<X, Y, Z> biFunction, Iterator<X> xIterator,
      Iterator<Y> yIterator) {
    checkIsNull(biFunction,"BiFunction<X, Y, Z> biFunction is Null");
    checkIsNull(xIterator, "Iterator<X> xIterator is Null");
    checkIsNull(yIterator, "Iterator<X> yIterator is Null");
    return new Iterator<Z>() {
      public boolean hasNext() {
        return xIterator.hasNext() && yIterator.hasNext();
      }

      public Z next() {
        if (!hasNext())
          throw new NoSuchElementException("zip");


        return biFunction.apply(xIterator.next(), yIterator.next());
      }
    };
  }

  // TODO: reduce를 써서
  public static <E> long count(Iterator<E> iterator) {
    checkIsNull(iterator, "Iterator<E> iterator is Null");
    return reduce(iterator, (r, e) -> r + 1, 0L);
  }

  public static <T> T get(Iterator<T> iterator, long index) {
    checkIsNull(iterator,"Iterator<T> iterator is Null");
    if (index < 0)
      throw new IndexOutOfBoundsException("index < " + index);
    return getLast(limit(iterator, index + 1));
  }

  private static <T> T getLast(Iterator<T> iterator) {
    while (true) {
      T current = iterator.next();
      if (!iterator.hasNext())
        return current;
    }
  }

  public static <T> List<T> toList(Iterator<T> iterator) {
    checkIsNull(iterator, "Iterator<T> iterator is Null");
    List<T> list = new ArrayList<>();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list;
  }

  public static <E> void print(Iterator<E> iterator, String separator,
      java.io.PrintStream printStream) {
    printStream.print(toString(iterator, separator));
  }

  public static <E> void print(Iterator<E> iterator, String separator) {
    print(iterator, separator, System.out);
  }

  public static <E> void println(Iterator<E> iterator, String separator,
      java.io.PrintStream printStream) {
    print(iterator, separator, printStream);
    printStream.println();
  }

  public static <E> void println(Iterator<E> iterator, String separator) {
    println(iterator, separator, System.out);
  }

  public static <E> void println(Iterator<E> iterator) {
    println(iterator, ", ");
  }

  private Iterators() {}

}


