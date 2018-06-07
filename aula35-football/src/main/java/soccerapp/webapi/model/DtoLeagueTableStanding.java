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


    private final DtoLinks _links;
    private final int position;
    private final String teamName;
    private final int playedGames;
    private final int points;
    private final int goals;
    private final int wins;
    private final int draws;
    private final int losses;

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

    public String getId() {
        String[] strs = _links.team.href.split("/");
        return strs[strs.length - 1];
    }

    public int getPosition() {
        return position;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public int getPoints() {
        return points;
    }

    public int getGoals() {
        return goals;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }
}
