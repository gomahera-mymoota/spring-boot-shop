package com.kosa.shop.util.functional;

@FunctionalInterface
public interface ExceptionRunnable<T> {

    void run() throws Exception;

}
