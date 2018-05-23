

import org.junit.jupiter.api.Test;
import util.AsyncFileRequest;

public class AsyncFileRequestTest {
    @Test
    public void testReadResource() {
        AsyncFileRequest req = new AsyncFileRequest();
        req.read("past-weather.ashx-q-41.15--8.6167-date-2017-02-01-enddate-2017-04-30");
    }
}