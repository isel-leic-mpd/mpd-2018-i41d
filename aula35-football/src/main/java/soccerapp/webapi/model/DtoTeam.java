package soccerapp.webapi.model;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class DtoTeam {

    public static class DtoLinks {
        public final DtoLinksPlayer players;
        public DtoLinks(DtoLinksPlayer players) {
            this.players = players;
        }
        @Override
        public String toString() {
            return "players = " + players;
        }
    }

    public static class DtoLinksPlayer {
        public final String href;
        public DtoLinksPlayer(String href) {
            this.href = href;
        }
        @Override
        public String toString() {
            return href;
        }
    }

    private final DtoLinks _links;
    private final String name;
    private final String code;
    private final String shortName;
    private final String squadMarketValue;
    private final String crestUrl;

    public DtoTeam(DtoLinks links, String name, String code, String shortName, String squadMarketValue, String crestUrl) {
        this._links = links;
        this.name = name;
        this.code = code;
        this.shortName = shortName;
        this.squadMarketValue = squadMarketValue;
        this.crestUrl = crestUrl;
    }

    @Override
    public String toString() {
        return "DtoTeam{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", shortName='" + shortName + '\'' +
                ", squadMarketValue='" + squadMarketValue + '\'' +
                ", crestUrl='" + crestUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getShortName() {
        return shortName;
    }

    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    public String getCrestUrl() {
        return crestUrl;
    }
}
