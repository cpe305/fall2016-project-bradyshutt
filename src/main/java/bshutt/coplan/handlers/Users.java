package bshutt.coplan.handlers;

import bshutt.coplan.Database;
import bshutt.coplan.Handler;
import bshutt.coplan.Response;
import bshutt.coplan.models.Course;
import bshutt.coplan.models.User;
import com.mongodb.Block;
import org.bson.Document;

import java.util.ArrayList;

public class Users {

    private Database db = Database.getInstance();

    public Handler getUser = (req, res) -> {
        String username = req.get("username");
        User user = null;
        try {
            user = new User().load(username);
            if (user == null)
                res.err("User '" + username + "' not found!", req);
            else
                res.setResponse(user.getAttributes());
        } catch (Exception exc) {
            res.err(exc, req);
        }
    };

    public Handler createUser = (req, res) -> {
        User user = new User().build(req.data);
        if (!User.isUsernameAvailable(user.username)) {
            res.err("Username '" + user.username + "' is not available.", req);
            return;
        }
        if (user.validate()) {
            user.save();
            res.append("createdUser", "success");
            res.end();
        } else {
            res.err("Invalid data params for creating a user", req);
        }
    };

    public Handler usernameIsAvailable = (req, res) -> {
        String username = req.get("username");
        Document userDoc = null;
        userDoc = this.db
                .col("users")
                .find(db.filter("username", username))
                .first();
        res.append("usernameIsAvailable", (userDoc == null));
        res.end();
    };

    public Handler authenticate = (req, res) -> {
        User user = new User().load(req.get("username"));
        if (user.authenticate(req.get("password")))
            res.setResponse("Authentication successful");
        else
            res.setResponse("Incorrect password");
    };

    public Handler registerForCourse = (req, res) -> {
        User user = new User().load(req.get("username"));
        Course course = new Course().load(req.get("courseName"));
        Response.log("cname: " + course.get("courseName"));
        user.registerForCourse(course.get("courseName"));
        course.registerUser(user.get("username"));
        res.append("body", "User '" + user.get("username")
                + "' registered for courses + '"
                + req.get("courseName") + "'.");
        res.end();
    };

    public Handler unregisterForCourse = (req, res) -> {
        User user = new User().load(req.get("username"));
        Course course = new Course().load(req.get("courseName"));
        user.unregisterForCourse(req.get("courseName"));
        course.unregisterUser(req.get("username"));
        res.append("body", "User '" + user.get("username")
                + "' unregistered for courses '"
                + req.get("courseName") + "'.");
        res.end();
    };

    public Handler getAllUsers = (req, res) -> {
        ArrayList<String> users = new ArrayList<String>();
        db.col("users").find().forEach((Block<Document>)
                (doc) -> users.add(doc.getString("username")));
        res.append("users", users);
        res.end();
    };
}

//    public Document getUser(String username) {
//        Document userDoc = null;
//        try {
//            userDoc = this.db
//                    .col("users")
//                    .find(db.filter("username", username))
//                    .first();
//        } catch (DBException exc) {
//            res.err(exc, res);
//        }
//        return userDoc;
//    }

//    public void createUser(Document user) {
//        try {
//            this.db.col("users").insertOne(user);
//        } catch (DBException exc) {
//            res.
//            e.printStackTrace();
//        }
//    }


