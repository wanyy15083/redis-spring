package com.redis.cache;

public interface Function<T, E> {
    public T callback(E e);
}
