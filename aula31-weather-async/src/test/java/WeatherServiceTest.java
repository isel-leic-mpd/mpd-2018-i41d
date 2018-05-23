import org.junit.jupiter.api.Test;
import util.*;
import weather.WeatherService;
import weather.WeatherWebApi;
import weather.model.Location;
import weather.model.WeatherInfo;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherServiceTest {
    @Test
    public void testSearch() {
        // <=> WeatherService weather = new WeatherService(new WeatherWebApi(new FileRequest()));
        WeatherService weather = new WeatherService(new FileRequest());

        // search => map(filter(req.getConstent())))
        Stream<Location> locations = weather.search("Oporto").join();
        locations.forEach(System.out::println);
    }

    @Test
    public void testPastWeatherGroupingBy() {
        WeatherService weather = new WeatherService(new WeatherWebApi(new FileRequest()));
        Map<String, Long> oporto = weather
                .search("Oporto")
                .join()
                .findFirst()
                .get()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30))
                .join()
                .collect(groupingBy(WeatherInfo::getDescription, Collectors.counting()));
        oporto.forEach((desc, lst) -> System.out.println(desc + ": " + lst));
    }

    @Test
    public void testPastWeatherGroupingByAndThen() {
        WeatherService weather = new WeatherService(new FileRequest());
        DecimalFormat df = new DecimalFormat("#.##");
        Map<String, String> oporto = weather
                .search("Oporto")
                .join()
                .findFirst()
                .get()
                .pastWeather(of(2017, 2, 1), of(2017, 4, 30))
                .join()
                .collect(groupingBy(
                        WeatherInfo::getDescription,
                        // mapping(WeatherInfo::getTempC, toList())));
                        collectingAndThen(averagingInt(WeatherInfo::getTempC), df::format)));
        oporto.forEach((desc, lst) -> System.out.println(desc + ": " + lst));
    }

    @Test
    public void testSearchAndPastWeather() {
        Function<String, CompletableFuture<String>> http = new HttpRequest()::getContent;
        Function<String, String> logger = arg -> {
            System.out.println("HTTP Get... " + arg);
            return arg;
        };
        Function<String, CompletableFuture<String>> req = logger.andThen(http);
        // <=> Function<String, Iterable<String>> req = http.thenBy(logger);


        // Function<String, Iterable<String>> logger =
        //        Loggify.of(http::getContent, "HTTP Get... ");
        Countify.Counter<String, CompletableFuture<String>> counter =
                Countify.of(req);
        WeatherService weather = new WeatherService(counter::apply);
        System.out.println("Get Faro Location...");
        Location faro = weather.search("Faro").join().iterator().next();
        assertEquals(1, counter.getCount());
        System.out.println("Get past weather for Faro...");
        Stream<WeatherInfo> past = faro.past30DaysWeather().join();
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
        public CompletableFuture<String> getContent(String path) {
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
        public CompletableFuture<String> getContent(String path) {
            count++;
            return req.getContent(path);
        }
    }

}
