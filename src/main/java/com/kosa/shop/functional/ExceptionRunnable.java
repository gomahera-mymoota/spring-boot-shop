package com.kosa.shop.functional;

@FunctionalInterface
public interface ExceptionRunnable<T> {

    void run() throws Exception;

}
