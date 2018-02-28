import org.junit.Test;
import util.FileRequest;
import util.HttpRequest;
import weather.WeatherWebApi;
import weather.dto.WeatherInfo;

import java.time.LocalDate;

import static java.time.LocalDate.of;
import static org.junit.Assert.*;

public class QueriesTest {
    @Test public void testSomeThing() {
        WeatherWebApi weather = new WeatherWebApi(new HttpRequest());
        // WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Iterable<WeatherInfo> past = weather
                .pastWeather(41.15, -8.6167, of(2017, 2, 1), of(2017, 4, 30));
        past.forEach(System.out::println);

    }
}
