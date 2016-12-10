package bshutt.coplan.models;

import bshutt.coplan.Database;
import com.mongodb.MongoWriteException;
import org.bson.Document;
import org.junit.*;

import static org.junit.Assert.*;

public class CourseTest {

    @BeforeClass
    public static void setup() throws Exception {
        //Database.getInstance().changeDatabase("junit");
    }

    @AfterClass
    public static void teardown() throws Exception {
        //Database.getInstance().db.drop();
    }

    @Test
    public void testCreateAndLoadCourse() throws Exception {
//        String courseName = "CPE-305";
//        Document courseDoc = new Document("courseName", courseName);
//
//        Course courseA = new Course().fromDoc(courseDoc);
//        assertEquals(courseName, courseA.getCourseName());
//        assertNotNull(courseA.getRegisteredUsers());
//        assertNotNull(courseA.getBoard());
//
//        courseA.save();
//
//        Course courseB = Course.load(courseName);
//        assertEquals(courseName, courseB.getCourseName());
//        assertNotNull(courseB.getRegisteredUsers());
//        assertNotNull(courseB.getBoard());
    }

    @Test
    public void testDuplicateCourseNameError() throws Exception {
//        String courseName = "CPE-123";
//        Document courseDoc = new Document("courseName", courseName);
//        Course courseA = new Course().fromDoc(courseDoc);
//        Course courseB = new Course().fromDoc(courseDoc);
//
//        courseA.save();
//        System.out.println(courseA.toDoc().toJson());
//        Exception exc = null;
//        try {
//            courseB.save();
//        } catch(MongoWriteException e) {
//            exc = e;
//        }
//        assertNotNull(exc);
    }

}