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
    //private String serializedPins;
    private ArrayList<Pin> pins;


    public Course() {
        super(collectionName, filterKey);
    }

    public static Course load(String courseName) throws CourseValidationException, DatabaseException {
        Course course = Course.loadOrCreateCourse(courseName);
        return course;
    }

    public static Course createNewCourse(String courseName) throws CourseValidationException, DatabaseException {
        Course course = new Course();
        course.setCourseName(courseName);
        //course.serializedPins = JSON.serialize(new ArrayList<Pin>());
        course.pins = new ArrayList<>();
        course.registeredUsers = new ArrayList<>();
        if (!course.validate(course.toDBDoc())) {
            System.out.println("not valid!");
            throw new CourseValidationException();
        }
        course.save();
        return course;

    }

    public static Course loadOrCreateCourse(String courseName) throws DatabaseException, CourseValidationException {
        Course course = new Course();
        Document rawDocument = course.loadModel(courseName);
        if (rawDocument == null) {
            course = Course.createNewCourse(courseName);
        } else {
            course.fromDoc(rawDocument);
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


    @Override
    public Document toDBDoc() {
        Document doc = new Document();
        doc.append("courseName", this.courseName);
        doc.append("registeredUsers", this.registeredUsers);
        //doc.append("serializedPins", this.serializedPins);

        ArrayList<Document> pinDocs = new ArrayList<>();
        this.pins.forEach((pin) -> pinDocs.add(pin.toDoc()));
        doc.append("pins", pinDocs);
        return doc;
    }
    public Document toClientDoc() {
        return this.toDBDoc();
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
//            this.serializedPins = (courseDoc.containsKey("serializedPins")
//                    ? courseDoc.getString("jsonPins")
//                    : JSON.serialize(new ArrayList<Pin>()));
            this.pins = (courseDoc.containsKey("pins")
                    ? courseDoc.get("pins", ArrayList.class)
                    : new ArrayList<>());
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

    public void addPin(Pin pin) throws DatabaseException, PinValidationException, PinParseException, PinSerializationException {
        pin.validate();
        this.pins.add(pin);
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

    //public void updatePins(PinsUpdater pinsUpdater) throws PinParseException, PinSerializationException {
//        String serializedPins = this.serializedPins;
//        ArrayList<Pin> pins;
//        try {
//            pins = (ArrayList<Pin>) Document.parse(serializedPins);
//        } catch (Exception exc) {
//            throw new PinParseException("Error parsing pins");
//        }
     //   pinsUpdater.Do(this.pins);
        //String newSerializedPins;
//        try {
//            newSerializedPins = JSON.serialize(pins);
//        } catch (Exception exc) {
//            throw new PinSerializationException("Error serializing pins");
//        }
//
//        this.serializedPins = newSerializedPins;
   // }

    public String toString() {
        return "Course: { " + this.getCourseName() + ", " + this.getRegisteredUsers() + " }";

    }
}

