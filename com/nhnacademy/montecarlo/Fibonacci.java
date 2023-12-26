package com.nhnacademy.montecarlo;

import java.util.NoSuchElementException;

public class Fibonacci implements InfiniteIterator<Integer> {
    // TODO: 채우기
    private int pre = 1;
    private int next = 1;
    private int temp = 0;

    public boolean hasNext() {
        return true;
    }

    public Integer next() {
        if (!hasNext())
            throw new NoSuchElementException("Fibonacci.next()");
        temp = pre;
        pre = next;
        next = temp + pre;
        return temp;
    }



}
