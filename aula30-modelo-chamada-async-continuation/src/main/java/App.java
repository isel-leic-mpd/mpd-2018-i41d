import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class App
{
    class LeagueTable {
        public String matchday;
        public Standing[] standing;
        public String leagueCaption;
    }

    class Standing {
        public String teamName;
        public int goals;
        public int playedGames;
    }
    
    static final String FOOTBALL_URI = "http://api.football-data.org/v1/soccerseasons/%s/leagueTable";
    
    public static void main(String[] args) throws Exception
    {
        System.out.println("############ SYNC demo");
        getSync(445);
        getSync(446);
        getSync(457);
        
        System.out.println("############ Async BAD demo");
        getAsyncBad(445);
        getAsyncBad(446);
        getAsyncBad(457);
        
        
        System.out.println("############ Async demo");
        getAsync(445);
        getAsync(446);
        getAsync(457);
        
        
    }
    static void getSync(int leagueId) throws Exception{
        System.out.println("...... Requesting " + leagueId);
        String path = String.format(FOOTBALL_URI, leagueId);
        //
        // Prepare Request
        //
        URLConnection connection = new URL(path).openConnection(); 
        connection.setRequestProperty("X-Auth-Token", "f8ca52bbe7a7436b832e939ed704394f");
        //
        // Parse Response => Bloqueante
        //
        InputStream data = connection.getInputStream(); // SYNC => Bloqueante
        BufferedReader reader = new BufferedReader(new InputStreamReader(data));        
        String content = reader.lines().reduce((prev, curr) -> prev + curr).get();
        
        LeagueTable league = fromJson(content);
        System.out.printf("> %s --- leader: %s\n", 
            league.leagueCaption, 
            league.standing[0].teamName);        
    }
    
    static void getAsyncBad(int leagueId) throws Exception{
        System.out.println("...... Requesting " + leagueId);
        String path = String.format(FOOTBALL_URI, leagueId);
        //
        // Prepare Request
        //
        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        CompletableFuture<Response> whenResponse = asyncHttpClient
            .prepareGet(path)
            .addHeader("X-Auth-Token", "f8ca52bbe7a7436b832e939ed704394f")
            .execute()
            .toCompletableFuture();
        //
        // Parse Response => MAL Bloqueante
        //
        String content = whenResponse
                            .join() // !!!!!!! NãO fazer BLOQUEANTE
                            .getResponseBody();
        closeAHC(asyncHttpClient);
        
        LeagueTable league = fromJson(content);
        System.out.printf("> %s --- leader: %s\n", 
            league.leagueCaption, 
            league.standing[0].teamName);        
    }
    
    static void getAsync(int leagueId) throws Exception{
        System.out.println("...... Requesting " + leagueId);
        String path = String.format(FOOTBALL_URI, leagueId);
        //
        // Prepare Request
        //
        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        asyncHttpClient
            .prepareGet(path)
            .addHeader("X-Auth-Token", "f8ca52bbe7a7436b832e939ed704394f")
            .execute()
            .toCompletableFuture()
            .thenApply(Response::getResponseBody)
            .whenComplete((res, ex) -> closeAHC(asyncHttpClient))
            .thenApply(App::fromJson)
            .thenAccept(league -> 
                System.out.printf("> %s --- leader: %s\n", 
                    league.leagueCaption, 
                    league.standing[0].teamName));
    }
    
    static LeagueTable fromJson(String content) {
        Gson gson = new Gson();
        return gson.fromJson(content, LeagueTable.class);
    }
    
    static void closeAHC(AsyncHttpClient client){
        try {
            client.close();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}