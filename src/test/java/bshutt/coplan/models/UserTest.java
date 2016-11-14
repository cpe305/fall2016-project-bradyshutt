package bshutt.coplan.models;

import bshutt.coplan.Database;
import com.mongodb.MongoWriteException;
import org.bson.Document;
import org.junit.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTest {

    @BeforeClass
    public static void setup() throws Exception {
        Database.getInstance().changeDatabase("junit");
    }

    @AfterClass
    public static void teardown() throws Exception {
        Database.getInstance().db.drop();
    }

    @Before
    public void setupTestUserAndCourse() throws Exception {
        Document userDoc = new Document("username", "_bshutt")
                .append("firstName", "Brady")
                .append("lastName", "Shutt")
                .append("password", "password123");
        User testUser = new User().fromDoc(userDoc);
        testUser.save();

        Document courseDoc = new Document("courseName", "CPE-305");
        Course course = new Course().fromDoc(courseDoc);
        course.save();
    }

    @After
    public void destroyTestUser() throws Exception {
        Database db = Database.getInstance();
        db.col("users").findOneAndDelete(db.filter("username", "_bshutt"));
        db.col("courses").findOneAndDelete(db.filter("courseName", "CPE-305"));
    }

    @Test
    public void testCreateAndLoadUser() throws Exception {
        String username = "bshutt";
        String password = "password123";

        Document userDoc = new Document("username", username)
                .append("firstName", "Brady")
                .append("lastName", "Shutt")
                .append("password", "password123");

        User userA = new User().fromDoc(userDoc);
        assertEquals(username, userA.getUsername());

        userA.save();

        User loadedUser = User.load(username);
        assertEquals(username, loadedUser.getUsername());
        ArrayList<String> courses = loadedUser.getCourses();
    }

    @Test
    public void testDuplicateCourseNameError() throws Exception {
        String username = "b-rad";
        String password = "password123";

        Document userDoc = new Document("username", username)
                .append("firstName", "Brady")
                .append("lastName", "Shutt")
                .append("password", "password123");

        User userA = new User().fromDoc(userDoc);
        User userB = new User().fromDoc(userDoc);
        assertEquals(username, userA.getUsername());
        assertEquals(username, userB.getUsername());

        Exception exc = null;

        userA.save();
        try {
            userB.save();
        } catch (MongoWriteException e) {
            exc = e;
        }
        assertNotNull(exc);
    }

    @Test
    public void testLoadUser() throws Exception {
        User user = User.load("_bshutt");
        assertEquals("_bshutt", user.getUsername());
    }

    @Test
    public void testPasswordIsEncrypted() throws Exception {
        User user = User.load("_bshutt");
        assertNotEquals("password123", user.getHashedPassword());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        User user = User.load("_bshutt");
        boolean ans = user.authenticate("password123");
        assertTrue(ans);
    }

    @Test
    public void testLoginFail() throws Exception {
        User user = User.load("_bshutt");
        assertFalse(user.authenticate("password321"));
    }

    @Test
    public void testUserRegisterForNonexistentCourse() throws Exception {
        User userA = User.load("_bshutt");
        assertFalse(userA.getCourses().contains("CPE-123"));

        Exception exc = null;
        try {
            userA.registerForCourse("CPE-123");
        } catch (Exception e) {
            exc = e;
        }
        assertNotNull(exc);
    }

    @Test
    public void testUserRegisterForCourse() throws Exception {
        User userA = User.load("_bshutt");
        Course courseA = Course.load("CPE-305");

        assertFalse(courseA.getRegisteredUsers().contains("_bshutt"));
        assertFalse(userA.getCourses().contains("CPE-305"));

        userA.registerForCourse("CPE-305");
        userA.save();

        Course courseB = Course.load("CPE-305");
        User userB = User.load("_bshutt");

        assertTrue(courseB.getRegisteredUsers().contains("_bshutt"));
        assertTrue(userB.getCourses().contains("CPE-305"));
    }

}
