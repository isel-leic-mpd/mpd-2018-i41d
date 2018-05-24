/*
 * Copyright (c) 2018, Miguel Gamboa
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

package app;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import util.HttpRequest;
import util.HttpServer;
import weather.WeatherRestApi;
import weather.WeatherService;
import weather.model.Location;
import weather.model.WeatherInfo;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Double.parseDouble;
import static java.time.LocalDate.now;

public class WebApp {
    public static void main(String[] args) throws Exception {

        ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
        String resPath = getSystemResource("public").toString();
        holderHome.setInitParameter("resourceBase", resPath);
        holderHome.setInitParameter("dirAllowed", "true");
        holderHome.setInitParameter("pathInfoOnly", "true");

        WeatherService weather =
                new WeatherService(new WeatherRestApi(new HttpRequest()));

        new HttpServer(3000)
                .addHandler("/search/city", "text/plain", req ->
                        weather
                            .search(req.getParameter("name"))
                            .join()
                            .map(Location::toString)
                            .collect(Collectors.joining("\n"))
                )
                .addHandler("/weather/*", "text/plain", req -> {
                    String[] parts = req.getPathInfo().split("/");
                    double lat = parseDouble(parts[1]);
                    double log = parseDouble(parts[2]);
                    return weather
                            .pastWeather(lat, log, now().minusDays(30), now().minusDays(1))
                            .join()
                            .map(WeatherInfo::toString)
                            .collect(Collectors.joining("\n"));
                })
                .addServletHolder("/public/*", holderHome)
                .run();
    }
}
