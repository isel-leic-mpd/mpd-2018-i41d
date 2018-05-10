import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Fibonacci {
    public static IntStream generate(){
        int [] nrs = {0, 1};
        return IntStream.generate(() -> {
            int tmp = nrs[1];
            nrs[1] = nrs[0] + nrs[1];
            return nrs[0] = tmp;
        });
    }
    public static IntStream iterate(){
        return Stream.iterate(new int[]{0, 1}, nrs -> {
            return new int[]{
                    nrs[1],
                    nrs[0] + nrs[1]
            };
        })
        .mapToInt(nrs -> nrs[1]);
    }
}
