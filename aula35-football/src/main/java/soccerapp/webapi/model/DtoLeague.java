package soccerapp.webapi.model;

import java.util.Date;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class DtoLeague {
    private final int id;
    private final String caption;
    private final String league;
    private final String year;
    private final int currentMatchday;
    private final int numberOfMatchdays;
    private final int numberOfTeams;
    private final int numberOfGames;
    private final Date lastUpdated;

    public DtoLeague(
            int id,
            String caption,
            String league,
            String year,
            int currentMatchday,
            int numberOfMatchdays,
            int numberOfTeams,
            int numberOfGames,
            Date lastUpdated)
    {
        this.id = id;
        this.caption = caption;
        this.league = league;
        this.year = year;
        this.currentMatchday = currentMatchday;
        this.numberOfMatchdays = numberOfMatchdays;
        this.numberOfTeams = numberOfTeams;
        this.numberOfGames = numberOfGames;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "DtoLeague{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                ", league='" + league + '\'' +
                ", year='" + year + '\'' +
                ", currentMatchday=" + currentMatchday +
                ", numberOfMatchdays=" + numberOfMatchdays +
                ", numberOfTeams=" + numberOfTeams +
                ", numberOfGames=" + numberOfGames +
                ", lastUpdated=" + lastUpdated +
                "}";
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getLeague() {
        return league;
    }

    public String getYear() {
        return year;
    }

    public int getCurrentMatchday() {
        return currentMatchday;
    }

    public int getNumberOfMatchdays() {
        return numberOfMatchdays;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
