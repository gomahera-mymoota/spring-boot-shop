package com.kosa.shop.util.functional;

@FunctionalInterface
public interface ExceptionFunction<T, R> {

    R apply(T value) throws Exception;

}