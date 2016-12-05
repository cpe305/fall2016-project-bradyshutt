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
        Document userDoc = doc.get("user", Document.class);

        assertEquals("_test_bshutt", userDoc.get("username"));
        assertEquals("BradyTest", userDoc.get("firstName"));
        assertEquals("ShuttTest", userDoc.get("lastName"));

        Request deleteReq = new RequestBuilder()
                .setRoute("removeUser")
                .addData("username", "_test_bshutt")
                .end();
        Document deleteRes = router.route(deleteReq).getDoc();
        assertEquals("good", deleteRes.get("type"));
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

        Request deleteReq = new RequestBuilder()
                .setRoute("removeUser")
                .addData("username", "_test_user")
                .end();
        Document deleteResponse = router.route(deleteReq).getDoc();
        assertEquals("good", deleteResponse.get("type"));
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
        assertEquals("good", response1.get("type"));

        Request req2 = reqTemplate.end();
        Document response2 = router.route(req2).getDoc();
        assertEquals("error", response2.get("type"));

        Request deleteReq = new RequestBuilder()
                .setRoute("removeUser")
                .addData("username", "_test_bshutt")
                .end();
        Document deleteResponse = router.route(deleteReq).getDoc();
        assertEquals("good", deleteResponse.get("type"));
    }

    @Test
    public void testUserLoginSuccess() {
        Document signUpRes = this.createTestUser("_test_bshutt", "123");
        assertEquals("good", signUpRes.getString("status"));

        Request loginReq = new RequestBuilder()
                .setRoute("login")
                .addData("username", "_test_bshutt")
                .addData("password", "123")
                .end();

        Document loginRes = router.route(loginReq).getDoc();
        assertEquals("good", loginRes.getString("status"));
        assertNotNull(loginRes.getString("jwt"));

        this.deleteUsers(new String[]{"_test_bshutt"});
    }

    @Test
    public void testUserLoginFailure() {
        Document signUpRes = this.createTestUser("_test_bshutt", "123");
        assertEquals("good", signUpRes.getString("status"));

        Request loginReq = new RequestBuilder()
                .setRoute("login")
                .addData("username", "_test_bshutt")
                .addData("password", "1234")
                .end();

        Document loginRes = router.route(loginReq).getDoc();
        assertEquals("bad", loginRes.getString("status"));

        this.deleteUsers(new String[]{"_test_bshutt"});
    }

    @Test
    public void testJwt() {
        this.createTestUser("_test_bshutt", "123");

        Request loginReq = new RequestBuilder()
                .setRoute("login")
                .addData("username", "_test_bshutt")
                .addData("password", "123")
                .end();

        Document loginRes = router.route(loginReq).getDoc();
        assertEquals("good", loginRes.getString("status"));
        String jwt = loginRes.getString("jwt");
        assertNotNull(jwt);

        Request jwtLoginReq = new RequestBuilder()
                .setRoute("checkJwt")
                .addData("jwt", jwt)
                .end();

        Document jwtLoginRes = router.route(jwtLoginReq).getDoc();
        assertEquals("good", jwtLoginRes.getString("status"));

        this.deleteUsers(new String[]{"_test_bshutt"});
    }

    @Test
    public void testRegisterForCourse() {
        this.createTestUser("_test_bshutt", "123");

        Request registerReq = new RequestBuilder()
                .setRoute("registerForCourse")
                .addData("username", "_test_bshutt")
                .addData("courseName", "CPE-123")
                .done();
        Document registerRes = router.route(registerReq).getDoc();
        //assertEquals("good", registerRes.getString("status"));

        this.deleteUsers(new String[]{"_test_bshutt"});
    }

    private Document createTestUser(String username, String password) {
        Request req = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", username)
                .addData("firstName", "Brady")
                .addData("lastName", "Shutt")
                .addData("password", password)
                .end();
        return router.route(req).getDoc();
    }

    private void deleteUsers(String[] usernames) {
        for (String username : usernames) {
            Request deleteReq = new RequestBuilder()
                    .setRoute("removeUser")
                    .addData("username", username)
                    .end();
            router.route(deleteReq);
        }
    }

}