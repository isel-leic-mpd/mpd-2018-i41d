package soccerapp.webapi.model;

import java.util.Arrays;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class DtoLeagueTable {
    public final DtoLeagueTableStanding[] standing;

    public DtoLeagueTable(DtoLeagueTableStanding[] standing) {
        this.standing = standing;
    }

    @Override
    public String toString() {
        return "DtoLeagueTable{" +
                "standing=" + Arrays.toString(standing) +
                '}';
    }
}
