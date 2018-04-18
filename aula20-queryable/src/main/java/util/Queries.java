package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.DISTINCT;
import static java.util.stream.StreamSupport.stream;
import static util.Box.empty;

public class Queries {

    public static <T> Stream<T> collapse(Stream<T> src) {
        /*
        Iterable<T> res = collapse(() -> src.iterator());
        Spliterator<T> resIter = res.spliterator(); // !!!!! Overhead
        return StreamSupport.stream(resIter, false);
        */
        return stream(new CollapseSpliterator<>(src.spliterator()), false);
    }

    public static <T> Iterable<T> collapse(Iterable<T> src) {
        return () -> new CollapseIterator(src.iterator());
    }

    static class CollapseIterator<T> implements Iterator<T>{
        final Iterator<T> src;
        T prev;
        boolean advance = true;

        public CollapseIterator(Iterator<T> src) {
            this.src = src;
        }

        @Override
        public boolean hasNext() {
            if(advance) {
                advance = false;
                while(src.hasNext()){
                    T curr = src.next();
                    if(!curr.equals(prev)) {
                        prev = curr;
                        return true;
                    }
                }
                return false;
            }
            return prev != null;
        }

        @Override
        public T next() {
            if(!hasNext()) throw new NoSuchElementException();
            advance = true;
            return prev;
        }
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
