package util;

import java.util.function.Function;

public class Countify {
    public static <T,R> Counter<T,R> of(Function<T,R> inner) {
        return new Counter<>(inner);
    }

    public static class Counter<T,R> implements Function<T,R>{
        public Counter(Function<T, R> inner) {

        }
        public int getCount() {
            return 0;
        }
        @Override
        public R apply(T arg) {
            throw new UnsupportedOperationException();
        }
    }
}