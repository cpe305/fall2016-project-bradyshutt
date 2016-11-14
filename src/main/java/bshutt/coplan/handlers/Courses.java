package bshutt.coplan.handlers;

import bshutt.coplan.Database;
import bshutt.coplan.Handler;
import bshutt.coplan.models.Course;
import com.mongodb.Block;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.util.ArrayList;

public class Courses {

    private Database db = Database.getInstance();

    public Handler getCourse = (req, res) -> {
        String courseName = req.get("courseName");
        Course course = Course.load(courseName);
        if (course != null) {
            res.setResponse(course.toDoc());
        }
    };

    public Handler createCourse = (req, res) -> {
        Course course = new Course().fromDoc(req.data);
        if (course.validate(course.toDoc())) {
            course.save();
            res.append("createdCourse", "success");
            res.end();
        } else {
            res.err("invalid params for creating new course", req);
        }
    };

    public Handler deleteCourse = (req, res) -> {
        String courseName = req.get("courseName");
        this.db.col("courses").findOneAndDelete(this.db.filter("courseName", courseName));
        res.end("Course deleted");
    };

    public Handler getAllCourses = (req, res) -> {
        ArrayList<String> courses = new ArrayList<String>();
        db.col("courses").find().forEach((Block<Document>)
                (doc) -> courses.add(doc.getString("courseName")));
        res.append("courses", courses);
        res.end();

    };

}
