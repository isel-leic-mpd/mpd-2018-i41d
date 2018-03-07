package util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Miguel Gamboa
 *         created on 01-03-2018
 */
public class Queries {
    public static <T> Iterable<T> filter(
            Iterable<T> src,
            Predicate<T> p) {
        List<T> res = new ArrayList<>();
        for(T item: src) {
            if(p.test(item))
                res.add(item);
        }
        return res;
    }

    /**
     * Returns a new sequence of R (Iterable<R>)
     * resulting from applying the mapper function to the elements
     * of the original src sequence of T objects.
     */
    public static <T, R> Iterable<R> map(
            Iterable<T> src,
            Function<T, R> mapper) {
        List<R> res = new ArrayList<>();
        for (T item:src) {
            res.add(mapper.apply(item));
        }
        return res;
    }

    /**
     * Applies an accumulator function over a sequence.
     */
    public static <T, R> R reduce(
            Iterable<T> src,
            R seed,
            BiFunction<R, T, R> acc) {
        for (T item : src) {
            seed = acc.apply(seed,item);
        }
        return seed;
    }

    public static <T> void forEach(
            Iterable<T> src,
            Consumer<T> cons) {
        for (T item : src) {
            cons.accept(item);
        }
    }



    public static <T> int count(Iterable<T> src) {
        int n = 0;
        for(T item : src) n++;
        return n;
    }
}
