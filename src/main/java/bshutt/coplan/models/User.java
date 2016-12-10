package bshutt.coplan.models;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.bson.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import bshutt.coplan.exceptions.*;

public class User extends Model<User> {
    private static final String collectionName = "users";
    private static final String filterKey = "username";
    private static final String SECRET_KEY= "123j3kkk89dfd73kk7zh";
    private static final String ISSUER = "http://coplan.bshutt.com/";

    public String username;
    public ArrayList<Course> courses;
    public String firstName;
    public String lastName;
    public String email;
    public String jwt;
    public String hashedPassword;


    public User() {
        super(collectionName, filterKey);
    }


//    public ArrayList<Course> getCourses() { return this.courses; }
//    public String getEmail() { return this.email; }
//    public String getFirstName() { return this.firstName; }
//    public String getHashedPassword() { return this.hashedPassword; }
//    public String getLastName() { return this.lastName; }
//    public String getUsername() { return this.username; }
//    public void setCourses(ArrayList<Course> courses) { this.courses = courses; }
//    public void setEmail(String email) { this.email = email; }
//    public void setFirstName(String firstName) { this.firstName = firstName; }
//    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
//    public void setJwt(String jwt) { this.jwt = jwt; }
//    public void setLastName(String lastName) { this.lastName = lastName; }
//    public void setUsername(String username) { this.username = username; }

    public String getJwt() {
        return this.jwt;
    }

    public static User login(String username, String password) throws UserDoesNotExistException {
        User user = User.load(username);
        if (user.authenticate(password))
            return user;
        else
            throw new InvalidPasswordException();
    }

    public static User load(String username) throws UserDoesNotExistException {
        User user = new User();
        Document rawDocument = user.loadModel(username);
        System.out.println("~~~~LOADING USER rawDoc: " + rawDocument.toJson());
        if (rawDocument == null) {
            throw new UserDoesNotExistException("User that does not exist: '"+username+"'");
        }
        return user.deserialize(rawDocument);
    }

//    @Override
//    public User fromDoc(Document userDoc) {
//        this.username = userDoc.getString("username");
//        this.firstName = userDoc.getString("firstName");
//        this.lastName = userDoc.getString("lastName");
//        this.email = userDoc.getString("email");
//        this.courses = userDoc.get("courses", ArrayList.class);
//        if (this.courses == null)
//            this.courses = new ArrayList<Course>();
//        if (userDoc.containsKey("hashedPassword")) {
//            this.hashedPassword = userDoc.getString("hashedPassword");
//        } else if (userDoc.containsKey("password")) {
//            String textPass = userDoc.getString("password");
//            this.hashedPassword = BCrypt.hashpw(textPass, BCrypt.gensalt(12));
//        } else {
//            this.hashedPassword = null;
//        }
//        return this;
//    }

    public boolean validate(Document doc) {
        return (doc.containsKey("username")
                && doc.getString("username") != null
                && doc.containsKey("firstName")
                && doc.getString("firstName") != null
                && doc.containsKey("lastName")
                && doc.getString("lastName") != null
                && doc.containsKey("courses")
                && doc.get("courses") != null
                && doc.containsKey("hashedPassword")
                && doc.get("hashedPassword") != null);
    }

    @Override
    public String getFilterValue() {
        return this.username;
    }

    //@Override
    //public Document toDBDoc() {
//        for (Course course : this.getCourses()) {
//            coursesList.add(course.toDBDoc());
//
//        }
//        this.courses.forEach((course) -> {
//        });
//        ArrayList<Document> coursesList = new ArrayList<>();
//        Document doc = new Document("username", this.username)
//                .append("firstName", this.firstName)
//                .append("lastName", this.lastName)
//                .append("courses", coursesList)
//                .append("hashedPassword", this.hashedPassword)
//                .append("email", this.email)
//                .append("jwt", this.jwt);
//        return doc;
//    }

