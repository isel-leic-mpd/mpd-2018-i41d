import org.junit.Test;
import util.FileRequest;
import weather.WeatherQueries;
import weather.WeatherWebApi;
import weather.dto.WeatherInfo;

import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.Assert.assertEquals;
import static weather.WeatherQueries.count;
import static weather.WeatherQueries.filter;
import static weather.WeatherQueries.filterByDescription;
import static weather.WeatherQueries.map;
import static weather.WeatherQueries.reduce;

public class WeatherQueriesTest {
    @Test
    public void testSomeThing() {
        // WeatherWebApi weather = new WeatherWebApi(new HttpRequest());
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        for (WeatherInfo w : past) {
            System.out.println(w);
        }
        // for(Iterator<WeatherInfo> iter = past.iterator(); iter.hasNext(); )
        //     System.out.println(iter.next());
    }

    @Test
    public void testFilterSunnyDays() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        Iterable<WeatherInfo> sunnies = WeatherQueries.filterSunnyDays(past);
        assertEquals(31, count(sunnies));
    }

    @Test
    public void testFilterRainnyDays() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        Iterable<WeatherInfo> rainny = WeatherQueries.filterRainnyDays(past);
        assertEquals(37, count(rainny));
    }

    @Test
    public void testFilterByDescription() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        assertEquals(31, count(filterByDescription(past, "Sunny")));
        assertEquals(37, count(filterByDescription(past, "rain")));
        assertEquals(10, count(filterByDescription(past, "cloud")));
    }

    @Test
    public void testFilter() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        assertEquals(31, count(filter(past, w -> w.getDescription().contains("Sunny"))));
        assertEquals(37, count(filter(past, w -> w.getDescription().contains("rain"))));
        assertEquals(10, count(filter(past, w -> w.getDescription().contains("cloud"))));
        assertEquals(43, count(filter(past, w -> w.getTempC() > 18)));
        assertEquals(42, count(filter(past, w -> w.getPrecipMM() ==  0)));
        assertEquals(0, count(filter(past, w -> w.getTempC() <= 0)));
    }

    @Test
    public void testMap() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        Iterable<WeatherInfo> cloudy = filter(past, w -> w.getDescription().contains("cloud"));
        Iterable<Integer> temps = map(cloudy, w -> w.getTempC());
        List<Integer> expected = Arrays.asList(14, 15, 17, 25, 16, 19, 25, 24, 22, 18);
        assertEquals(expected, temps);
    }

    @Test
    public void testReduceMax() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        Iterable<WeatherInfo> cloudy = filter(past, w -> w.getDescription().contains("cloud"));
        int max = reduce(
                cloudy,
                Integer.MIN_VALUE,
                (prev, w) -> prev >= w.getTempC() ? prev : w.getTempC());
        assertEquals(25, max);
    }

    @Test
    public void testReduceCount() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        int size = reduce(past,0, (prev, w) -> ++prev);
        assertEquals(89, size);
    }
}
