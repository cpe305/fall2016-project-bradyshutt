package bshutt.coplan.handlers;

import bshutt.coplan.Database;
import bshutt.coplan.Handler;
import bshutt.coplan.Response;
import bshutt.coplan.models.Course;
import bshutt.coplan.models.User;
import com.mongodb.Block;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class Users {

    private Database db = Database.getInstance();

    public Handler getUser = (req, res) -> {
        String username = req.get("username");
        User user = null;
        try {
            user = User.load(username);
            if (user == null)
                res.err("User '" + username + "' not found!", req);
            else {
                res.append("user", user.toDoc());
                res.end(true);
            }
        } catch (Exception exc) {
            res.err(exc, req);
        }
    };

    public Handler createUser = (req, res) -> {
        User user = new User().fromDoc(req.data);
        if (!User.isUsernameAvailable(user.getUsername())) {
            res.err("Username '" + user.getUsername() + "' is not available.", req);
            return;
        }
        if (user.validate(user.toDoc())) {
            user.save();
            res.append("createdUser", "success");
            res.end(true);
        } else {
            res.err("Invalid data params for creating a user", req);
        }
    };

    public Handler removeUser = (req, res) -> {
        String username = req.data.getString("username");
        this.db.col("users").findOneAndDelete(this.db.filter("username", username));
        res.end(true);
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

    public Handler checkJwt = (req, res) -> {
        String jwt = req.get("jwt");
        Boolean validated = User.validateJwt(jwt);
        if (validated)
            res.end(true);
        else
            res.end(false);
    };

    public Handler login = (req, res) -> {
        User user = req.getUser();
        if (user.authenticate(req.get("password"))) {
            String jwt = user.giveJwt();
            res.append("jwt", jwt);
            res.append("user", user.toDoc());
            res.end(true);
        }
        else
            res.end(false);
    };

    public Handler registerForCourse = (req, res) -> {
        User user = req.getUser();
        Course course = Course.load(req.get("courseName"));
        Response.log("cname: " + course.courseName);
        user.registerForCourse(course.courseName);
        course.registerUser(user.getUsername());
        res.append("body", "User '" + user.getUsername()
                + "' registered for courses + '"
                + req.get("courseName") + "'.");
        res.end(true);
    };

    public Handler unregisterForCourse = (req, res) -> {
        User user = req.getUser();
        Course course = Course.load(req.get("courseName"));
        user.unregisterForCourse(req.get("courseName"));
        course.unregisterUser(req.get("username"));
        res.append("body", "User '" + user.getUsername()
                + "' unregistered for courses '"
                + req.get("courseName") + "'.");
        res.end(true);
    };

    public Handler getAllUsers = (req, res) -> {
        ArrayList<String> users = new ArrayList<String>();
        db.col("users").find().forEach((Block<Document>)
                (doc) -> users.add(doc.getString("username")));
        res.append("users", users);
        res.end(true);
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


