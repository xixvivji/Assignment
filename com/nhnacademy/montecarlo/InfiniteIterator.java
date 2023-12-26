package com.nhnacademy.montecarlo;

public interface InfiniteIterator<T> extends java.util.Iterator<T> {
    @Override
    default boolean hasNext() {
        return true;
    }
    // iterator의 subtype인데 제대로 된건지? 잘못된건지 어쩔수 없는건지
    // 3가지중에서 본인들 판단.
}
