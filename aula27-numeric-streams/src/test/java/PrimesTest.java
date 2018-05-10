import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

public class PrimesTest {

    @Test
    public void testPrimes() {
        for (int i = 0; i < 5; i++) {
            System.out.println("############################");
            // System.out.println(measurePerformance(() -> Primes.primes(10000).count()));
            System.out.println(measurePerformance(() -> Primes.primes(10000, Primes::isPrimeOpt).count()));
            System.out.println(measurePerformance(() -> Primes.primes(10000, Primes::isPrimeOpt2).count()));
        }
    }

    /**
     * !!!! Do not Use for reliable performance tests.
     * For accurate micro benchmarking you must use JMH!!!!
     */
    private static <T> T measurePerformance(Supplier<T> action) {
        T val = null;
        long min = Long.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            long init = System.nanoTime();
            val = action.get();
            long dur = System.nanoTime() - init;
            if(dur < min) min = dur;
        }
        min = min / 1000;
        System.out.println("Duration = " + min + "us");
        return val;
    }
}
