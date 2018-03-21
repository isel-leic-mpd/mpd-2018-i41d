package util;

import java.util.Comparator;
import java.util.function.Function;

public class Cmp {

    /**
     * Returns a lexicographic-order comparator with a function
     * that extracts a Comparable sort key.
     */
    public static <T, R extends Comparable<R>> QueriesCmp<T, R> comparing(Function<T, R> prop) {
        /*
        return (o1, o2) -> {
            R r1 = prop.apply(o1);
            R r2 = prop.apply(o2);
            return r1.compareTo(r2);
        };
        */ // <=>
        return new QueriesCmp(prop);
    }

    public static class QueriesCmp<T, R extends Comparable<R>> implements Comparator<T> {
        final Function<T, R> prop;

        public QueriesCmp(Function<T, R> prop) {
            this.prop = prop;
        }

        @Override
        public int compare(T o1, T o2) {
            R r1 = prop.apply(o1);
            R r2 = prop.apply(o2);
            return r1.compareTo(r2);
        }

        public <U extends Comparable<U>> Comparator<T> thenBy(Function<T, U> prop2) {
            return (o1, o2) -> 0;
        }
    }
}
