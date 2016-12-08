package bshutt.coplan.handlers;

import bshutt.coplan.*;
import bshutt.coplan.utils.RequestBuilder;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

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
    public void testCreateThenGetUserSuccess() {
        this.testUser("rswan", "123", (user, doc) -> {
            Request getReq = new RequestBuilder()
                    .setRoute("getUser")
                    .addData("username", "rswan")
                    .end();
            Document getResponseDoc = router.route(getReq).getDoc();
            assertEquals(1, getResponseDoc.get("res"));
            Document userDoc = getResponseDoc.get("user", Document.class);

            assertEquals("rswan", userDoc.get("username"));
            assertEquals("Ron", userDoc.get("firstName"));
            assertEquals("Swanson", userDoc.get("lastName"));
        });
    }

    @Test
    public void testGetUserFailure() {
        Request req = new RequestBuilder()
                .setRoute("getUser")
                .addData("username", "jon_snow")
                .done();
        Document resDoc = router.route(req).getDoc();
        assertEquals(0, resDoc.get("res"));
    }

    @Test
    public void testLoginUserDNE() {
        Request req = new RequestBuilder()
                .setRoute("authenticate")
                .addData("username", "jon_snow")
                .addData("password", "winter_is_coming")
                .done();
        Document resDoc = router.route(req).getDoc();
        assertEquals(0, resDoc.get("res"));
    }

    @Test
    public void testCreateUser() {
        this.testUser("jsnow", "123", (user, doc) -> {
            assertEquals(1, doc.get("res"));
        });
    }


    @Test
    public void testCreateUserFailure() {
        this.testUser("rswan", "123", (user, doc) -> {
            Request req = new RequestBuilder()
                    .setRoute("createUser")
                    .addData("username", "rswan")
                    .addData("firstName", "Ron")
                    .addData("lastName", "Swanson")
                    .addData("password", "123")
                    .end();

            Document response = router.route(req).getDoc();
            assertEquals(0, response.get("res"));

            Request deleteReq = new RequestBuilder()
                    .setRoute("removeUser")
                    .addData("username", "_test_bshutt2")
                    .end();
            Document deleteResponse = router.route(deleteReq).getDoc();
            assertEquals(1, deleteResponse.get("res"));
        });
    }

    @Test
    public void testUserLoginSuccess() {
        this.testUser("_test_bshutt", "123", (user, doc) -> {
            Request loginReq = new RequestBuilder()
                    .setRoute("login")
                    .addData("username", "_test_bshutt")
                    .addData("password", "123")
                    .end();

            Document loginRes = router.route(loginReq).getDoc();
            assertEquals(1, loginRes.get("res"));
            assertNotNull(loginRes.getString("jwt"));
        });
    }

    @Test
    public void testUserLoginFailure() {
        this.testUser("_test_bshutt", "123", (user, doc) -> {
            Request loginReq = new RequestBuilder()
                    .setRoute("login")
                    .addData("username", "_test_bshutt")
                    .addData("password", "abc")
                    .end();
            Document loginRes = router.route(loginReq).getDoc();
            assertEquals(0, loginRes.get("res"));
        });
    }

    @Test
    public void testJwt() {
        this.testUser("_test_bshutt", "123", (user, doc) -> {
            Request loginReq = new RequestBuilder()
                    .setRoute("login")
                    .addData("username", "_test_bshutt")
                    .addData("password", "123")
                    .end();

            Document loginRes = router.route(loginReq).getDoc();
            assertEquals(1, loginRes.get("res"));
            String jwt = loginRes.getString("jwt");
            assertNotNull(jwt);

            Request jwtLoginReq = new RequestBuilder()
                    .setRoute("checkJwt")
                    .addData("jwt", jwt)
                    .end();

            Document jwtLoginRes = router.route(jwtLoginReq).getDoc();
            assertEquals(1, jwtLoginRes.get("res"));

        });
    }

    @Test
    public void testRegisterForCourseSuccess() {
        this.testUser("_test_bshutt", "123", (user, doc) -> {
            String jwt = doc.getString("jwt");

            Request registerReq = new RequestBuilder()
                    .setRoute("registerForCourse")
                    .addData("jwt", jwt)
                    .addData("courseName", "_test_CPE-123")
                    .done();

            Document registerRes = router.route(registerReq).getDoc();
            assertEquals(1, registerRes.get("res"));

            Request courseReq = new RequestBuilder()
                    .setRoute("getCourse")
                    .addData("courseName", "_test_CPE-123")
                    .done();
            Document courseRes = router.route(courseReq).getDoc();
            assertEquals(1, courseRes.get("res"));
            Document courseDoc = courseRes.get("course", Document.class);
            ArrayList<String> users = courseDoc.get("registeredUsers", ArrayList.class);

            assertTrue(users.contains("_test_bshutt"));
            assertFalse(users.contains("this_user_does_not_exist"));
            this.deleteCourses(new String[]{"_test_CPE-123"}    );
        });
    }

    @Test
    public void testRegisterForCourseFailure() {
        this.testUser("_test_bshutt", "123", (user, doc) -> {
            String jwt = doc.getString("jwt");

            Request registerReq = new RequestBuilder()
                    .setRoute("registerForCourse")
                    .addData("jwt", jwt)
                    .addData("courseName", "_test_CPE-123")
                    .done();
            Document registerRes = router.route(registerReq).getDoc();
            assertEquals(1, registerRes.get("res"));

            Document registerAgainRes = router.route(registerReq).getDoc();
            assertEquals(0, registerAgainRes.get("res"));
            this.deleteCourses(new String[]{"_test_CPE-123"}    );
        });
    }

    @Test
    public void testJwtExchange() {
        this.testUser("_test_bshutt", "123", (user, doc) -> {
            String oldJwt = doc.getString("jwt");
            assertNotNull(oldJwt);

            Document jwtLoginRes = router.route(
                    new RequestBuilder()
                            .setRoute("checkJwt")
                            .addData("jwt", oldJwt)
                            .end())
                    .getDoc();
            assertEquals(1, jwtLoginRes.get("res"));

            Document reqNewJwtResponse = router.route(
                    new RequestBuilder()
                            .setRoute("refreshJwt")
                            .addData("jwt", oldJwt)
                            .done())
                    .getDoc();
            String newJwt = reqNewJwtResponse.getString("jwt");
            assertEquals(1, reqNewJwtResponse.get("res"));

            assertEquals(oldJwt, newJwt);

            Request tryOldJwt = new RequestBuilder()
                    .setRoute("checkJwt")
                    .addData("jwt", oldJwt)
                    .end();
            Document oldJwtResponse = router.route(tryOldJwt).getDoc();
            assertEquals(1, oldJwtResponse.get("res"));

            Request tryNewJwt = new RequestBuilder()
                    .setRoute("checkJwt")
                    .addData("jwt", newJwt)
                    .end();
            Document newJwtResponse = router.route(tryNewJwt).getDoc();
            assertEquals(1, newJwtResponse.get("res"));
        });
    }

    @Test
    public void testGetUserDetailsWithJwt() {
        this.testUser((user, doc) -> {
            String jwt = doc.getString("jwt");

            Request detailsRequest = new RequestBuilder()
                    .setRoute("getUserDetails")
                    .addData("jwt", jwt)
                    .done();
            Document detailsResponse = router.route(detailsRequest).getDoc();
            assertEquals(1, detailsResponse.get("res"));
        });
    }

    @Test
    public void testGetUserDetailsWithJwtFailure() {
        this.testUser((user, doc) -> {
            String jwt = doc.getString("jwt");

            Request detailsRequest = new RequestBuilder()
                    .setRoute("getUserDetails")
                    .addData("jwt", jwt + "123") // --> wrong jwt
                    .done();
            Document detailsResponse = router.route(detailsRequest).getDoc();
            assertEquals(0, detailsResponse.get("res"));
        });
    }

    @Test
    public void testGetAllUsers() {
        this.testUser("_test_1", "123", (user1, doc1) -> {
            this.testUser("_test_2", "123", (user2, doc2) -> {
                this.testUser("_test_3", "123", (user3, doc3) -> {
                    Document allUsers = router.route(new RequestBuilder()
                            .setRoute("getAllUsers")
                            .end()).getDoc();

                    ArrayList<String> users = allUsers.get("users", ArrayList.class);
                    assertTrue(users.contains("_test_1"));
                    assertTrue(users.contains("_test_2"));
                    assertTrue(users.contains("_test_3"));

                });
            });
        });
    }

    @Test
    public void testGetUserCourses() {
        this.testUser((user, doc) -> {
            String jwt = doc.getString("jwt");

            Document login = router.route(new RequestBuilder()
                    .setRoute("getUserDetails")
                    .addData("jwt", jwt)
                    .done()).getDoc();
            assertEquals(1, login.get("res"));

            Document registerForCourse = router.route(new RequestBuilder()
                    .setRoute("registerForCourse")
                    .addData("jwt", jwt)
                    .addData("courseName", "CPE-101")
                    .done()).getDoc();
            assertEquals(1, registerForCourse.get("res"));

            Document registerForAnotherCourse = router.route(new RequestBuilder()
                    .setRoute("registerForCourse")
                    .addData("jwt", jwt)
                    .addData("courseName", "PHIL-331")
                    .done()).getDoc();
            assertEquals(1, registerForAnotherCourse.get("res"));

            Document getUserCourses = router.route(new RequestBuilder()
                    .setRoute("getUserCourses")
                    .addData("jwt", jwt)
                    .done()).getDoc();
            assertEquals(1, getUserCourses.get("res"));
            ArrayList<Document> courses = getUserCourses.get("courses", ArrayList.class);
            final boolean[] hasCPE101_A = {false};
            final boolean[] hasPHIL331_A = {false};
            courses.forEach((course) -> {
                String courseName = course.getString("courseName");
                if (courseName.equals("PHIL-331")) {
                    hasPHIL331_A[0] = true;
                } else if (courseName.equals("CPE-101")) {
                    hasCPE101_A[0] = true;
                }
            });
            assertTrue(hasCPE101_A[0]);
            assertTrue(hasPHIL331_A[0]);

            Document unregisterForCourse = router.route(new RequestBuilder()
                    .setRoute("unregisterForCourse")
                    .addData("jwt", jwt)
                    .addData("courseName", "PHIL-331")
                    .done()).getDoc();
            assertEquals(1, unregisterForCourse.get("res"));

            Document getUserCourses_B = router.route(new RequestBuilder()
                    .setRoute("getUserCourses")
                    .addData("jwt", jwt)
                    .done()).getDoc();
            assertEquals(1, getUserCourses_B.get("res"));
            ArrayList<Document> courses_B = getUserCourses_B.get("courses", ArrayList.class);
            final boolean[] hasCPE101_B = {false};
            final boolean[] hasPHIL331_B = {false};
            courses_B.forEach((course) -> {
                String courseName = course.getString("courseName");
                if (courseName.equals("PHIL-331")) {
                    hasPHIL331_B[0] = true;
                } else if (courseName.equals("CPE-101")) {
                    hasCPE101_B[0] = true;
                }
            });
            assertTrue(hasCPE101_B[0]);
            assertFalse(hasPHIL331_B[0]);

            Document unregisterForAnotherCourse = router.route(new RequestBuilder()
                    .setRoute("unregisterForCourse")
                    .addData("jwt", jwt)
                    .addData("courseName", "CPE-101")
                    .done()).getDoc();
            assertEquals(1, unregisterForAnotherCourse.get("res"));

            Document getUserCourses_C = router.route(new RequestBuilder()
                    .setRoute("getUserCourses")
                    .addData("jwt", jwt)
                    .done()).getDoc();
            assertEquals(1, getUserCourses_C.get("res"));
            ArrayList<Document> courses_C = getUserCourses_C.get("courses", ArrayList.class);
            final boolean[] hasCPE101_C = {false};
            final boolean[] hasPHIL331_C = {false};
            courses_C.forEach((course) -> {
                String courseName = course.getString("courseName");
                if (courseName.equals("PHIL-331")) {
                    hasPHIL331_C[0] = true;
                } else if (courseName.equals("CPE-101")) {
                    hasCPE101_C[0] = true;
                }
            });
            assertFalse(hasCPE101_C[0]);
            assertFalse(hasPHIL331_C[0]);
        });
    }

    @Test
    public void testTestUser() {

        this.testUser((user, doc) -> {
            assertNotNull(user);
            assertEquals("rswan", user.getString("username"));

            Request getUserRequest = new RequestBuilder()
                    .setRoute("getUser")
                    .addData("username", "rswan")
                    .end();
            Document getUserResponse = router.route(getUserRequest).getDoc();
            assertEquals(1, getUserResponse.get("res"));
            assertEquals("rswan", getUserResponse.get("user", Document.class).getString("username"));
        });

        // user no longer exists
        Request getUserRequest = new RequestBuilder()
                .setRoute("getUser")
                .addData("username", "rswan")
                .end();
        Document getUserResponse = router.route(getUserRequest).getDoc();
        assertEquals(0, getUserResponse.get("res"));

    }

    private Document createTestUser(String username, String password) {
        this.deleteUsers(new String[]{username});
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

    private void deleteCourses(String[] courseNames) {
        for (String courseName : courseNames) {
            Request deleteReq = new RequestBuilder()
                    .setRoute("deleteCourse")
                    .addData("courseName", courseName)
                    .end();
            router.route(deleteReq);
        }
    }

    private void testUser(DoWithTempUser actions) {
        this.deleteUsers(new String[]{"rswan"});

        Request createUserRequest = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", "rswan")
                .addData("firstName", "Ron")
                .addData("lastName", "Swanson")
                .addData("password", "123")
                .end();
        Document createUserRes = router.route(createUserRequest).getDoc();
        Document user = createUserRes.get("user", Document.class);

        try {
            actions.Do(user, createUserRes);
        } catch (Throwable t) {
            System.out.println("--- uncaught '"+t.toString()+"' exception ---");
            throw t;
        } finally {
            this.deleteUsers(new String[]{"rswan"});
        }
    }

    private void testUser(String username, String password, DoWithTempUser actions) {
        this.deleteUsers(new String[]{username});

        Request createUserRequest = new RequestBuilder()
                .setRoute("createUser")
                .addData("username", username)
                .addData("firstName", "Ron")
                .addData("lastName", "Swanson")
                .addData("password", password)
                .end();
        Document createUserRes = router.route(createUserRequest).getDoc();
        Document user = createUserRes.get("user", Document.class);

        try {
            actions.Do(user, createUserRes);
        } catch (Throwable t) {
            System.out.println("--- uncaught '"+t.toString()+"' exception ---");
            throw t;
        } finally {
            this.deleteUsers(new String[]{username});
        }

    }
}

interface DoWithTempUser {
    void Do(Document user, Document createUserRes);
}

