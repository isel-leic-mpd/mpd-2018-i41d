package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static util.Box.empty;
import static util.Box.of;

/**
 * @author Miguel Gamboa
 *         created on 01-03-2018
 */
public class Queries {
    public static <T> Iterable<T> filter(
            Iterable<T> src,
            Predicate<T> p) {
        return () -> new FilterIterator<>(src, p);
    }

    static class FilterIterator<T> implements Iterator<T> {
        final Predicate<T> p;
        final Iterator<T> src;
        Box<T> curr;
        public FilterIterator(Iterable<T> src,Predicate<T> p) {
            this.src = src.iterator();
            this.p = p;
            curr = empty();
        }
        public boolean hasNext() {
            if (curr.isPresent()) return true;
            while(src.hasNext()){
                T item = src.next();
                if (p.test(item)) {
                    curr = of(item);
                    return true;
                }
            }
            return false;
        }
        public T next() {
            if(!hasNext()) throw new NoSuchElementException();
            T aux = curr.getItem();
            curr = empty();
            return aux;
        }
    }

    /**
     * Returns a new sequence of R (Iterable<R>)
     * resulting from applying the mapper function to the elements
     * of the original src sequence of T objects.
     */
    public static <T, R> Iterable<R> map(
            Iterable<T> src,
            Function<T, R> mapper) {
        return () -> new MapIterator<>(src, mapper);
    }

    static class MapIterator<T,R> implements Iterator<R> {
        final Function<T, R> mapper;
        final Iterator<T> src;
        public MapIterator(Iterable<T> src,Function<T, R> mapper) {
            this.src = src.iterator();
            this.mapper = mapper;
        }
        public boolean hasNext() { return src.hasNext(); }
        public R next() { return mapper.apply(src.next()); }
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

    /**
     * Generates an infinite sequence of elements => LAZY
     * !!!! WE CANNOT use an auxiliary List !!!!
     */
    public static <T> Iterable<T> generate(Supplier<T> src) {
        return () -> new Generator<>(src);
    }

    static class Generator<T> implements Iterator<T> {
        final Supplier<T> src;
        public Generator(Supplier<T> src) { this.src = src; }
        public boolean hasNext() { return true; }
        public T next() { return src.get(); }
    }

    /**
     * <=> Top do SQL
     */
    public static <T> Iterable<T> limit(Iterable<T> src, int n) {
        return () -> new Limiter<>(src.iterator(), n);
    }

    static class Limiter<T> implements Iterator<T> {
        final Iterator<T> iter;
        int n;
        public Limiter(Iterator<T> iter, int n) {
            this.iter = iter;
            this.n = n;
        }
        public boolean hasNext() {
            return n > 0 ? iter.hasNext() : false;
        }
        public T next() {
            if(n-- < 0) throw new IllegalStateException();
            return iter.next();
        }
    }
    /**
     * Returns a sequence consisting of the remaining elements of src sequence
     * after discarding the first n elements of the sequence.
     */
    public static <T> Iterable<T> skip(Iterable<T> src, int n) {
        return () -> {
            Iterator<T> iter = src.iterator();
            int count = n;
            while(iter.hasNext() && count-- > 0) iter.next();
            return iter;
        };
    }


    /**
     * Returns the maximum element of the src Iterable according
     * to the provided Comparator.
     */
    public static <T> Optional<T> max(Iterable<T> src, Comparator<T> cmp) {
        Optional<T> res = Optional.empty();
        for (T item : src) {
            if(res.isPresent() == false)
                res = Optional.of(item);
            else if(cmp.compare(item, res.get()) > 0)
                res = Optional.of(item);
        }
        return res;
    }

}
