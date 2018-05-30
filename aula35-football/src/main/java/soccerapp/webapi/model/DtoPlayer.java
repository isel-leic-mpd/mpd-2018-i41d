package soccerapp.webapi.model;

/**
 * @author Miguel Gamboa
 *         created on 02-06-2016
 */
public class DtoPlayer {
    public final String name;
    public final String position;
    public final int jerseyNumber;
    public final String dateOfBirth;
    public final String nationality;
    public final String contractUntil;
    public final String marketValue;

    public DtoPlayer(String name, String position, int jerseyNumber, String dateOfBirth, String nationality, String contractUntil, String marketValue) {
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.contractUntil = contractUntil;
        this.marketValue = marketValue;
    }

    @Override
    public String toString() {
        return "DtoPlayer{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", nationality='" + nationality + '\'' +
                ", contractUntil='" + contractUntil + '\'' +
                ", marketValue='" + marketValue + '\'' +
                '}';
    }
}
