import org.junit.jupiter.api.Test;
import util.FileRequest;
import util.HttpRequest;
import util.Queries;
import weather.WeatherWebApi;
import weather.dto.Location;
import weather.dto.WeatherInfo;

import static java.time.LocalDate.of;

public class WeatherWebApiTest {

    @Test
    public void testSearch() {
        // WeatherWebApi weather = new WeatherWebApi(new HttpRequest());
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        // search => map(filter(req.getConstent())))
        Iterable<Location> locations = weather.search("Oporto");
        Queries.forEach(locations, System.out::println);

    }

}
