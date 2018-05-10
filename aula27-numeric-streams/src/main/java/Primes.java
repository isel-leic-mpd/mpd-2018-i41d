import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class Primes {
    public static IntStream primes(int max) {
        return IntStream
                .range(2, max + 1)
                .filter(candidate -> isPrimeStream(candidate));
    }

    public static IntStream primes(int max, BiFunction<List<Integer>, Integer, Boolean> isPrime) {
        List<Integer> prs = new ArrayList<>();
        return IntStream
                .range(2, max + 1)
                .filter(candidate -> isPrime.apply(prs, candidate));
    }
    /**
     * Version 4
     * Functional version of is prime algorithm with Stream.
     * Receives an additional List of already selected primes.
     * And iterate until Square root of candidate.
     */
    public static boolean isPrimeOpt2(List<Integer> primes, int candidate) {
        int root = (int) Math.sqrt(candidate);
        boolean notPrime = primes
                .stream()
                .takeWhile(div -> div <= root)
                .anyMatch(div -> candidate%div == 0);
        if(!notPrime) primes.add(candidate);
        return !notPrime;
    }

    /**
     * Version 3
     * Functional version of is prime algorithm with Stream.
     * Receives an additional List of already selected primes.
     */
    public static boolean isPrimeOpt(List<Integer> primes, int candidate) {
        boolean notPrime = primes
                .stream()
                .anyMatch(div -> candidate%div == 0);
        if(!notPrime) primes.add(candidate);
        return !notPrime;
    }
    /**
     * Version 2
     * Functional version of is prime algorithm with Stream.
     */
    public static boolean isPrimeStream(int candidate) {
        /*
        return IntStream
                .range(2, candidate)
                .filter(div -> candidate%div == 0)
                .count() == 0;
        */
        boolean notPrime = IntStream
                .range(2, candidate)
                .anyMatch(div -> candidate%div == 0);
        return !notPrime;
    }

    /**
     * Version 1
     * Iterative version of is prime algorithm.
     */
    public static boolean isPrimeImperative(int candidate) {
        for (int div = 2; div < candidate; div++) {
            if(candidate%div == 0) return false;
        }
        return true;
    }
}
