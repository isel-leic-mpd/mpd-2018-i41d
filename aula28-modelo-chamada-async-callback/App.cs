using System;
using System.Net;
using Newtonsoft.Json;
using System.Threading;
using System.Net.Http;
using System.Threading.Tasks;

static class App
{
    class LeagueTable {
        public string matchday;
        public Standing[] standing;
        public string leagueCaption;
    }

    class Standing {
        public string teamName;
        public int goals;
        public int playedGames;
    }
    
    const string FOOTBALL_URI = "http://api.football-data.org/v1/soccerseasons/{0}/leagueTable";
    
    static void Main()
    {
        /*
        Console.WriteLine("############ SYNC demo");
        GetSync(445);
        GetSync(446);
        GetSync(457);
        Console.ReadLine();
        */        
        
        Console.WriteLine("############ Async demo");
        GetAsync(445);
        GetAsync(446);
        GetAsync(457);

        Console.ReadLine();
        Console.WriteLine("############ Async BAD Promise demo");
        GetAsyncBadPromise(445);
        GetAsyncBadPromise(446);
        GetAsyncBadPromise(457);
        
        Console.ReadLine();
        Console.WriteLine("############ Async Promise demo");
        GetAsyncPromise(445);
        GetAsyncPromise(446);
        GetAsyncPromise(457);
        Console.ReadLine();
    }
    static void GetSync(int leagueId) {
        Console.WriteLine("...... Requesting " + leagueId);
        string path = string.Format(FOOTBALL_URI, leagueId);
        using(WebClient client = new WebClient()) {
            string content = client.DownloadString(path);
            LeagueTable league = JsonConvert.DeserializeObject<LeagueTable>(content);
            Console.WriteLine("> {0} --- leader: {1}", 
                league.leagueCaption, 
                league.standing[0].teamName);        
        }
        
    }
    static void GetAsync(int leagueId) { 
        Console.WriteLine("...... Requesting " + leagueId);
        string path = string.Format(FOOTBALL_URI, leagueId);
        WebClient client = new WebClient();
        // <=> client.add_DownloadStringCompleted(GetAsyncCallback);
        client.DownloadStringCompleted += GetAsyncCallback; 
        client.DownloadStringAsync(new Uri(path));
    }

    static void GetAsyncCallback(object sender, DownloadStringCompletedEventArgs args){
        LeagueTable league = JsonConvert.DeserializeObject<LeagueTable>(args.Result);
        Console.WriteLine("> {0} --- leader: {1}", 
                league.leagueCaption, 
                league.standing[0].teamName);        
    }
    
    static void GetAsyncBadPromise(int leagueId) { 
        Console.WriteLine("...... Requesting " + leagueId);
        string path = string.Format(FOOTBALL_URI, leagueId);
        HttpClient client = new HttpClient();
        Task<string> res = client.GetStringAsync(new Uri(path));
        
        // !!!!! NAO fazer isto => bloqueante
        string content = res.Result; //!!!!! Bloqueante
        LeagueTable league = JsonConvert.DeserializeObject<LeagueTable>(content);
        Console.WriteLine("> {0} --- leader: {1}", 
            league.leagueCaption, 
            league.standing[0].teamName);        
    }

    static void GetAsyncPromise(int leagueId) { 
        Console.WriteLine("...... Requesting " + leagueId);
        string path = string.Format(FOOTBALL_URI, leagueId);
        HttpClient client = new HttpClient();
        client
            .GetStringAsync(new Uri(path))
            .ContinueWith(res => {
                string content = res.Result;
                LeagueTable league = JsonConvert.DeserializeObject<LeagueTable>(content);
                Console.WriteLine("> {0} --- leader: {1}", 
                    league.leagueCaption, 
                    league.standing[0].teamName);        
            });
    }

}