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
import io.vertx.ext.web.Router;
import routes.SoccerRouter;

public class WebApp {
    public static void main(String[] args) throws Exception {

        Vertx vertx = Vertx.vertx();
        Router router = SoccerRouter.router(vertx);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(3000);
    }
}
