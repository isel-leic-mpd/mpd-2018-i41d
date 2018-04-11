package util;

import java.util.function.Function;

public class Countify {
    public static <T,R> Counter<T,R> of(Function<T,R> inner) {
        return new Counter<>(inner);
    }

    public static class Counter<T,R> implements Function<T,R>{
        int counter;
        Function<T, R> inner;
        public Counter(Function<T, R> inner) {
            this.inner = inner;
        }
        public int getCount() {
            return counter;
        }
        @Override
        public R apply(T arg) {
            counter++;
            return inner.apply(arg);
        }
    }
}