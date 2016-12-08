package bshutt.coplan.handlers;

import java.util.ArrayList;

import bshutt.coplan.models.Pin;
import com.mongodb.Block;
import org.bson.Document;

import bshutt.coplan.Database;
import bshutt.coplan.Handler;
import bshutt.coplan.exceptions.*;
import bshutt.coplan.models.Course;
import bshutt.coplan.models.User;

public class Users {

    private Database db = Database.getInstance();

    public Handler getUser = (req, res) -> {
        String username = req.get("username");
        User user = null;
        try {
            user = User.load(username);
            if (user == null) {
                res.err("User '" + username + "' not found!");
                return;
            } else {
                res.append("user", user.toDoc());
                res.end(true);
            }
        } catch (UserDoesNotExistException uDNE) {
            res.append("message", "User '" + username + "' does not exist.");
            res.end(false);
        } catch (Exception exc) {
            res.err(exc);
            return;
        }
    };

    public Handler getUserDetails = (req, res) -> {
        String jwt = null;
        try {
            jwt = req.user.getJwt();
        } catch (DatabaseException e) {
            res.err("DB error with getting the JWT", e);
        }
        boolean jwtIsValid = false;
        try {
            jwtIsValid = User.validateJwt(jwt);
        } catch (JwtException e) {
            res.err("Jwt error", e);
            return;
        }

        if (!jwtIsValid) {
            res.append("errorMessage", "JWT not validated.");
            res.end(false);
        }

        res.append("message", "JWT validated successfully.");
        res.append("user", req.user.toClientDoc(true));

//        try {
//            res.append("courses", req.user.getCourseDetails());
//        } catch (DatabaseException | CourseValidationException exc) {
//            res.err("Error getting course details", exc);
//            return;
//        }
        res.end(true);
    };

    public Handler getUserCourses = (req, res) -> {
        try {
            res.append("courses", req.user.getCourseDetails().get("courses"));
        } catch (DatabaseException | CourseValidationException exc) {
            res.err("Error getting course details", exc);
            return;
        }
        res.end(true);
    };

    public Handler userAddPinToBoard = (req, res) -> {
        String courseName = req.get("courseName");
        Course course = null;
        try {
            course = Course.load(courseName);
        } catch (CourseValidationException | DatabaseException exc) {
            res.err("error loading course", exc);
            return;
        }
        Pin pin = Pin.fromDoc(req.get("pin", Document.class));
        try {
            course.addPin(pin);
        } catch (DatabaseException exc) {
            res.err("error adding pin to course", exc);
        }
        res.append("user", req.user);
        res.end(true);
    };

    public Handler createUser = (req, res) -> {
        User user = new User().fromDoc(req.data);
        if (!User.isUsernameAvailable(user.getUsername())) {
            res.append("errorMessage", "Username '"+user.getUsername()+"' is not available.");
            res.end(false);
            return;
        }
        if (user.validate(user.toDoc())) {
            try {
                user.save();
            } catch (DatabaseException exc) {
                res.err("There was an error saving the user", exc);
                return;
            }

            String jwt = null;
            try {
                jwt = user.giveJwt();
            } catch (DatabaseException exc) {
                res.err("There was an error with the JWT", exc);
                return;
            }

            res.append("createdUser", "success");
            res.append("jwt", jwt);
            res.append("user", user.toClientDoc(true));
            //res.append("courses", null);
            res.end(true);
        } else {
            res.err("Invalid data params for creating a user");
            return;
        }
    };

    public Handler removeUser = (req, res) -> {
        String username = req.data.getString("username");
        try {
            this.db.col("users").findOneAndDelete(this.db.filter("username", username));
            this.db.col("courses").deleteMany(this.db.filter("registeredUsers", username));
        } catch (DatabaseException exc) {
            res.err("There was an error removing the user", exc);
            return;
        }
        res.end(true);
    };

    public Handler usernameIsAvailable = (req, res) -> {
        String username = req.get("username");
        Document userDoc = null;
        try {
            userDoc = this.db
                    .col("users")
                    .find(db.filter("username", username))
                    .first();
        } catch (DatabaseException exc) {
            res.err("There was a Database error", exc);
            return;
        }
        res.append("usernameIsAvailable", (userDoc == null));
        res.end();
    };

