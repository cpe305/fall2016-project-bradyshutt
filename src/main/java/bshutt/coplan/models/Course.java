package bshutt.coplan.models;

import bshutt.coplan.Database;
import org.bson.Document;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class Course extends Model<Course> {

    private static final String collectionName = "courses";
    private static final String filterKey = "courseName";

    public String courseName;
    private ArrayList<String> registeredUsers;
    private Board board;


    public Course() {
        super(collectionName, filterKey);
        //this.board = new Board(courseName);
        //this.registeredUsers = new ArrayList<String>();
    }

    public static Course load(String courseName) throws Exception {
        Course course = new Course();
        Document rawDocument = course.loadModel(courseName);
        if (course.fromDoc(rawDocument) != null) {
            return course;
        } else {
            return null;
        }
    }

    public boolean validate(Document doc) throws Exception {
        return (doc.containsKey("courseName")
                && doc.containsKey("registeredUsers")
                && doc.containsKey("board"));
    }

    @Override
    public Document toDoc() throws Exception {
        Document doc = new Document();
        doc.append("courseName", this.courseName);
        doc.append("registeredUsers", this.registeredUsers);
        doc.append("board", this.board.toDoc());
        return doc;
    }

    @Override
    public Course fromDoc(Document courseDoc) throws Exception {
        this.courseName = courseDoc.getString("courseName");
        if (this.courseName == null) {
            return null;
        } else {
            this.registeredUsers = (courseDoc.containsKey("registeredUsers")
                    ? courseDoc.get("registeredUsers", ArrayList.class)
                    : new ArrayList<String>());
            this.board = (courseDoc.containsKey("board")
                    ? Board.fromDoc(courseDoc.get("board", Document.class))
                    : new Board(this.courseName));
            return this;
        }
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
        this.registeredUsers.add(username);
        this.save();
    }

    public void unregisterUser(String username) throws Exception {
        if (this.registeredUsers.contains(username)) {
            this.registeredUsers.remove(username);
            this.save();
        }
    }

    public ArrayList getRegisteredUsers() {
        return this.registeredUsers;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) throws Exception {
        this.courseName = courseName;
        this.save();
    }

    public Board getBoard() {
        return this.board;
    }
}
