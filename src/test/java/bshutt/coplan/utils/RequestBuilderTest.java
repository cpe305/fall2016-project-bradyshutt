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

        assertEquals(req.getRoute(), reqBuilder.route);
        assertEquals(req.getData(), reqBuilder.data);
    }
}