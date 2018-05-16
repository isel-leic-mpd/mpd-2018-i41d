package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.StreamSupport.stream;
import static util.Box.empty;

/**
 * Definition of a sequence based on a single method iteration:
 * tryAdvance(Consumer<T>)
 */
public interface Query<T> {

    public boolean tryAdvance(Consumer<? super T> action);

    public static <T> Query<T> of(Stream<T> src) {
        // return cons -> src.spliterator().tryAdvance(cons); // !!!!! MAL
        return src.spliterator()::tryAdvance;
    }

    public static <T> Query<T> of(T...values) {
        int[] index = {0};
        return cons -> {
            if(index[0] < values.length)
                cons.accept(values[index[0]]);
            return index[0]++ < values.length;
        };
    }

    public static <T> Query<T> generate(Supplier<T> generator) {
        return cons -> {
            cons.accept(generator.get());
            return true;
        };
    }

    public default <R> Query<R> map(Function<T,R> mapper) {
        return cons -> this.tryAdvance(item -> cons.accept(mapper.apply(item)));
    }

    public default Query<T> filter(Predicate<T> pred) {
        return cons -> {
            boolean[] found = {false};
            while(!found[0] && this.tryAdvance(item -> {
                if (pred.test(item)) {
                    cons.accept(item);
                    found[0] = true;
                }
            })){}
            return found[0];
        };
    }

    public default Query<T> skip(int size) {
        int[] s = {size};
        return cons -> {
            while((s[0]--) > 0) tryAdvance((item)-> {});
            return tryAdvance(cons);
        };
    }
    public default Query<T> oddLines() {
        return cons -> tryAdvance((item) ->{}) && tryAdvance(cons);
    }

    public default Query<T> collapse() {
        Box<T> prev = empty();
        return action -> {
            Box<T> curr = empty();
            while(this.tryAdvance(item -> {
                curr.set(item);
            }) && curr.getItem().equals(prev.getItem())) { }
            if(curr.isPresent() && !curr.getItem().equals(prev.getItem())) {
                prev.set(curr.getItem());
                action.accept(curr.getItem()); // <=> return curr;
                return true;
            }
            return false;
        };
    }

    public default Query<T> limit(int size) {
        int count[] = {0};
        return cons -> {
            if(count[0] >= size) return false;
            count[0]++;
            return this.tryAdvance(cons::accept);
        };
    }

    public default Object[] toArray() {
        List<T> res = new ArrayList<>();
        while(this.tryAdvance(res::add)){}
        return res.toArray();
    }

    public default T[] toArray(IntFunction<T[]> factory) {
        List<T> res = new ArrayList<>();
        while(this.tryAdvance(res::add)){}
        return res.toArray(factory.apply(res.size()));
    }

    public default Stream<T> toStream(){
        return stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return Query.this.tryAdvance(action);
            }
        }, false);
    }
}
