package soccerapp;

import soccerapp.webapi.SoccerService;
import soccerapp.webapi.SoccerWebApi;
import soccerapp.webapi.model.DtoLeague;
import soccerapp.webapi.model.DtoLeagueTable;
import soccerapp.webapi.model.DtoPlayersList;
import soccerapp.webapi.model.DtoTeam;
import util.HttpRequest;
import util.IRequest;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class Program {

    public static void main(String[] args) throws Exception {
        HttpRequest req = new HttpRequest();
        IRequest logger = path -> {
            System.out.println(path);
            return req.getContent(path);
        };
        SoccerWebApi api = new SoccerWebApi(logger);
        /*
        CompletableFuture<DtoLeague[]> leagues = api.getLeagues();
        leagues
                .thenApply(Stream::of) // CF<Stream<DtoLeague>>
                .thenApply(strm -> strm.map(DtoLeague::getCaption)) // CF<Stream<String>>
                .thenAccept(strm -> strm.forEach(System.out::println)); // CF<void>
        */

        SoccerService soccer = new SoccerService(new SoccerWebApi(logger));
        soccer
                .getLeaguesWinners()
                .thenAccept(strm -> strm.forEach(System.out::println)) // CF<void>
                .join();
    }

    private static void sleep(int dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
