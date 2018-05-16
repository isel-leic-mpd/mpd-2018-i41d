package weather;

import util.IRequest;
import weather.dto.LocationDto;
import weather.dto.WeatherInfoDto;
import weather.model.Location;
import weather.model.WeatherInfo;

import java.time.LocalDate;
import java.util.stream.Stream;

public class WeatherService {

    final WeatherApi api;

    public WeatherService(IRequest req) {
        api = new WeatherWebApi(req);
    }

    public WeatherService(WeatherApi api) {
        this.api = api;
    }

    private Stream<WeatherInfo> pastWeather(
            double lat,
            double log,
            LocalDate from,
            LocalDate to
    ) {
        return api
                .pastWeather(lat, log, from, to)
                .map(WeatherService::dtoToWeatherInfo)
                .toStream();
    }

    public Stream<Location> search(String query) {
        return api
                .search(query)
                .map(this::dtoToLocation);
    }

    private Location dtoToLocation(LocationDto dto) {
        final double lat = dto.getLatitude();
        final double log = dto.getLongitude();
        return new Location(
                dto.getCountry(),
                dto.getRegion(),
                lat,
                log,
                (from, to) -> this.pastWeather(
                        lat,
                        log,
                        from,
                        to));
    }

    private static WeatherInfo dtoToWeatherInfo(WeatherInfoDto dto) {
        return new WeatherInfo(
                dto.getDate(),
                dto.getTempC(),
                dto.getDescription(),
                dto.getPrecipMM(),
                dto.getFeelsLikeC());
    }
}
