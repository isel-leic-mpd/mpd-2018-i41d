package weather;

import util.Query;
import weather.dto.LocationDto;
import weather.dto.WeatherInfoDto;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface WeatherApi {

    Stream<LocationDto> search(String query);

    Query<WeatherInfoDto> pastWeather(double lat,
                                      double log,
                                      LocalDate from,
                                      LocalDate to);
}
