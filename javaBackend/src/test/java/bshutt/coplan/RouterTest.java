package bshutt.coplan;

import bshutt.coplan.Request;
import bshutt.coplan.Router;
import org.bson.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RouterTest {
    @Test
    public void getInstance() throws Exception {
        Router router = Router.getInstance();
        Router router2 = Router.getInstance();
        assertEquals(router, router2);
    }

    @Test
    public void register() throws Exception {
        Router router = Router.getInstance();
        Handler handler1 = (req, res) -> { };
        router.register("handler1", handler1);
        assertEquals(router.getHandler("handler1"), handler1);
    }

    @Test
    public void route() throws Exception {
        this.register();
        Router router = Router.getInstance();
        assertTrue(router.routeExists("handler1"));
    }

    @Test
    public void setupRoutes() throws Exception {
        Router router = Router.getInstance();
        router.setupRoutes();
        assertTrue(router.routeExists("getUser"));
        assertTrue(router.routeExists("usernameIsAvailable"));
        assertTrue(router.routeExists("registerForCourse"));
        assertTrue(router.routeExists("authenticate"));
    }
}