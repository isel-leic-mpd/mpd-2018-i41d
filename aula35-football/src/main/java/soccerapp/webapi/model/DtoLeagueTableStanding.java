package soccerapp.webapi.model;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class DtoLeagueTableStanding {

    public static class DtoLinks {
        public final DtoLinksTeam team;
        public DtoLinks(DtoLinksTeam team) {
            this.team = team;
        }
        @Override
        public String toString() {
            return "team=" + team;
        }
    }

    public static class DtoLinksTeam {
        public final String href;
        public DtoLinksTeam(String href) {
            this.href = href;
        }
        @Override
        public String toString() {
            return href;
        }
    }


    public final DtoLinks _links;
    public final int position;
    public final String teamName;
    public final int playedGames;
    public final int points;
    public final int goals;
    public final int wins;
    public final int draws;
    public final int losses;

    public DtoLeagueTableStanding(
            DtoLinks links,
            int position,
            String teamName,
            int playedGames,
            int points,
            int goals,
            int wins,
            int draws,
            int losses)
    {
        this._links = links;
        this.position = position;
        this.teamName = teamName;
        this.playedGames = playedGames;
        this.points = points;
        this.goals = goals;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }

    @Override
    public String toString() {
        return "DtoLeagueTableStanding{" +
                "_links=" + _links +
                ", position=" + position +
                ", teamName='" + teamName + '\'' +
                ", playedGames=" + playedGames +
                ", points=" + points +
                ", goals=" + goals +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                "}";
    }
}
