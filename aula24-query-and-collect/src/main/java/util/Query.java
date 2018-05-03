package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
        // !!!!! Implementação Errada !!!!!
        return cons -> this.tryAdvance(item -> {
            if (pred.test(item)) cons.accept(item);
        });
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
}
