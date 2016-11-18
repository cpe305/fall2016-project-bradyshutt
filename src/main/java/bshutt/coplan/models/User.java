package bshutt.coplan.models;

import bshutt.coplan.Database;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class User extends Model<User> {
    private static final String collectionName = "users";
    private static final String filterKey = "username";

    private String username;
    private ArrayList<String> courses;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;

    public User() {
        super(collectionName, filterKey);
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public static User load(String username) throws Exception {
        User user = new User();
        Document rawDocument = user.loadModel(username);
        if (user.fromDoc(rawDocument) != null) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User fromDoc(Document userDoc) throws Exception {
        this.username = userDoc.getString("username");
        this.firstName = userDoc.getString("firstName");
        this.lastName = userDoc.getString("lastName");
        this.email = userDoc.getString("email");
        this.courses = userDoc.get("courses", ArrayList.class);
        if (this.courses == null)
            this.courses = new ArrayList<String>();
        if (userDoc.containsKey("hashedPassword")) {
            this.hashedPassword = userDoc.getString("hashedPassword");
        } else if (userDoc.containsKey("password")) {
            String textPass = userDoc.getString("password");
            this.hashedPassword = BCrypt.hashpw(textPass, BCrypt.gensalt(12));
        } else {
            this.hashedPassword = null;
        }
        return this;
    }

    public boolean validate(Document doc) throws Exception {
        return (doc.containsKey("username")
                && doc.containsKey("firstName")
                && doc.containsKey("lastName")
                && doc.containsKey("courses")
                && doc.containsKey("hashedPassword"));
    }

    @Override
    public Document toDoc() throws Exception {
        Document doc = new Document("username", this.username)
                .append("firstName", this.firstName)
                .append("lastName", this.lastName)
                .append("courses", this.courses)
                .append("hashedPassword", this.hashedPassword)
                .append("email", this.email);
        return doc;
    }

    public boolean authenticate(String password) throws Exception {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

    public static boolean isUsernameAvailable(String username) {
        try {
            User user = User.load(username);
            return (user == null);
        } catch (Exception e) {
            return true;
        }
    }

    public void registerForCourse(String courseName) throws Exception {
        if (!Course.exists(courseName))
            throw new Exception("The course '" + courseName + "' does not exist!");
        this.courses.add(courseName);
        Course.load(courseName).registerUser(this.username);
        this.hasUpdates = true;
        this.save();
    }

    public void unregisterForCourse(String courseName) throws Exception {
        this.courses.remove(courseName);
        this.hasUpdates = true;
        this.save();
    }

    public ArrayList getCourses() {
        return this.courses;
    }

    public String toString() {
        try {
            return "<"+this.username+"> "+ this.toDoc().toJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

