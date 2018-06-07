package soccerapp.webapi;

import com.google.gson.Gson;
import soccerapp.webapi.model.DtoLeague;
import soccerapp.webapi.model.DtoLeagueTable;
import soccerapp.webapi.model.DtoPlayersList;
import soccerapp.webapi.model.DtoTeam;
import util.IRequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class SoccerWebApi {

    private static final String HOST = "http://api.football-data.org/";
    private static final String PATH_LEAGUES = "/v1/soccerseasons";
    private static final String PATH_TABLE = "/v1/soccerseasons/%d/leagueTable";
    private static final String PATH_TEAM = "/v1/teams/%s";
    private static final String API_TOKEN_VALUE = "PLACE HERE YOUR API KEY";
    private static final String API_TOKEN_KEY = "X-Auth-Token";


    private final Gson gson = new Gson();
    private final IRequest req;


    public SoccerWebApi(IRequest req)
    {
        this.req = req;
    }

    public CompletableFuture<DtoLeague[]> getLeagues()
    {
        return httpGet(HOST + PATH_LEAGUES, DtoLeague[].class);
    }

    public CompletableFuture<DtoLeagueTable> getLeagueTable(int leagueId)
    {
        String path = String.format(PATH_TABLE, leagueId);
        return httpGet(HOST + path, DtoLeagueTable.class);
    }

    public CompletableFuture<DtoTeam> getTeam(String id)
    {
        String path = String.format(PATH_TEAM, id);
        return httpGet(HOST + path, DtoTeam.class);
    }

    public CompletableFuture<DtoPlayersList> getPlayers(String path)
    {
        return httpGet(path, DtoPlayersList.class);
    }

    /**
     * @param klass Class reflection info of dto
     * @param <T> The type of DTO
     */
    private <T> CompletableFuture<T> httpGet(String path, Class<T> klass)
    {
        return req
                .getContent(path) // CF<String>
                // .addHeader(API_TOKEN_KEY, API_TOKEN_VALUE) // Remove comment to enable your API KEY
                .thenApply(body -> gson.fromJson(body, klass));
    }
}
