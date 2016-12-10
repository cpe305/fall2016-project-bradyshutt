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
    public ArrayList<String> registeredUsers;
    public ArrayList<Pin> pins;


    public Course() {
        super(collectionName, filterKey);
    }

    public static Course load(String courseName) throws CourseValidationException, DatabaseException {
        Course course = Course.loadOrCreateCourse(courseName);
        System.out.println("Loaded course <" + course.courseName + "> successfully." + course.toString());
        return course;
    }

    public static Course createNewCourse(String courseName) throws CourseValidationException, DatabaseException {
        Course course = new Course();
        course.courseName = courseName;
        course.pins = new ArrayList<>();
        course.registeredUsers = new ArrayList<>();
        if (!course.validate(course.serialize())) {
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


//    @Override
//    public Document toDxBDoc() {
//        Document doc = new Document();
//        doc.append("courseName", this.courseName);
//        doc.append("registeredUsers", this.registeredUsers);
//        doc.append("pins", Pin.pinsToDocs(this.pins));
//        return doc;
//    }

    public Document toClientDoc() {
        Document doc = this.serialize();
        //filter some if necesarry
        return doc;
    }

//    @Override
//    public Course fromDoc(Document courseDoc) {
//        this.courseName = courseDoc.getString("courseName");
//        if (this.courseName == null) {
//            return null;
//        } else {
//            this.registeredUsers = (courseDoc.containsKey("registeredUsers")
//                    ? courseDoc.get("registeredUsers", ArrayList.class)
//                    : new ArrayList<String>());
////            this.serializedPins = (courseDoc.containsKey("serializedPins")
////                    ? courseDoc.getString("jsonPins")
////                    : JSON.serialize(new ArrayList<Pin>()));
//            this.pins = (courseDoc.containsKey("pins")
//                    ? courseDoc.get("pins", ArrayList.class)
//                    : new ArrayList<>());
//            return this;
//        }
//    }

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
            course.printDetails();
            Document doc = course.serialize();
            documents.add(doc);
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
        System.out.println("\n\n"+s+"\n\n");
    }
}

