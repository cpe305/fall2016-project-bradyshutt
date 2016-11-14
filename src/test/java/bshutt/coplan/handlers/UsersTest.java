package bshutt.coplan.handlers;

import bshutt.coplan.*;
import bshutt.coplan.utils.RequestBuilder;
import org.bson.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

public class UsersTest {

    private JsonReaderStrategy jrs;
    private Router router;

    @Before
    public void setup() {
        this.jrs = new JsonReaderStrategy();
        this.router = Router.getInstance();
        router.setupRoutes();
    }

    @Test
    public void testGetUser() {
        String usernameToGet = "bshutt";
        Request req = new RequestBuilder()
                .setRoute("getUser")
                .addData("username", usernameToGet)
                .end();
        Document response = router.route(req).getDoc();
        Document body = response.get("body", Document.class);

        assertEquals("bshutt", body.get("username"));
        assertEquals("Brady", body.get("firstName"));
        assertEquals("Shutt", body.get("lastName"));

    }

    @Test
    public void testCreateUser() {
        Request req = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", "xx_test_user")
                .addData("firstName", "Test")
                .addData("lastName", "User")
                .end();
        Document response = router.route(req).getDoc();
        assertEquals("response", response.get("type"));

        Request deleteReq = new RequestBuilder()
                .setRoute("removeUser")
                .addData("username", "xx_test_user")
                .end();
        Document deleteResponse = router.route(deleteReq).getDoc();
        assertEquals("response", deleteResponse.get("type"));
    }


    @Test
    public void testCreateUserFailure() {
        Request req = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", "bshutt")
                .addData("firstName", "Brady")
                .addData("lastName", "Shutt")
                .end();
        Document response = router.route(req).getDoc();

        assertEquals("error", response.get("type"));
    }

}