    public Handler checkJwt = (req, res) -> {
        String jwt = req.get("jwt");
        Boolean validated = null;
        try {
            validated = User.validateJwt(jwt);
        } catch (JwtException exc) {
            res.err("There was a JWT validation error", exc);
            return;
        }
        if (validated)
            res.end(true);
        else
            res.end(false);
    };

    public Handler login = (req, res) -> {
        String username = req.get("username");
        String password = req.get("password");
        User user = null;
        try {
            user = User.login(username, password);
        } catch (InvalidPasswordException ipe) {
            res.append("errorMessage", "Invalid password.").end(false);
            return;
        } catch (UserDoesNotExistException udne) {
            res.append("errorMessage", "User '" + username + "' does not exist.").end(false);
            return;
        }

        String jwt = null;
        try {
            jwt = user.giveJwt();
        } catch (DatabaseException exc) {
            res.err("There was a Database error", exc);
            return;
        }

        res.append("jwt", jwt);
        res.append("user", user.toClientDoc(true));
        res.end(true);
    };

    public Handler registerForCourse = (req, res) -> {
        User user = req.user;
        Course course = null;
        try {
            course = Course.load(req.get("courseName"));
        } catch (CourseValidationException exc) {
            res.err("Error validating course", exc);
            return;
        } catch (DatabaseException exc) {
            res.err("There was a Database error", exc);
            return;
        }

        try {
            course.registerUser(user.getUsername());
        } catch (CourseRegistrationException exc) {
            res.append("message", "User '" + user.getUsername()
                    + "' is already registered for '"
                    + req.get("courseName") + "'.");
            res.end(false);
            return;
        }

        try {
            user.registerForCourse(course.courseName);
        } catch (CourseRegistrationException exc) {
            res.err("There was a problem registering for that course", exc);
            return;
        } catch (DatabaseException exc) {
            res.err("There was a Database error", exc);
            return;
        }

        res.append("message", "User '" + user.getUsername()
                + "' registered for course + '"
                + req.get("courseName") + "'.");
        //res.append("course", course.toClientDoc(true));
        res.append("user", user.toClientDoc(true));
        res.end(true);
    };

    public Handler unregisterForCourse = (req, res) -> {
        User user = null;
        try {
            user = req.getUser();
        } catch (UserDoesNotExistException exc) {
            res.err("User does not exist", exc);
            return;
        } catch (JwtException exc) {
            res.err("There was a JWT validation error", exc);
            return;
        }

        Course course = null;
        try {
            course = Course.load(req.get("courseName"));
        } catch (CourseValidationException exc) {
            res.err("Error validating course", exc);
            return;
        } catch (DatabaseException exc) {
            res.err(exc);
            return;
        }

        try {
            user.unregisterForCourse(req.get("courseName"));
            course.unregisterUser(req.get("username"));
        } catch (CourseRegistrationException exc) {
            res.err("There was an error with registration", exc);
            return;
        } catch (DatabaseException exc) {
            res.err("There was a database error.", exc);
            return;
        }
        res.append("message", "User '" + user.getUsername()
                + "' unregistered for courses '"
                + req.get("courseName") + "'.");
        //res.append("course", course.toClientDoc(true));
        res.append("user", user.toClientDoc(true));
        res.end(true);
    };

    public Handler getAllUsers = (req, res) -> {
        ArrayList<String> users = new ArrayList<String>();
        try {
            db.col("users").find().forEach((Block<Document>)
                    (doc) -> users.add(doc.getString("username")));
        } catch (DatabaseException exc) {
            res.err("There was a database error.", exc);
            return;
        }
        res.append("users", users);
        res.end(true);
    };


    public Handler refreshJwt = (req, res) -> {
        String jwt = req.get("jwt");
        Boolean validated = null;
        try {
            validated = User.validateJwt(jwt);
        } catch (JwtException exc) {
            res.err("There was a JWT validation error", exc);
            return;
        }
        if (validated) {
            String newJwt = null;
            try {
                newJwt = req.user.giveJwt();
            } catch (DatabaseException exc) {
                res.err("There was a database error.", exc);
                return;
            }
            res.append("jwt", newJwt);
            res.append("user", req.user.toDoc());
            res.end(true);
        } else {
            res.append("errorMessage", "The the old JWT did not match.");
            res.end(false);
        }
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


