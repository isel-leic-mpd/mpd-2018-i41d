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

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.ContextTask;
import io.vertx.ext.web.Router;
import util.HttpRequest;
import weather.WeatherRestApi;
import weather.WeatherService;
import weather.model.Location;
import weather.model.WeatherInfo;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;
import static java.time.LocalDate.now;

public class WebApp {
    public static void main(String[] args) throws Exception {

        WeatherService weather =
                new WeatherService(new WeatherRestApi(new HttpRequest()));

        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        router.route("/search/city").handler(ctx -> {
            HttpServerRequest req = ctx.request();
            HttpServerResponse resp = ctx.response();
            resp.putHeader("content-type", "text/plain");
            weather
                    .search(req.getParam("name"))
                    .thenAccept(strm -> resp.end(strm
                            .map(Location::toString)
                            .collect(Collectors.joining("\n")))
                    );
        });
        router.route("/weather/:lat/:log").handler(ctx -> {
            HttpServerRequest req = ctx.request();
            HttpServerResponse resp = ctx.response();
            resp.putHeader("content-type", "text/plain");
            double lat = parseDouble(req.getParam("lat"));
            double log = parseDouble(req.getParam("log"));
            weather
                    .pastWeather(lat, log, now().minusDays(30), now().minusDays(1))
                    .thenAccept(strm ->  resp.end(strm
                            .map(WeatherInfo::toString)
                            .collect(Collectors.joining("\n")))
                    );
        });

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(3000);

        /*
        vertx
                .createHttpServer()
                .requestHandler(request -> {
                    // This handler gets called for each request that arrives on the server
                    HttpServerResponse response = request.response();
                    response.putHeader("content-type", "text/plain");

                    // Write to the response and end it
                    // response.end("Hello World!");
                    // <=>
                    String body = "Hello World!";
                    // response.putHeader("content-length", body.length() + "");
                    response.write(body);
                    response.end();
                })
                .listen(3000);
        */
    }
}
