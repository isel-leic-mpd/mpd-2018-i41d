package soccerapp.webapi;

import soccerapp.webapi.model.DtoLeagueTable;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class SoccerService {
    final SoccerWebApi api;

    public SoccerService(SoccerWebApi api) {
        this.api = api;
    }

    public CompletableFuture<Stream<String>> getLeaguesWinners(){
        return api
                .getLeagues() // CF<DtoLeague[]>
                .thenApply(Stream::of) // CF<Stream<DtoLeague>>
                .thenApply(strm -> strm.map(l -> api // CF<Stream<CF<String>>>
                        .getLeagueTable(l.id)
                        .thenApply(table -> table.standing[0].teamName)))
                .thenApply(strm -> strm.collect(toList()).stream())
                .thenApply(strm -> strm.map(CompletableFuture::join));// CF<Stream<String>>
    }
}