    public Document toClientDoc() {
        Document doc = this.serialize();
        doc.remove("hashedPassword");
        doc.remove("jwt");
        return doc;
    }

//    public Document toClientDoc() {
//        Document doc = new Document("username", this.username)
//                .append("firstName", this.firstName)
//                .append("lastName", this.lastName)
//                .append("email", this.email);
//        if (includeCourseDetails) {
//            try {
//                this.replaceCourseNamesWithDetails(doc);
//                doc.append("courses", .getData("courses"));
//            } catch (DatabaseException | CourseValidationException exc) {
//                doc.append("courses", null);
//            }
//        } else {
//            doc.append("courses", this.courses);
//        }
//        return doc;
//    }

//    public Document replaceCourseNamesWithDetails() throws DatabaseException, CourseValidationException {
//        for (String courseName : this.courses) {
//            Course course = Course.load(courseName);
//            Document courseDoc = course.toClientDoc();
//            courses.add(courseDoc);
//        }
//        Document doc = new Document();
//        doc.append("courses", courses);
//        return doc;
//    }

    public Document replaceNamesWithDetails(Document doc) throws DatabaseException, CourseValidationException {
        ArrayList<Document> courses = new ArrayList<Document>();
        for (String courseName : doc.get("courses", String[].class)) {
            Course course = Course.load(courseName);
            courses.add(course.toClientDoc());
        }
        doc.put("courses", courses.toArray());
        return doc;
    }

    public boolean authenticate(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

    public String giveJwt() throws DatabaseException {
        final long iat = System.currentTimeMillis() / 1000L;
        final long exp = iat + (24L*60L*60L);
        final JWTSigner signer = new JWTSigner(User.SECRET_KEY);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", User.ISSUER);
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("username", this.username);
        final String jwt  = signer.sign(claims);
        this.jwt = jwt;
        this.save();
        return jwt;
    }

    public static boolean validateJwt(String jwt) throws JwtException {
        final JWTVerifier verifier = new JWTVerifier(User.SECRET_KEY);
        final Map<String, Object> claims;
        try {
            claims = verifier.verify(jwt);
        } catch (Exception exc) {
            throw new JwtException("Jwt error occurred.", exc);
        }
        String username = (String) claims.get("username");
        return username != null;
    }

    public static User loadFromJwt(String jwt) throws UserDoesNotExistException, JwtException {
        final JWTVerifier verifier = new JWTVerifier(User.SECRET_KEY);
        final Map<String, Object> claims;

        try {
            claims = verifier.verify(jwt);
        } catch (Exception e) {
            throw new JwtException("There was a problem verifying that JWT", e);
        }

        String username = (String) claims.get("username");
        return (username != null) ? User.load(username) : null;
    }

    public static boolean isUsernameAvailable(String username) {
        try {
            User user = User.load(username);
            return (user == null);
        } catch (UserDoesNotExistException e) {
            return true;
        }
    }

    public void registerForCourse(Course course) throws CourseRegistrationException, DatabaseException, CourseValidationException {
        if (!Course.exists(course.courseName))
            throw new CourseRegistrationException("The course '" + course.courseName + "' does not exist!");
        this.courses.add(course);
        //Course.load(courseName).registerUser(this.username);
        this.save();
    }

    public void unregisterForCourse(String courseName) throws DatabaseException {
        this.courses.remove(courseName);
        this.save();
    }

    public String toString() {
        return "<"+this.username+"> "+ this.serialize().toJson();
    }


    @Override
    public Document serialize() {
        Document doc = new Document("username", this.username)
                .append("firstName", this.firstName)
                .append("lastName", this.lastName)
                .append("courses", Course.toDocList(this.courses))
                .append("hashedPassword", this.hashedPassword)
                .append("email", this.email)
                .append("jwt", this.jwt);
        return doc;
    }

    @Override
    public User deserialize(Document doc) {
        this.username = doc.getString("username");
        this.firstName = doc.getString("firstName");
        this.lastName = doc.getString("lastName");
        this.email = doc.getString("email");
        this.hashedPassword = doc.getString("hashedPassword");

        if (doc.containsKey("password")) {
            String textPass = doc.getString("password");
            this.hashedPassword = BCrypt.hashpw(textPass, BCrypt.gensalt(12));
        }

        System.out.println("LOADING DOC: " + doc.toJson());


        ArrayList<Document> courseDocs = doc.get("courses", ArrayList.class);
        if (courseDocs != null && courseDocs.size() > 0) {
            this.courses = Course.toCourseList(courseDocs);
        } else {
            this.courses = new ArrayList<>();
        }

        return this;
    }

}

