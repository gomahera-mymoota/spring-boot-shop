package com.kosa.shop.functional;

@FunctionalInterface
public interface ExceptionFunction<T, R> {

    R apply(T t) throws Exception;

}
