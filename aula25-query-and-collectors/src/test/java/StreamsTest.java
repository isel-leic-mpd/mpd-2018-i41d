import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Cmp;
import util.FileRequest;
import util.Queries;
import util.Query;
import weather.WeatherService;
import weather.model.WeatherInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.time.LocalDate.of;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static util.Queries.collapse;

public class StreamsTest {


    @Test
    public void testCollectWithForEach() {
        IntStream nrs = new Random().ints(1, 10).limit(7);
        List<Integer> lst = new ArrayList<>();
        nrs.forEach(lst::add); // !!! side-effects
        System.out.println(lst);
    }

    @Test
    public void testCollectWithReduce() {
        Stream<Integer> nrs = new Random().ints(1, 10).limit(1000000).boxed();
        List<Integer> lst = nrs.parallel().reduce(
                new ArrayList<Integer>(),
                (l, curr) -> {
                    l.add(curr); // !!!! Mutação de estado => Inconsistencia
                    return l;
                }, // accumulator
                (l1, l2) -> l1);// combiner => for parallel proposes
        System.out.println(lst);
        System.out.println(lst.size());
    }

    @Test
    public void testCollectWithCollect() {
        Stream<Integer> nrs = new Random().ints(1, 10).limit(1000000).boxed();
        /*
        List<Integer> lst = nrs.parallel().collect(
                () -> new ArrayList<Integer>(),
                (l, curr) -> l.add(curr), // accumulator
                (l1, l2) -> l1.addAll(l2));// combiner => for parallel proposes
        */
        List<Integer> lst = nrs.parallel().collect(
                ArrayList::new,
                ArrayList::add,
                ArrayList::addAll);
        System.out.println(lst);
        System.out.println(lst.size());
    }

    @Test
    public void testCollectWithCollector() {
        Collector<Integer, List<Integer>, List<Integer>> listCol =
            new Collector<Integer, List<Integer>, List<Integer>>() {
                public Supplier<List<Integer>> supplier() { return ArrayList::new; }
                public BiConsumer<List<Integer>, Integer> accumulator() {
                    return List::add;
                }
                public BinaryOperator<List<Integer>> combiner() { return (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                };}
                public Function<List<Integer>, List<Integer>> finisher() { return identity(); }
                public Set<Characteristics> characteristics() {
                    HashSet<Characteristics> charact = new HashSet<>();
                    charact.add(Characteristics.IDENTITY_FINISH);
                    return charact;
                }
            };
        Stream<Integer> nrs = new Random().ints(1, 10).limit(1000000).boxed();
        List<Integer> lst = nrs.parallel().collect(listCol);
        // List<Integer> lst = nrs.parallel().collect( toList());
        // System.out.println(lst);
        System.out.println(lst.size());
        // System.out.println(lst);
    }

    class Joiner<T> implements Collector<T, StringBuilder, String> {
        public Supplier<StringBuilder> supplier() { return StringBuilder::new; }
        public BiConsumer<StringBuilder, T> accumulator() {
            return (bldr, item) ->  {
                if(bldr.length() != 0) bldr.append(",");
                bldr.append(item);
            };
        }
        public BinaryOperator<StringBuilder> combiner() {
            return (bldr1, bldr2) ->  bldr1.append(",").append(bldr2);
        }
        public Function<StringBuilder, String> finisher() {
            return StringBuilder::toString;
        }
        public Set<Characteristics> characteristics() {
            HashSet<Characteristics> charact = new HashSet<>();
            charact.add(Characteristics.CONCURRENT);
            return charact;
        }
    }

    @Test
    public void testJoiningWithCollector() {
        Stream<Integer> nrs = new Random().ints(1, 10).limit(1000000).boxed();
        // String res = nrs.parallel().collect(new Joiner<>());
        String res = nrs.map(Object::toString).parallel().collect(Collectors.joining(","));
        System.out.println(res);
    }


