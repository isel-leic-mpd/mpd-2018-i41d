package util;

import java.util.Comparator;
import java.util.function.Function;

public class Cmp {

    /**
     * Returns a lexicographic-order comparator with a function
     * that extracts a Comparable sort key.
     */
    public static <T, R extends Comparable<R>> QueriesCmp<T> comparing(Function<T, R> prop) {
        return (o1, o2) -> {
            R r1 = prop.apply(o1);
            R r2 = prop.apply(o2);
            return r1.compareTo(r2);
        };
    }

    public static interface QueriesCmp<T> extends Comparator<T> {

        @Override abstract int compare(T o1, T o2);

        public default <U extends Comparable<U>> QueriesCmp<T> thenBy(Function<T, U> prop2) {

            return (o1, o2) -> {
                int res = QueriesCmp.this.compare(o1, o2);
                if(res != 0) return res;
                U r1 = prop2.apply(o1);
                U r2 = prop2.apply(o2);
                return r1.compareTo(r2);
            };
        }
    }
}
