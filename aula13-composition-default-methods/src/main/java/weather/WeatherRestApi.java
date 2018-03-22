/*
 * Copyright (c) 2017, Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package weather;

import com.google.gson.Gson;
import util.IRequest;
import weather.dto.LocationDto;
import weather.dto.WeatherInfoDto;
import weather.restdto.PastWeatherDataWeatherDto;
import weather.restdto.PastWeatherDataWeatherHourlyDto;
import weather.restdto.PastWeatherDto;
import weather.restdto.SearchDto;
import weather.restdto.SearchSearchApiResultDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.time.LocalDate;

import static java.lang.Double.parseDouble;
import static util.Queries.map;
import static util.Queries.of;
import static util.Queries.reduce;

/**
 * @author Miguel Gamboa
 *         created on 07-03-2017
 */
public class WeatherRestApi implements WeatherApi {

    private static final String WEATHER_TOKEN;
    private static final String WEATHER_HOST = "http://api.worldweatheronline.com";
    private static final String WEATHER_PAST = "/premium/v1/past-weather.ashx";
    private static final String WEATHER_PAST_ARGS =
            "?q=%s&date=%s&enddate=%s&tp=24&format=json&key=%s";
    private static final String WEATHER_SEARCH="/premium/v1/search.ashx?query=%s";
    private static final String WEATHER_SEARCH_ARGS="&format=json&key=%s";

    static {
        try {
            URL keyFile = ClassLoader.getSystemResource("worldweatheronline-app-key.txt");
            if(keyFile == null) {
               throw new IllegalStateException(
                       "YOU MUST GOT a KEY in developer.worldweatheronline.com and place it in src/main/resources/worldweatheronline-app-key.txt");
            } else {
                InputStream keyStream = keyFile.openStream();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(keyStream))) {
                    WEATHER_TOKEN = reader.readLine();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private final IRequest req;
    private final Gson gson = new Gson();

    public WeatherRestApi(IRequest req) {
        this.req = req;
    }

    /**
     * E.g. http://api.worldweatheronline.com/premium/v1/search.ashx?query=oporto&format=json&key=*****
     */

    public Iterable<LocationDto> search(String query) {
        String url=WEATHER_HOST + WEATHER_SEARCH + WEATHER_SEARCH_ARGS;
        url = String.format(url, query, WEATHER_TOKEN);
        Iterable<String> src = req.getContent(url);
        String json = reduce(src, "", (prev, curr) -> prev + curr);
        SearchDto dto = gson.fromJson(json, SearchDto.class);

        return map(
                of(dto.getSearch_api().getResult()),
                WeatherRestApi::parseLocationDto);
    }

    private static LocationDto parseLocationDto(SearchSearchApiResultDto dto) {
        return new LocationDto(
                dto.getCountry()[0].getValue(),
                dto.getRegion()[0].getValue(),
                parseDouble(dto.getLatitude()),
                parseDouble(dto.getLongitude())
        );
    }

    /**
     * E.g. http://api.worldweatheronline.com/premium/v1/past-weather.ashx?q=41.15,-8.6167&date=2017-02-01&enddate=2017-04-30&tp=24&format=json&key=*********
     */
    public Iterable<WeatherInfoDto> pastWeather(
            double lat,
            double log,
            LocalDate from,
            LocalDate to
    ) {
        String query = lat + "," + log;
        String path = WEATHER_HOST + WEATHER_PAST +
                String.format(WEATHER_PAST_ARGS, query, from, to, WEATHER_TOKEN);
        Iterable<String> src = req.getContent(path);
        String json = reduce(src, "", (prev, curr) -> prev + curr);
        PastWeatherDto dto = gson.fromJson(json, PastWeatherDto.class);
        return map(
                of(dto.getData().getWeather()),
                WeatherRestApi::parseWeatherInfoDto);
    }

    private static WeatherInfoDto parseWeatherInfoDto(PastWeatherDataWeatherDto rest) {
        PastWeatherDataWeatherHourlyDto hourly = rest.getHourly()[0];
        return new WeatherInfoDto(
                LocalDate.parse(rest.getDate()),
                hourly.getTempC(),
                hourly.getWeatherDesc(),
                hourly.getPrecipMM(),
                hourly.getFeelsLikeC());
    }


}
