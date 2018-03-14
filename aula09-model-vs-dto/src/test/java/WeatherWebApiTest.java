import org.junit.jupiter.api.Test;
import util.FileRequest;
import util.Queries;
import weather.WeatherService;
import weather.model.Location;

import static java.time.LocalDate.of;

public class WeatherWebApiTest {

    @Test
    public void testSearch() {
        // WeatherWebApi weather = new WeatherWebApi(new HttpRequest());
        WeatherService weather = new WeatherService(new FileRequest());
        // search => map(filter(req.getConstent())))
        Iterable<Location> locations = weather.search("Oporto");
        Queries.forEach(locations, System.out::println);

    }

}
