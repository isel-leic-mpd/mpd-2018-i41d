package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;
import static util.Box.empty;

public class Queries {

    public static <T> Stream<T> oddLines(Stream<T> src){
        Spliterator<T> iter = src.spliterator();
        long size = iter.estimateSize()%2 == 0
                ? iter.estimateSize()/2
                : iter.estimateSize()/2 + 1;
        return  stream(new OddLinesSpliterator<>(size, iter),false); }

    static class OddLinesSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
        final Spliterator<T> src;

        public OddLinesSpliterator(long size, Spliterator<T> src) {
            super(size, src.characteristics());
            this.src = src;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if(!src.tryAdvance((item)->{}))
                return false;
            return  src.tryAdvance((item)-> action.accept(item));
        }
    }

    public static <T> Stream<T> collapse(Stream<T> src) {
        return stream(new CollapseSpliterator<>(src.spliterator()), false);
    }

    static class CollapseSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
        final Spliterator<T> src;
        T prev;

        public CollapseSpliterator(Spliterator<T> src) {
            super(src.estimateSize(), DISTINCT | SIZED | NONNULL | ORDERED); // != SORTED
            this.src = src;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            Box<T> curr = empty();
            while(src.tryAdvance(item -> {
                curr.set(item);
            }) && curr.getItem().equals(prev)) { }
            if(curr.isPresent() && !curr.getItem().equals(prev)) {
                action.accept(prev = curr.getItem()); // <=> return curr;
                return true;
            }
            return false;
        }
    }
}
