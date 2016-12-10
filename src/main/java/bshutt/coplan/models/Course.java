package bshutt.coplan.models;

import bshutt.coplan.Database;
import bshutt.coplan.exceptions.*;
import bshutt.coplan.exceptions.PinParseException;
import org.bson.Document;

import java.util.ArrayList;

public class Course extends Model<Course> {

    private static final String collectionName = "courses";
    private static final String filterKey = "courseName";

    public String courseName;
    private ArrayList<String> registeredUsers;
    public ArrayList<Pin> pins;


    public Course() {
        super(collectionName, filterKey);
    }

    public static Course load(String courseName) throws CourseValidationException, DatabaseException {
        return Course.loadOrCreateCourse(courseName);
    }

    private static Course createNewCourse(String courseName) throws CourseValidationException, DatabaseException {
        Course course = new Course();
        course.courseName = courseName;
        course.pins = new ArrayList<>();
        course.registeredUsers = new ArrayList<>();
        if (!course.validate(course.serialize())) {
            throw new CourseValidationException();
        }
        course.save();
        return course;

    }

    private static Course loadOrCreateCourse(String courseName) throws DatabaseException, CourseValidationException {
        Course course = new Course();
        Document rawDocument = course.loadModel(courseName);
        if (rawDocument == null) {
            course = Course.createNewCourse(courseName);
        } else {
            course.deserialize(rawDocument);
        }
        return course;
    }

    public boolean validate(Document doc) {
        return (doc.containsKey("courseName")
                && doc.containsKey("registeredUsers")
                && doc.containsKey("pins"));
    }

    @Override
    public String getFilterValue() {
        return this.courseName;
    }



    public Document toClientDoc() {
        return this.serialize();
    }

    public static boolean exists(String courseName) {
        Database db = Database.getInstance();
        Document doc;
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
        if (this.registeredUsers != null) {
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

    public void addPin(Pin pin) throws DatabaseException, PinValidationException, PinParseException, PinSerializationException {
        pin.validate();
        this.pins.add(pin);
        this.save();
    }

    public static ArrayList<Course> toCourseList(ArrayList<Document> documents) {
        ArrayList<Course> courses = new ArrayList<>();
        documents.forEach(document -> {
            Course course = new Course();
            courses.add(course.deserialize(document));
        });
        return courses;
    }

    public static ArrayList<Document> toDocList(ArrayList<Course> courses) {
        ArrayList<Document> documents = new ArrayList<>();
        courses.forEach(course -> {
            if (course != null)
                documents.add(course.serialize());
        });
        return documents;
    }

    @Override
    public Document serialize() {
        Document doc = new Document();
        doc.append("courseName", this.courseName);
        doc.append("registeredUsers", this.registeredUsers);
        doc.append("pins", Pin.toDocList(this.pins));
        return doc;
    }

    public Course deserialize(Document doc) {
        this.courseName = doc.getString("courseName");
        this.registeredUsers = doc.get("registeredUsers", ArrayList.class);
        ArrayList<Document> pinDocs = doc.get("pins", ArrayList.class);
        this.pins = Pin.toPinList(pinDocs);
        return null;
    }

    public String toString() {
        return "<" + this.courseName + ">";
    }

    public void printDetails() {
        final String[] usersString = {"\n"};
        this.registeredUsers.forEach(user -> usersString[0] +="\t\t\t'"+user+"'\n");
        usersString[0] +="\t\t";

        final String[] pinsString = {"\n"};
        this.pins.forEach(pin -> pinsString[0] +="\t\t\t'"+pin.toString()+"'\n");
        pinsString[0] +="\t\t";

        String s = "Details for Course <"+this.courseName+">: {\n" +
                "\t\tregisteredUsers: [" + usersString[0] + "]\n" +
                "\t\tpins: [" + pinsString[0] + "]\n";
    }
}

