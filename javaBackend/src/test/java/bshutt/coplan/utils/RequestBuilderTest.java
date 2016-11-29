package bshutt.coplan.utils;

import bshutt.coplan.Request;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestBuilderTest {
    @Test
    public void testBuilder1() throws Exception {
        RequestBuilder reqBuilder = new RequestBuilder()
                .setRoute("routeABC")
                .addData("dataA", "valueA")
                .addData("dataB", "valueB");
        Request req = reqBuilder.done();

        assertEquals(req.route, reqBuilder.route);
        assertEquals(req.data, reqBuilder.data);
    }
}