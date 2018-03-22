import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import util.HttpRequest;
import util.Queries;
import weather.WeatherApi;
import weather.WeatherRestApi;
import weather.WeatherService;
import weather.dto.WeatherInfoDto;
import weather.model.WeatherInfo;
import weather.restdto.PastWeatherDataWeatherDto;
import weather.restdto.PastWeatherDataWeatherHourlyDto;
import weather.restdto.PastWeatherDto;

import java.time.LocalDate;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Queries.count;
import static util.Queries.reduce;
import static util.Queries.skip;

public class WeatherRestTest {

    @Test
    public void testPastWeatherDtos() {
        HttpRequest req = new HttpRequest();
        Iterable<String> body = req.getContent("http://api.worldweatheronline.com/premium/v1/past-weather.ashx?q=37.017,-7.933&date=2018-02-19&enddate=2018-03-21&tp=24&format=json&key=715b185b36034a4c879141841182802");
        String json = reduce(body, "", (prev, curr) -> prev + curr);
        Class<PastWeatherDto> dtoClass = PastWeatherDto.class;// <=> typeof(PastWeatherDto)
        Gson gson = new Gson();
        PastWeatherDto dto = gson.fromJson(json, dtoClass);
        assertEquals(31, dto.getData().getWeather().length);
        PastWeatherDataWeatherHourlyDto w = dto.getData().getWeather()[30].getHourly()[0];
        assertEquals(14, w.getTempC());
        assertEquals(0, w.getPrecipMM());
        assertEquals(9, w.getFeelsLikeC());
        assertEquals("Sunny", w.getWeatherDesc());

    }

    @Test
    public void testWeatherRestApiPastWeather() {
        WeatherApi api = new WeatherRestApi(new HttpRequest());
        Iterable<WeatherInfoDto> dtos = api.pastWeather(37.017, -7.933, parse("2018-02-19"), parse("2018-03-21"));
        assertEquals(31, count(dtos));
        WeatherInfoDto w = skip(dtos, 30).iterator().next();
        assertEquals(14, w.getTempC());
        assertEquals(0, w.getPrecipMM());
        assertEquals(9, w.getFeelsLikeC());
        assertEquals("Sunny", w.getDescription());
    }

    @Test
    public void testWeatherServiceWithRestApi() {
        WeatherService api = new WeatherService(new WeatherRestApi(new HttpRequest()));
        Iterable<WeatherInfo> past = api
                .search("Faro")
                .iterator()
                .next()
                .pastWeather(parse("2018-02-19"), parse("2018-03-21"));
        assertEquals(31, count(past));
        WeatherInfo w = skip(past, 30).iterator().next();
        assertEquals(14, w.getTempC());
        assertEquals(0, w.getPrecipMM());
        assertEquals(9, w.getFeelsLikeC());
        assertEquals("Sunny", w.getDescription());
    }
}