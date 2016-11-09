package bshutt.coplan.handlers;

import bshutt.coplan.DBException;
import bshutt.coplan.Database;
import bshutt.coplan.Handler;
import bshutt.coplan.models.Course;
import org.bson.Document;

public class Courses {

    private Database db = Database.getInstance();

    public Handler getCourse = (req, res) -> {
        String courseID = req.get("courseID");
        Course course = null;
        try {
            course = new Course().load(courseID);
        } catch (DBException exc) {
            res.err(exc);
            return;
        }
        if (course != null) {
            res.setResponse(course.getAttributes());
        }
    };

    public Handler createCourse = (req, res) -> {
        Course course = new Course().build(req.data);

    };
}
