package weather;

import weather.dto.WeatherInfo;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel Gamboa
 *         created on 01-03-2018
 */
public class WeatherQueries {

    /**
     * V1 Naive Approach
     */
    public static Iterable<WeatherInfo> filterSunnyDays(Iterable<WeatherInfo> src) {
        List<WeatherInfo> res = new ArrayList<>();
        for(WeatherInfo w: src) {
            if(w.getDescription().contains("Sunny"))
                res.add(w);
        }
        return res;
    }

    public static Iterable<WeatherInfo> filterRainnyDays(Iterable<WeatherInfo> src) {
        List<WeatherInfo> res = new ArrayList<>();
        for(WeatherInfo w: src) {
            if(w.getDescription().contains("rain"))
                res.add(w);
        }
        return res;
    }

    /**
     * V2 - Value parametrization
     */
    public static Iterable<WeatherInfo> filterByDescription(
            Iterable<WeatherInfo> src,
            String desc) {
        List<WeatherInfo> res = new ArrayList<>();
        for(WeatherInfo w: src) {
            if(w.getDescription().contains(desc))
                res.add(w);
        }
        return res;
    }

    /**
     * V3 - Behavior parametrization
     */
    public static Iterable<WeatherInfo> filter(
            Iterable<WeatherInfo> src,
            WeatherPredicate p) {
        List<WeatherInfo> res = new ArrayList<>();
        for(WeatherInfo w: src) {
            if(p.test(w))
                res.add(w);
        }
        return res;
    }

    /**
     * Returns a new sequence of integers (Iterable<Integer>)
     * resulting from applying the toInt function to the elements
     * of the original src sequence of WeatherInfo objects.
     */
    public static Iterable<Integer> map(
            Iterable<WeatherInfo> src,
            WeatherToInt toInt) {
        throw new UnsupportedOperationException("Please provide an implementation for this method");
    }

    /**
     * Applies an accumulator function over a sequence.
     */
    public static int reduce(
            Iterable<WeatherInfo> src,
            int seed,
            WeatherAccumulator acc) {
        throw new UnsupportedOperationException("Please provide an implementation for this method");
    }


    public static int count(Iterable<WeatherInfo> src) {
        int n = 0;
        for(WeatherInfo w: src) n++;
        return n;
    }
}
