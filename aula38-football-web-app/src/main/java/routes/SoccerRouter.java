package routes;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import soccerapp.webapi.SoccerWebApi;
import soccerapp.webapi.model.DtoLeague;
import soccerapp.webapi.model.DtoLeagueTableStanding;
import util.HttpRequest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SoccerRouter {

    private final SoccerWebApi soccer;
    private final TemplateEngine engine = HandlebarsTemplateEngine.create();

    public SoccerRouter(SoccerWebApi soccer) {
        this.soccer = soccer;
    }

    public static Router router(Vertx vertx) {
        return router(vertx, new SoccerWebApi(new HttpRequest()));
    }

    public static Router router(Vertx vertx, SoccerWebApi soccer) {
        Router router = Router.router(vertx);
        SoccerRouter handlers = new SoccerRouter(soccer);
        router.route("/leagues").handler(handlers::leaguesHandler);
        router.route("/leagues/:id/table").handler(handlers::leagueTableHandler);
        router.route("/teams/:id").handler(handlers::teamHandler);
        return router;
    }

    private void teamHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        String teamId = req.getParam("id");
        soccer
                .getTeam(teamId)
                .thenAccept(dto -> {
                    ctx.put("team", dto);
                    engine.render(ctx, "templates", "/team.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }

    private void leagueTableHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        int leagueId = Integer.parseInt(req.getParam("id"));
        soccer
                .getLeagueTable(leagueId)
                .thenAccept(dto -> {
                    ctx.put("matchday", dto.getMatchday());
                    ctx.put("leagueCaption", dto.getLeagueCaption());
                    ctx.put("standing", dto.getStanding());
                    engine.render(ctx, "templates", "/leagueTable.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }

    private void leaguesHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        soccer
                .getLeagues()
                .thenAccept(arr -> {
                    ctx.put("leagues", arr);
                    engine.render(ctx, "templates", "/leagues.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }
}
