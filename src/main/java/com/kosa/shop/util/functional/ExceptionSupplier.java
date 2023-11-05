package com.kosa.shop.util.functional;

@FunctionalInterface
public interface ExceptionSupplier<T> {
    T get() throws Exception ;
}
