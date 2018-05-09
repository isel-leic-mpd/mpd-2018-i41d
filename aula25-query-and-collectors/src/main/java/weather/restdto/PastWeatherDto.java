package weather.restdto;

public class PastWeatherDto {
    public PastWeatherDto(PastWeatherDataDto data) {
        this.data = data;
    }

    private final PastWeatherDataDto data;

    public PastWeatherDataDto getData() {
        return data;
    }
}
