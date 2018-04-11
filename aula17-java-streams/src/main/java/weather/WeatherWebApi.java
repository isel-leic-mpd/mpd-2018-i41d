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

import util.IRequest;
import weather.dto.LocationDto;
import weather.dto.WeatherInfoDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.time.LocalDate;

import static util.Queries.filter;
import static util.Queries.map;
import static util.Queries.skip;

/**
 * @author Miguel Gamboa
 *         created on 07-03-2017
 */
class WeatherWebApi implements WeatherApi {

    private static final String WEATHER_TOKEN;
    private static final String WEATHER_HOST = "http://api.worldweatheronline.com";
    private static final String WEATHER_PAST = "/premium/v1/past-weather.ashx";
    private static final String WEATHER_PAST_ARGS =
            "?q=%s&date=%s&enddate=%s&tp=24&format=csv&key=%s";
    private static final String WEATHER_SEARCH="/premium/v1/search.ashx?query=%s";
    private static final String WEATHER_SEARCH_ARGS="&format=tab&key=%s";

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

    public WeatherWebApi(IRequest req) {
        this.req = req;
    }

    /**
     * E.g. http://api.worldweatheronline.com/premium/v1/search.ashx?query=oporto&format=tab&key=*****
     */

    public Iterable<LocationDto> search(String query) {
        String url=WEATHER_HOST + WEATHER_SEARCH + WEATHER_SEARCH_ARGS;
        url = String.format(url, query, WEATHER_TOKEN);
        return map(
                filter(
                        req.getContent(url),
                        l -> !l.startsWith("#")),
                LocationDto::valueOf);

        /*
        List<LocationDto> locations= new ArrayList<>();
        Iterator<String> iteratorString= req.getContent(url).iterator();
        while(iteratorString.hasNext()) {
            String line = iteratorString.next();
            if(!line.startsWith("#")) locations.add(LocationDto.valueOf(line));
        }
        return locations;
        */
    }

    /**
     * E.g. http://api.worldweatheronline.com/premium/v1/past-weather.ashx?q=41.15,-8.6167&date=2017-02-01&enddate=2017-04-30&tp=24&format=csv&key=*********
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
        boolean [] isEven = {true};
        return map(
                filter(
                    skip(
                        filter(src, w -> !w.startsWith("#")), // Filter comments
                        1),  // Skip line: Not Available
                    l -> isEven[0] = !isEven[0]), // Filter Even lines
                WeatherInfoDto::valueOf); // Map to WeatherInfoDto objects
        /*
        while(iter.next().startsWith("#")) { }
        iter.next(); // Skip line: Not Available
        while(iter.hasNext()) {
            String line = iter.next(); // Skip Daily Info
            res.add(WeatherInfoDto.valueOf(line));
            if(iter.hasNext()) iter.next();
        }
        return res;
        */
    }
}
