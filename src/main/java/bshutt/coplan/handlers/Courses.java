package bshutt.coplan.handlers;

import bshutt.coplan.Database;
import bshutt.coplan.Handler;
import bshutt.coplan.exceptions.CourseValidationException;
import bshutt.coplan.exceptions.DatabaseException;
import bshutt.coplan.models.Course;
import com.mongodb.Block;
import org.bson.Document;

import java.util.ArrayList;

public class Courses {

    private Database db = Database.getInstance();

    public Handler getCourse = (req, res) -> {
        String courseName = req.get("courseName");
        Course course = null;
        try {
            course = Course.load(courseName);
        } catch (CourseValidationException cve) {
            res.err("Error validating course", cve);
        } catch (DatabaseException e) {
            res.err(e);
        }
        if (course != null) {
            res.append("course", course.toClientDoc());
            res.end(true);
        }
        else {
            res.end(false);
        }

    };

    public Handler createCourse = (req, res) -> {
        Course course = new Course().fromDoc(req.data);
        if (course.validate(course.toClientDoc())) {
            try {
                course.save();
            } catch (DatabaseException exc) {
                res.err("Error saving course", exc);
                return;
            }
            res.append("createdCourse", "success");
            res.end(true);
        } else {
            res.err("invalid params for creating new course");
            return;
        }
    };

    public Handler deleteCourse = (req, res) -> {
        String courseName = req.get("courseName");
        try {
            this.db.col("courses").findOneAndDelete(this.db.filter("courseName", courseName));
        } catch (DatabaseException exc) {
            res.err("Database error", exc);
            return;
        }
        res.end(true, "Course deleted");
    };

    public Handler getAllCourses = (req, res) -> {
        ArrayList<String> courses = new ArrayList<String>();
        try {
            db.col("courses").find().forEach((Block<Document>)
                    (doc) -> courses.add(doc.getString("courseName")));
        } catch (DatabaseException exc) {
            res.err("Database error", exc);
            return;
        }
        res.append("courses", courses);
        res.end();

    };

}
