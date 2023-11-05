package com.kosa.shop.util;

import com.kosa.shop.util.functional.ExceptionFunction;
import com.kosa.shop.util.functional.ExceptionRunnable;
import com.kosa.shop.util.functional.ExceptionSupplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

    public static <T> void wrap(ExceptionRunnable<T> er) {
        try {
            er.run();
        } catch(Exception e) {
            log.error("<< Utils.wrap >> {}", e.getMessage());
        }
    }

    public static <R> R wrap(int i, ExceptionSupplier<R> eif) {
        try {
            return eif.get();
        } catch (Exception e) {
            log.error("<< Utils.wrap >> {}", e.getMessage());
            return null;
        }
    }

    public static <T, R> R wrap(T t, ExceptionSupplier<R> eif) {
        try {
            return eif.get();
        } catch (Exception e) {
            log.error("<< Utils.wrap >> {}", e.getMessage());
            return null;
        }
    }

    public static <T, R> R wrap(T t, ExceptionFunction<T, R> eif) {
        try {
            return eif.apply(t);
        } catch (Exception e) {
            log.error("<< Utils.wrap >> {}", e.getMessage());
            return null;
        }
    }

}
