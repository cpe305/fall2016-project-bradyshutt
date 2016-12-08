package bshutt.coplan.models;

import bshutt.coplan.Database;
import bshutt.coplan.exceptions.CourseValidationException;
import bshutt.coplan.exceptions.DatabaseException;
import bshutt.coplan.exceptions.CourseRegistrationException;
import com.mongodb.util.JSON;
import org.bson.Document;

import java.util.ArrayList;

public class Course extends Model<Course> {

    private static final String collectionName = "courses";
    private static final String filterKey = "courseName";

    public String courseName;
    private ArrayList<String> registeredUsers;
    //private ArrayList<Pin> pins;
    private String jsonPins;


    public Course() {
        super(collectionName, filterKey);
    }

    public static Course load(String courseName) throws CourseValidationException, DatabaseException {
        Course course = Course.loadOrCreateCourse(courseName);
        return course;
    }

    public static Course loadOrCreateCourse(String courseName) throws CourseValidationException, DatabaseException {
        Course course = new Course();
        Document rawDocument = course.loadModel(courseName);
        if (rawDocument == null) {
            //course doesn't exist; create course
            course.setCourseName(courseName);
            //course.board = new Board(courseName);
            //course.pins = new ArrayList<Pin>();
            course.jsonPins = JSON.serialize(new ArrayList<Pin>());
            course.registeredUsers = new ArrayList<String>();
            if (!course.validate(course.toDoc())) {
                System.out.println("not valid!");
                throw new CourseValidationException();
            }
            course.save();
            return course;
        }
        course.fromDoc(rawDocument);
        return course;
    }

    public boolean validate(Document doc) {
        return (doc.containsKey("courseName")
                && doc.containsKey("registeredUsers")
                && doc.containsKey("jsonPins"));
    }

    @Override
    public String getFilterValue() {
        return this.courseName;
    }

    @Override
    public Document toDoc() {
        Document doc = new Document();
        doc.append("courseName", this.courseName);
        doc.append("registeredUsers", this.registeredUsers);
        doc.append("jsonPins", this.jsonPins);
        return doc;
    }
    public Document toClientDoc() {
        return this.toDoc();
    }

    @Override
    public Course fromDoc(Document courseDoc) {
        this.courseName = courseDoc.getString("courseName");
        if (this.courseName == null) {
            return null;
        } else {
            this.registeredUsers = (courseDoc.containsKey("registeredUsers")
                    ? courseDoc.get("registeredUsers", ArrayList.class)
                    : new ArrayList<String>());
//            this.pins = (courseDoc.containsKey("pins")
//                    ? courseDoc.get("pins", ArrayList.class)
//                    : new ArrayList<Pin>());
            this.jsonPins = (courseDoc.containsKey("jsonPins")
                    ? JSON.serialize(courseDoc.get("jsonPins", ArrayList.class))
                    : JSON.serialize(new ArrayList<Pin>()));
            return this;
        }
    }

    public static boolean exists(String courseName) {
        Database db = Database.getInstance();
        Document doc = null;
        try {
            doc = db
                    .col("courses")
                    .find(db.filter("courseName", courseName))
                    .first();
        } catch (DatabaseException e) {
            return false;
        }
        return (doc != null);
    }

    public void registerUser(String username) throws CourseRegistrationException {
        if (this.registeredUsers.contains(username)) {
            throw new CourseRegistrationException("User '" + username + "' is already registered for'"+this.courseName+"'.");
        }
        this.registeredUsers.add(username);
        try {
            this.save();
        } catch (DatabaseException e) {
            throw new CourseRegistrationException("Error updating course.");
        }
    }

    public void unregisterUser(String username) throws CourseRegistrationException {
        if (this.registeredUsers.contains(username)) {
            this.registeredUsers.remove(username);
            try {
                this.save();
            } catch (DatabaseException e) {
                throw new CourseRegistrationException("Error updating course.");
            }
        }
    }

    public void addPin(Pin pin) throws DatabaseException {
        ArrayList<Pin> pinsArray = (ArrayList<Pin>) JSON.parse(this.jsonPins);
        pinsArray.add(pin);
        this.jsonPins = JSON.serialize(pinsArray);
        this.save();
    }

    public ArrayList getRegisteredUsers() {
        return this.registeredUsers;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    //public ArrayList<Pin> getPins() {
    public ArrayList<Pin> getPins() {
        return (ArrayList<Pin>) JSON.parse(this.jsonPins);
    }
}
