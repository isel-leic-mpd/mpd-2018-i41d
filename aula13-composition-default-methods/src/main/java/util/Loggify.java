package util;

import java.util.function.Function;

public class Loggify {
    public static <T,R> Function<T,R> of(Function<T,R> inner, String msg) {
        return arg -> {
            System.out.println(msg);
            return inner.apply(arg);
        };
    }
}
