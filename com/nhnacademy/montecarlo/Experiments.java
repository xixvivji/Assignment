package com.nhnacademy.montecarlo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Experiments<T extends Number> implements Iterator<T> {

    private final String quality;
    private final String distribution;
    private final Iterator<T> iterator;
    private final int LIMIT;

    public Experiments(Iterator<T> iterator, String quality, String distribution) {
        this(iterator, quality, distribution, 10000);
    }
    public Experiments(Iterator<T> iterator, String quality, String distribution, int limit) {
        this.iterator = iterator;
        this.quality = quality;
        this.distribution = distribution;
        this.LIMIT = limit;
    }

    public void report() {
        System.out.println(quality);
        System.out.println(distribution);
        System.out.println(Mathx.sum(experimentsLimit()) / Iterators.count(experimentsLimit()));
    }

    private Iterator<T> experimentsLimit(){
        return Iterators.limit(iterator, LIMIT);
    }
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        if(! iterator.hasNext())
            throw new NoSuchElementException("experiments error");

        return iterator.next();
    }
}
