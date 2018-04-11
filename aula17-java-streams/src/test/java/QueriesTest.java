import org.junit.jupiter.api.Test;
import util.Cmp;
import util.FileRequest;
import util.Queries;
import weather.WeatherService;
import weather.model.WeatherInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.time.LocalDate.of;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static util.Queries.count;
import static util.Queries.filter;
import static util.Queries.forEach;
import static util.Queries.generate;
import static util.Queries.limit;
import static util.Queries.map;
import static util.Queries.reduce;

public class QueriesTest {
    @Test
    public void testSomeThing() {
        // WeatherWebApi weather = new WeatherWebApi(new HttpRequest());
        WeatherService weather = new WeatherService(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));

        // for(Iterator<WeatherInfoDto> iter = past.iteratosr(); iter.hasNext(); )
        //     System.out.println(iter.next());
        //
        // <=>  for (WeatherInfoDto w : past) { System.out.println(w); }
        // <=>
        Queries.forEach(past, System.out::println);

    }



    @Test
    public void testGenerator() {
        Iterable<Double> nrs = limit(generate(Math::random), 7);
        forEach(nrs, System.out::println);
    }

    @Test
    public void testFilter() {
        WeatherService weather = new WeatherService(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        assertEquals(31, count(filter(past, w -> w.getDescription().contains("Sunny"))));
        assertEquals(37, count(filter(past, w -> w.getDescription().contains("rain"))));
        assertEquals(10, count(filter(past, w -> w.getDescription().contains("cloud"))));
        assertEquals(43, count(filter(past, w -> w.getTempC() > 18)));
        assertEquals(42, count(filter(past, w -> w.getPrecipMM() ==  0)));
        assertEquals(0, count(filter(past, w -> w.getTempC() <= 0)));
    }

    @Test
    public void testFilterWithNullElements() {
        // Arrange
        Iterable<String> strs = asList("ola", "super", null, "abc", null, "1234");
        Predicate<String> p = s -> s == null || s.length() == 3;
        Iterable<String> expected = asList("ola", null, "abc", null);
        // Act
        Iterable<String> actual = filter(strs, p);
        // Assert
        assertIterableEquals(expected, actual);
    }

    @Test
    public void testMap() {
        WeatherService weather = new WeatherService(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        Iterable<WeatherInfo> cloudy = filter(past, w -> {
            System.out.println("Filtering .... " + w);
            return w.getDescription().contains("cloud");
        });
        Iterable<Integer> temps = map(cloudy, w -> {
            System.out.println("Mapping.... " + w.getTempC());
            return w.getTempC();
        });
        List<Integer> expected = asList(14, 15, 17, 25, 16, 19, 25, 24, 22, 18);
        assertIterableEquals(expected, temps);
    }

    @Test
    public void testReduceMax() {
        WeatherService weather = new WeatherService(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        Iterable<WeatherInfo> cloudy = filter(past, w -> w.getDescription().contains("cloud"));
        int max = reduce(
                cloudy,
                Integer.MIN_VALUE,
                (prev, w) -> prev >= w.getTempC() ? prev : w.getTempC());
        assertEquals(25, max);
    }

    @Test
    public void testMax() {
        WeatherService weather = new WeatherService(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        Optional<WeatherInfo> max = Queries.max(
                past,
                // <=> (prev, w) -> prev.getTempC() - w.getTempC()
                Cmp
                        .comparing(WeatherInfo::getTempC)
                        .thenBy(WeatherInfo::getDate)
                        // <=> .thenComparing(WeatherInfo::getDate));
        );
        assertEquals(LocalDate.of(2017,4,12), max.get().getDate());

        Optional<WeatherInfo> maxPrecip = Queries.max(
                past,
                Cmp.comparing(WeatherInfo::getPrecipMM));
        assertEquals(44.4, maxPrecip.get().getPrecipMM());
    }


    @Test
    public void testReduceCount() {
        WeatherService weather = new WeatherService(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .search("Oporto")
                .iterator()
                .next()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30));
        int size = reduce(past,0, (prev, w) -> ++prev);
        assertEquals(89, size);
    }
}