    @Test
    public void testCollapseStream() {
        Stream<Integer> nrs = Stream.of(7, 7, 11, 12, 7, 11, 11, 8, 8, 8);
        Iterable<Integer> actual = () -> collapse(nrs).iterator();
        Iterable<Integer> expected = Arrays.asList(7, 11, 12, 7, 11, 8);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void testExternalIteration() {
        WeatherService weather = new WeatherService(new FileRequest());
        Stream<String> descs = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30))
                .map(WeatherInfo::getDescription)
                .distinct();
        StringBuilder res = new StringBuilder();
        for(Iterator<String> iter = descs.iterator(); iter.hasNext(); ){
            String item = iter.next(); // ASK for an item
            res.append(item);
            res.append(',');
        }
        out.println(res);
        assertEquals(
                "Moderate rain,Moderate or heavy rain shower,Patchy rain possible,Light drizzle,Partly cloudy,Overcast,Light rain shower,Sunny,Cloudy,Light rain,Patchy light rain with thunder,",
                res.toString());
    }

    @Test
    public void testInternalIteration() {
        WeatherService weather = new WeatherService(new FileRequest());
        Stream<String> descs = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30))
                .map(WeatherInfo::getDescription)
                .distinct();
        StringBuilder res = new StringBuilder();
        Spliterator<String> iter = descs.spliterator();
        while(iter.tryAdvance(item -> res.append(item + ","))) {}
        out.println(res);
        assertEquals(
                "Moderate rain,Moderate or heavy rain shower,Patchy rain possible,Light drizzle,Partly cloudy,Overcast,Light rain shower,Sunny,Cloudy,Light rain,Patchy light rain with thunder,",
                res.toString());
    }

    @Test
    public void testSomeThing() {
        // WeatherWebApi weather = new WeatherWebApi(new HttpRequest());
        WeatherService weather = new WeatherService(new FileRequest());
        Stream<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));

        // for(Iterator<WeatherInfoDto> iter = past.iteratosr(); iter.hasNext(); )
        //     System.out.println(iter.next());
        //
        // <=>  for (WeatherInfoDto w : past) { System.out.println(w); }
        // <=>
        past.forEach(out::println);

    }

    @Test
    public void testGenerator() {
        generate(Math::random)
                .limit(7)
                .forEach(out::println);
    }

    @Test
    public void testFilter() {
        WeatherService weather = new WeatherService(new FileRequest());
        WeatherInfo[] past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30))
                .toArray(size -> new WeatherInfo[size]);
        assertEquals(31, Stream.of(past).filter(w -> w.getDescription().contains("Sunny")).count());
        assertEquals(37, Stream.of(past).filter(w -> w.getDescription().contains("rain")).count());
        assertEquals(10, Stream.of(past).filter(w -> w.getDescription().contains("cloud")).count());
        assertEquals(43, Stream.of(past).filter(w -> w.getTempC() > 18).count());
        assertEquals(42, Stream.of(past).filter(w -> w.getPrecipMM() ==  0).count());
        assertEquals(0, Stream.of(past).filter(w -> w.getTempC() <= 0).count());
    }

    @Test
    public void testMap() {
        WeatherService weather = new WeatherService(new FileRequest());
        Stream<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        Stream<Integer> temps = past
                .filter(w -> {
                    out.println("Filtering .... " + w);
                    return w.getDescription().contains("cloud");
                })
                .map(w -> {
                    out.println("Mapping.... " + w.getTempC());
                    return w.getTempC();
                });
        Integer[] expected = {14, 15, 17, 25, 16, 19, 25, 24, 22, 18};
        assertArrayEquals(expected, temps.toArray(size -> new Integer[size]));
    }

    @Test
    public void testReduceMax() {
        WeatherService weather = new WeatherService(new FileRequest());
        Stream<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        int max = past
                .filter(w -> w.getDescription().contains("cloud"))
                .map(WeatherInfo::getTempC)
                .reduce(
                        Integer.MIN_VALUE,
                        (prev, t) -> prev >= t ? prev : t);
        assertEquals(25, max);
    }

    @Test
    public void testMax() {
        WeatherService weather = new WeatherService(new FileRequest());
        WeatherInfo[] past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30))
                .toArray(size -> new WeatherInfo[size]);
        Optional<WeatherInfo> max = Stream.of(past).max(
                // <=> (prev, w) -> prev.getTempC() - w.getTempC()
                Cmp
                        .comparing(WeatherInfo::getTempC)
                        .thenBy(WeatherInfo::getDate)
                        // <=> .thenComparing(WeatherInfo::getDate));
        );
        assertEquals(LocalDate.of(2017,4,12), max.get().getDate());

        Optional<WeatherInfo> maxPrecip = Stream.of(past).max(
                Cmp.comparing(WeatherInfo::getPrecipMM));
        assertEquals(44.4, maxPrecip.get().getPrecipMM());
    }


    @Test
    public void testReduceCount() {
        WeatherService weather = new WeatherService(new FileRequest());
        Stream<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        int size = past
                .map(WeatherInfo::getTempC)
                .reduce(0, (prev, w) -> ++prev);
        assertEquals(89, size);
    }
}
