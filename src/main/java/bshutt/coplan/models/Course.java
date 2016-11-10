package bshutt.coplan.models;

import bshutt.coplan.Database;
import org.bson.Document;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class Course extends Model<Course> {

    public Course() {
        this.attributes = new Document();
    }

    public Course load(String courseName) throws Exception {
        Document courseDoc = this.db
                .col("courses")
                .find(this.db.filter("courseName", courseName))
                .first();
        if (courseDoc == null) {
            return null;
        } else {
            this.attributes = courseDoc;
            this.existsInDB = true;
        }
        return this;
    }

    public Course build(Document courseDoc) {
        this.attributes.putAll(courseDoc);
        return this;
    }

    public void save() throws Exception {
        if (this.existsInDB) {
            this.db.col("courses").findOneAndReplace(
                    this.db.filter("courseName", this.get("courseName")),
                    this.attributes);
        } else {
            this.db.col("courses").insertOne(this.attributes);
            this.existsInDB = true;
        }
    }

    public boolean validate() throws Exception {
        return ( this.attributes.containsKey("courseName")
                && this.attributes.containsKey("registeredUsers"));
    }

    public static boolean exists(String courseName) throws Exception {
        Database db = Database.getInstance();
        Document doc = db
                .col("courses")
                .find(db.filter("courseName", courseName))
                .first();
        return (doc != null);
    }

    public void registerUser(String username) throws Exception {
        this.db.col("courses")
                .find(this.db.filter("courseName", this.get("courseName")))
                .first()
                .get("registeredUsers", ArrayList.class)
                .add(username);
    }

    public void unregisterUser(String username) throws Exception {
        this.db.col("courses")
                .find(this.db.filter("courseName", this.get("courseName")))
                .first()
                .get("users", Document.class)
                .remove(username);
    }
}
