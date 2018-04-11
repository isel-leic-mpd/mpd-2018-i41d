package weather.restdto;

public class PastWeatherDataDto {
    private final PastWeatherDataWeatherDto[] weather;

    public PastWeatherDataDto(PastWeatherDataWeatherDto[] weather) {
        this.weather = weather;
    }

    public PastWeatherDataWeatherDto[] getWeather() {
        return weather;
    }
}
