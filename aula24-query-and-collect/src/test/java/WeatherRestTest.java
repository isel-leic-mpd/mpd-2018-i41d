import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import util.HttpRequest;
import weather.WeatherApi;
import weather.WeatherRestApi;
import weather.WeatherService;
import weather.dto.WeatherInfoDto;
import weather.model.WeatherInfo;
import weather.restdto.PastWeatherDataWeatherDto;
import weather.restdto.PastWeatherDataWeatherHourlyDto;
import weather.restdto.PastWeatherDto;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.time.LocalDate.parse;
import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherRestTest {

    @Test
    public void testPastWeatherDtos() {
        HttpRequest req = new HttpRequest();
        String json = req
                        .getContent("http://api.worldweatheronline.com/premium/v1/past-weather.ashx?q=37.017,-7.933&date=2018-02-19&enddate=2018-03-21&tp=24&format=json&key=715b185b36034a4c879141841182802")
                        .reduce("", (prev, curr) -> prev + curr);
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
        WeatherInfoDto[] dtos = api
                .pastWeather(37.017, -7.933, parse("2018-02-19"), parse("2018-03-21"))
                .toArray(size -> new WeatherInfoDto[size]);
        assertEquals(31, dtos.length);
        WeatherInfoDto w = of(dtos).skip(30).findFirst().get();
        assertEquals(14, w.getTempC());
        assertEquals(0, w.getPrecipMM());
        assertEquals(9, w.getFeelsLikeC());
        assertEquals("Sunny", w.getDescription());
    }

    @Test
    public void testWeatherServiceWithRestApi() {
        WeatherService api = new WeatherService(new WeatherRestApi(new HttpRequest()));
        WeatherInfo[] past = api
                .search("Faro")
                .iterator()
                .next()
                .pastWeather(parse("2018-02-19"), parse("2018-03-21"))
                .toArray(size -> new WeatherInfo[size]);
        assertEquals(31, past.length);
        WeatherInfo w = of(past).skip(30).findFirst().get();
        assertEquals(14, w.getTempC());
        assertEquals(0, w.getPrecipMM());
        assertEquals(9, w.getFeelsLikeC());
        assertEquals("Sunny", w.getDescription());
    }
}