package weather;

import weather.dto.WeatherInfo;

/**
 * @author Miguel Gamboa
 *         created on 01-03-2018
 */
public interface WeatherAccumulator {
    int apply(int prev, WeatherInfo curr);
}
