import org.junit.jupiter.api.Test;
import util.*;
import weather.WeatherService;
import weather.model.Location;
import weather.model.WeatherInfo;

import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherServiceTest {
    @Test
    public void testSearch() {
        // <=> WeatherService weather = new WeatherService(new WeatherWebApi(new FileRequest()));
        WeatherService weather = new WeatherService(new FileRequest());

        // search => map(filter(req.getConstent())))
        Stream<Location> locations = weather.search("Oporto");
        locations.forEach(System.out::println);
    }

    @Test
    public void testSearchAndPastWeather() {
        Function<String, Stream<String>> http = new HttpRequest()::getContent;
        Function<String, String> logger = arg -> {
            System.out.println("HTTP Get... " + arg);
            return arg;
        };
        Function<String, Stream<String>> req = logger.andThen(http);
        // <=> Function<String, Iterable<String>> req = http.thenBy(logger);


        // Function<String, Iterable<String>> logger =
        //        Loggify.of(http::getContent, "HTTP Get... ");
        Countify.Counter<String, Stream<String>> counter =
                Countify.of(req);
        WeatherService weather = new WeatherService(counter::apply);
        System.out.println("Get Faro Location...");
        Location faro = weather.search("Faro").iterator().next();
        assertEquals(1, counter.getCount());
        System.out.println("Get past weather for Faro...");
        Stream<WeatherInfo> past = faro.past30DaysWeather();
        assertEquals(2, counter.getCount());
        past.forEach(System.out::println);
    }
    static class Logger implements IRequest {
        final String msg;
        final IRequest req;
        public Logger(String msg, IRequest req) {
            this.msg = msg;
            this.req = req;
        }
        @Override
        public Stream<String> getContent(String path) {
            System.out.println(msg);
            return req.getContent(path);
        }
    }

    static class Counter implements IRequest {
        int count;
        final IRequest req;
        public Counter(IRequest req) {
            this.req = req;
        }
        public int getCount() {
            return count;
        }
        @Override
        public Stream<String> getContent(String path) {
            count++;
            return req.getContent(path);
        }
    }

}
