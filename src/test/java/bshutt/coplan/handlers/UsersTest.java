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
    public void testCreateThenGetUser() {

        Request createReq = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", "_test_bshutt")
                .addData("firstName", "BradyTest")
                .addData("lastName", "ShuttTest")
                .addData("password", "testpassword")
                .end();
        Document createResponse = router.route(createReq).getDoc();
        assertNotNull(createResponse);

        String usernameToGet = "_test_bshutt";
        Request getReq = new RequestBuilder()
                .setRoute("getUser")
                .addData("username", usernameToGet)
                .end();
        Response getResponse = router.route(getReq);
        Document doc = getResponse.getDoc();
        Document body = doc.get("body", Document.class);

        assertEquals("_test_bshutt", body.get("username"));
        assertEquals("BradyTest", body.get("firstName"));
        assertEquals("ShuttTest", body.get("lastName"));

        Request deleteReq = new RequestBuilder()
                .setRoute("removeUser")
                .addData("username", "_test_bshutt")
                .end();
        Document deleteRes = router.route(deleteReq).getDoc();
        assertEquals("response", deleteRes.get("type"));
    }

    @Test
    public void testCreateUser() {
        Request req = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", "_test_user")
                .addData("firstName", "Test")
                .addData("lastName", "User")
                .addData("password", "password123")
                .end();
        Document response = router.route(req).getDoc();
        assertEquals("response", response.get("type"));

        Request deleteReq = new RequestBuilder()
                .setRoute("removeUser")
                .addData("username", "_test_user")
                .end();
        Document deleteResponse = router.route(deleteReq).getDoc();
        assertEquals("response", deleteResponse.get("type"));
    }


    @Test
    public void testCreateUserFailure() {
        RequestBuilder reqTemplate = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", "_test_bshutt")
                .addData("firstName", "Brady")
                .addData("lastName", "Shutt")
                .addData("password", "123");

        Request req1 = reqTemplate.end();
        Document response1 = router.route(req1).getDoc();
        assertEquals("response", response1.get("type"));

        Request req2 = reqTemplate.end();
        Document response2 = router.route(req2).getDoc();
        assertEquals("error", response2.get("type"));

        Request deleteReq = new RequestBuilder()
                .setRoute("removeUser")
                .addData("username", "_test_bshutt")
                .end();
        Document deleteResponse = router.route(deleteReq).getDoc();
        assertEquals("response", deleteResponse.get("type"));

    }

}