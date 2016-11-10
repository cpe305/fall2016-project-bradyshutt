package bshutt.coplan.models;

import bshutt.coplan.Database;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User extends Model<User> {

    private Database db = Database.getInstance();

    public String username;

    public User() {
        this.attributes = new Document();
        this.existsInDB = false;
    }

    public User load(String username) throws Exception {
        Document userDoc = this.db
                .col("users")
                .find(this.db.filter("username", username))
                .first();
        if (userDoc == null) {
            return null;
        } else {
            this.attributes = userDoc;
            this.username = userDoc.getString("username");
            this.existsInDB = true;
            return this;
        }
    }

    public User build(Document userDoc) {
        this.attributes.putAll(userDoc);
        this.username = userDoc.getString("username");
        return this;
    }

    public void save() throws Exception {
        if (this.existsInDB) {
            this.db.col("users").findOneAndReplace(
                    this.db.filter("username", this.username),
                    this.attributes);
        } else {
            this.db.col("users").insertOne(this.attributes);
            this.existsInDB = true;
        }
    }

    public boolean validate() throws Exception {
        return (this.attributes.containsKey("username")
                && this.attributes.containsKey("firstName")
                && this.attributes.containsKey("lastName")
                && this.attributes.containsKey("password"));
    }

    public boolean authenticate(String password) throws Exception {
        User user = new User().load(username);
        return (user.get("password").equals(password));
    }

    public static boolean isUsernameAvailable(String username) {
        try {
            User user = new User().load(username);
            return (user == null);
        } catch (Exception e) {
            return false;
        }
    }

    public void registerForCourse(String courseName) throws Exception {
        if (!Course.exists(courseName))
            throw new Exception("The course '" + courseName + "' does not exist!");

        ArrayList courses;
        if (this.attributes.containsKey("courses")) {
            courses = this.attributes.get("courses", ArrayList.class);
        } else {
            courses = new ArrayList();
        }
        courses.add(courseName);
        this.attributes.put("courses", courses);
        this.save();
    }

    public void unregisterForCourse(String courseName) throws Exception {
        ArrayList courses =
                (this.attributes.containsKey("courses"))
                ? this.attributes.get("courses", ArrayList.class)
                : new ArrayList();
        if (!courses.contains(courseName)) {
            throw new Exception("User '" + this.get("username")
                    + "' is not registered for course '"
                    + courseName + "'.");
        } else {
            courses.remove(courseName);
            this.attributes.put("courses", courses);
            this.save();
        }
    }
}

