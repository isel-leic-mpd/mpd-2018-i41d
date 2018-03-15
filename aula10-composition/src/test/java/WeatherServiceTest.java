import org.junit.jupiter.api.Test;
import util.*;
import weather.WeatherService;
import weather.model.Location;
import weather.model.WeatherInfo;

import java.util.function.Function;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherServiceTest {
    @Test
    public void testSearch() {
        // <=> WeatherService weather = new WeatherService(new WeatherWebApi(new FileRequest()));
        WeatherService weather = new WeatherService(new FileRequest());

        // search => map(filter(req.getConstent())))
        Iterable<Location> locations = weather.search("Oporto");
        Queries.forEach(locations, System.out::println);
    }

    @Test
    public void testSearchAndPastWeather() {
        HttpRequest http = new HttpRequest();
        /*
        IRequest logger =  (path) -> {
            System.out.println("HTTP Get (lambda)...");
            return http.getContent(path);
        };
        */ // <=>
        Function<String, Iterable<String>> logger = Loggify.of(http::getContent, "HTTP Get... ");
        // <=> Counter c = new Counter(new Logger("HTTP Get...", new HttpRequest()));
        /*
        int[] counter = {0};
        IRequest req = (path) -> {
            counter[0]++;
            return logger.apply(path);
        };
        */ // <=>
        Countify.Counter<String, Iterable<String>> counter = Countify.of(logger);
        WeatherService weather = new WeatherService(counter::apply);
        System.out.println("Get Faro Location...");
        Location faro = weather.search("Faro").iterator().next();
        assertEquals(1, counter.getCount());
        System.out.println("Get past weather for Faro...");
        Iterable<WeatherInfo> past = faro.past30DaysWeather();
        assertEquals(2, counter.getCount());
        Queries.forEach(past, System.out::println);
    }
    static class Logger implements IRequest {
        final String msg;
        final IRequest req;
        public Logger(String msg, IRequest req) {
            this.msg = msg;
            this.req = req;
        }
        @Override
        public Iterable<String> getContent(String path) {
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
        public Iterable<String> getContent(String path) {
            count++;
            return req.getContent(path);
        }
    }

}
