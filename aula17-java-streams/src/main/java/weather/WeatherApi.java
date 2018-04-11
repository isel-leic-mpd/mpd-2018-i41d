package weather;

import weather.dto.LocationDto;
import weather.dto.WeatherInfoDto;

import java.time.LocalDate;

public interface WeatherApi {

    Iterable<LocationDto> search(String query);

    Iterable<WeatherInfoDto> pastWeather(double lat,
                                                double log,
                                                LocalDate from,
                                                LocalDate to);
}
