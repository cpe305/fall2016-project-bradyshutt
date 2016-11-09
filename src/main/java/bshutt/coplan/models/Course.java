package bshutt.coplan.models;

import bshutt.coplan.DBException;
import org.bson.Document;

public class Course extends Model<Course> {

    public Course() {
        this.attributes = new Document();
    }

    public Course load(String courseID) throws DBException {
        Course course = new Course();
        Document courseDoc = course.db
                .col("courses")
                .find(course.db.filter("_id", courseID))
                .first();
        if (courseDoc == null) {
            return null;
        } else {
            course.attributes = courseDoc;
            course.existsInDB = true;
        }
        return course;
    }

    public Course build(Document courseDoc) {
        this.attributes.putAll(courseDoc);
        return this;
    }

    public void save() throws DBException {
        if (this.existsInDB) {
            this.db.col("courses").findOneAndReplace(
                    this.db.filter("_id", this.get("_id")),
                    this.attributes);
        } else {
            this.db.col("courses").insertOne(this.attributes);
            this.existsInDB = true;

        }

    }

    public boolean validate() throws DBException {
        return (this.attributes.containsKey("courseName"));
    }
}
