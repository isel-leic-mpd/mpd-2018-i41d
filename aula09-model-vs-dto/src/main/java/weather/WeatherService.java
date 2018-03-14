package weather;

import util.IRequest;
import util.Queries;
import weather.dto.LocationDto;
import weather.dto.WeatherInfoDto;
import weather.model.Location;
import weather.model.WeatherInfo;

import java.time.LocalDate;

public class WeatherService {

    final WeatherWebApi api;

    public WeatherService(IRequest req) {
        api = new WeatherWebApi(req);
    }

    public WeatherService(WeatherWebApi api) {
        this.api = api;
    }

    private Iterable<WeatherInfo> pastWeather(
            double lat,
            double log,
            LocalDate from,
            LocalDate to
    ) {
        Iterable<WeatherInfoDto> past = api.pastWeather(lat, log, from, to);
        return Queries.map(past, WeatherService::dtoToWeatherInfo);
    }

    public Iterable<Location> search(String query) {
        Iterable<LocationDto> locals = api.search(query);
        return Queries.map(locals, this::dtoToLocation);
    }


    private static WeatherInfo dtoToWeatherInfo(WeatherInfoDto dto) {
        return new WeatherInfo(
                dto.getDate(),
                dto.getTempC(),
                dto.getDescription(),
                dto.getPrecipMM(),
                dto.getFeelsLikeC());
    }

    private  Location dtoToLocation(LocationDto dto) {
        double lat = dto.getLatitude();
        double log = dto.getLongitude();
        return new Location(
                dto.getCountry(),
                dto.getRegion(),
                lat,
                log,
                (from, to) -> { throw new UnsupportedOperationException(); });
    }
}
