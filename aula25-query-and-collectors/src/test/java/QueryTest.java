import org.junit.jupiter.api.Test;
import util.Query;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.stream.Stream;


public class QueryTest {

    @Test
    public void testTPCSkipIterator() {
        Query<Integer> nrs = Query.of(1, 2, 3, 1, -2, 3).skip(1);
        int []actual = {Integer.MIN_VALUE};
        nrs.tryAdvance(item -> actual[0] = item);
        assertEquals(2, actual[0]);
    }

    @Test
    public void testTPCSkip() {
        Query<Integer> nrs = Query.of(1, 2, 3, 1, -2, 3).skip(1);
        assertArrayEquals(new Object[]{2, 3, 1, -2, 3},nrs.toArray());

        Query<Integer> nrs1 = Query.of(1, 2, 3, 1, -2, 3).skip(0);
        assertArrayEquals(new Object[]{1, 2, 3, 1, -2, 3},nrs1.toArray());

        Query<Integer> nrs2 = Query.of(1, 2, 3, 1, -2, 3).skip(6);
        assertArrayEquals(new Object[]{},nrs2.toArray());
    }

    @Test
    public void testTPCOddLines() {
        Query<Integer> nrs = Query.of(1, 2, 3, 1, -2, 3).oddLines();
        assertArrayEquals(new Object[]{2, 1, 3},nrs.toArray());

        Query<Integer> nrs1 = Query.of(1, 2).oddLines();
        assertArrayEquals(new Object[]{2},nrs1.toArray());

        Query<Integer> nrs2 = Query.of(1).oddLines();
        assertArrayEquals(new Object[]{},nrs2.toArray());
    }

    @Test
    public void testTPCFilter() {
        Integer[] expected = {1, 2, 3, 1, -2, 3};
        Object[] actual = Query.of(15, 1, 2, 3, 4, 8, 23, 41, 1, -2, 50, 3)
                .filter(num -> num <= 3)
                .toArray();
        assertArrayEquals(expected, actual);

        String[] exp = {"Hello", "House"};
        Object[] act = Query.of(Stream.of("Hello", "Welcome", "To", "My", "House", "honey"))
                .filter(s -> s.contains("H"))
                .toArray();
        assertArrayEquals(exp, act);
    }

    @Test
    public void testFilterAdvanceOnce() {
        Query<Integer> nrs = Query
                .of(15, 1, 2, 3, 4, 8, 23, 41, 1, -2, 50, 3)
                .filter(num -> num <= 3);
        int[] actual = {Integer.MAX_VALUE};
        nrs.tryAdvance(n -> actual[0] = n);
        assertEquals(1, actual[0]);
    }

    @Test
    public void testQueryMapOfStream() {
        String[] expected = {"1","2","4","7","7","4","32","2","23"};
        Object[] actual = Query.of(
                Stream.of(1,2,4,7,7,4,32,2,23))
                .map(Object::toString)
                .toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testQueryMapOfInifinte() {
        String[] expected = {"7","8","9","10","11"};
        int count[] = {7};
        Object[] actual = Query
                .generate(() -> count[0]++) // 7,8,9,...
                .map(Object::toString)      // "7","8","9",...
                .limit(5)                   // "7","8","9","10","11"
                .toArray();// ["7","8","9","10","11"]
        assertArrayEquals(expected, actual);
    }

}