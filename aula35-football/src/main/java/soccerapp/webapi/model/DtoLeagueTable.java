package soccerapp.webapi.model;

import java.util.Arrays;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class DtoLeagueTable {

    private final String leagueCaption;
    private final int matchday;
    private final DtoLeagueTableStanding[] standing;

    public DtoLeagueTable(String leagueCaption, int matchday, DtoLeagueTableStanding[] standing) {
        this.leagueCaption = leagueCaption;
        this.matchday = matchday;
        this.standing = standing;
    }

    @Override
    public String toString() {
        return "DtoLeagueTable{" +
                "standing=" + Arrays.toString(standing) +
                '}';
    }

    public String getLeagueCaption() {
        return leagueCaption;
    }

    public int getMatchday() {
        return matchday;
    }

    public DtoLeagueTableStanding[] getStanding() {
        return standing;
    }
}
