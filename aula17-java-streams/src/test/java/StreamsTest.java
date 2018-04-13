import org.junit.jupiter.api.Test;
import util.Cmp;
import util.FileRequest;
import weather.WeatherService;
import weather.model.WeatherInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static java.util.Arrays.asList;
import static java.util.stream.Stream.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class StreamsTest {
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
        past.forEach(System.out::println);

    }

    @Test
    public void testGenerator() {
        generate(Math::random)
                .limit(7)
                .forEach(System.out::println);
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
                    System.out.println("Filtering .... " + w);
                    return w.getDescription().contains("cloud");
                })
                .map(w -> {
                    System.out.println("Mapping.... " + w.getTempC());
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
