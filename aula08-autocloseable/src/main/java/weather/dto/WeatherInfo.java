/*
 * Copyright (c) 2017, Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package weather.dto;

import java.time.LocalDate;

/**
 * @author Miguel Gamboa
 *         created on 01-08-2016
 */
public class WeatherInfo {
    private final LocalDate date;     // index 0
    private final int tempC;          // index 2
    private final String description; // index 10
    private final double  precipMM;   // index 11
    private final int feelsLikeC;     // index 24

    public WeatherInfo(LocalDate date, int tempC, String description, double precipMM, int feelsLikeC) {
        this.date = date;
        this.tempC = tempC;
        this.description = description;
        this.precipMM = precipMM;
        this.feelsLikeC = feelsLikeC;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTempC() {
        return tempC;
    }

    public String getDescription() {
        return description;
    }

    public double getPrecipMM() {
        return precipMM;
    }

    public int getFeelsLikeC() {
        return feelsLikeC;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                date +
                ", tempC=" + tempC +
                ", '" + description + '\'' +
                ", precipMM=" + precipMM +
                ", feelsLikeC=" + feelsLikeC +
                '}';
    }

    /**
     * Hourly information follows below the day according to the format of
     * /past weather resource of the World Weather Online API
     */
    public static WeatherInfo valueOf(String line) {
        String[] data = line.split(",");
        return new WeatherInfo(
                LocalDate.parse(data[0]),
                Integer.parseInt(data[2]),
                data[10],
                Double.parseDouble(data[11]),
                Integer.parseInt(data[24]));
    }
}
