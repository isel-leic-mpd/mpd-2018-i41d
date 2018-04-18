package util;

/**
 * <=> java.util.Optional
 */
public class Box<T> {
    private boolean isPresent;
    private T item;

    public Box(T item) {
        this.isPresent = true;
        this.item = item;
    }

    public Box() {
        this.isPresent = false;
        this.item = null;
    }

    public static <T> Box<T> of(T item) {
        return new Box<>(item);
    }
    public static <T> Box<T> empty() {
        return new Box<>();
    }

    public boolean isPresent() {
        return isPresent;
    }

    public T getItem() {
        return item;
    }

    public void set(T item) {
        this.item = item;
        isPresent = true;
    }
}
