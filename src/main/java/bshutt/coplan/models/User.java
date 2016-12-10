package bshutt.coplan.models;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.bson.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private String hashedPassword;


    public User() {
        super(collectionName, filterKey);
    }

    public String getJwt() {
        return this.jwt;
    }

    /**
     * Log in
     * @param username Users username
     * @param password Users plain-text password
     * @return A User instance
     * @throws UserDoesNotExistException
     */
    public static User login(String username, String password) throws UserDoesNotExistException {
        User user = User.load(username);
        if (user.authenticate(password))
            return user;
        else
            throw new InvalidPasswordException();
    }

    /**
     * Load a user from the database
     * @param username Username of user to load
     * @return A User instance
     * @throws UserDoesNotExistException
     */
    public static User load(String username) throws UserDoesNotExistException {
        User user = new User();
        Document rawDocument = user.loadModel(username);
        if (rawDocument == null) {
            throw new UserDoesNotExistException("User that does not exist: '"+username+"'");
        }
        return user.deserialize(rawDocument);
    }

    /**
     * Validate a users attributes
     * @param doc A users attributes
     * @return whether or not they're valid
     */
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

    /**
     * @return The username, for use by parent class
     */
    @Override
    public String getFilterValue() {
        return this.username;
    }

    /**
     * Generates a document to send to the client
     * @return the document
     */
    public Document toClientDoc() {
        Document doc = this.serialize();
        doc.remove("hashedPassword");
        doc.remove("jwt");
        return doc;
    }


    /**
     * Replaces the list of course names with actual course objects
     * @param doc a document containing the list of course names
     * @return A document containing the actual course objects
     * @throws DatabaseException
     * @throws CourseValidationException
     */
    public Document replaceNamesWithDetails(Document doc) throws DatabaseException, CourseValidationException {
        ArrayList<Document> courses = new ArrayList<Document>();
        for (String courseName : doc.get("courses", String[].class)) {
            Course course = Course.load(courseName);
            courses.add(course.toClientDoc());
        }
        doc.put("courses", courses.toArray());
        return doc;
    }

    /**
     * Validate users password
     * @param password Users password
     * @return
     */
    public boolean authenticate(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

    /**
     * Generate a JWT for the user
     * @return the JWT
     * @throws DatabaseException
     */
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

    /**
     * Checks if the JWT is valid
     * @param jwt the JWT
     * @return Whether or not it's been tampered with
     * @throws JwtException
     */
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

    /**
     * Loads the user given just the JWT
     * @param jwt the Users JWT
     * @return a new User instance
     * @throws UserDoesNotExistException
     * @throws JwtException
     */
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

    /**
     * Checks if the username is available
     * @param username the Username to test
     * @return whether or not it's available
     */
    public static boolean isUsernameAvailable(String username) {
        try {
            User user = User.load(username);
            return (user == null);
        } catch (UserDoesNotExistException e) {
            return true;
        }
    }

    /**
     * Registers the user to the course
     * @param course the course to register to
     * @throws CourseRegistrationException
     * @throws DatabaseException
     * @throws CourseValidationException
     */
    public void registerForCourse(Course course) throws CourseRegistrationException, DatabaseException, CourseValidationException {
        if (!Course.exists(course.courseName))
            throw new CourseRegistrationException("The course '" + course.courseName + "' does not exist!");
        this.courses.add(course);
        this.save();
    }

    /**
     * Removes the user from the course
     * @param courseName the name of the course to unregister from
     * @throws DatabaseException
     */
    public void unregisterForCourse(String courseName) throws DatabaseException {
        this.courses.remove(courseName);
        this.save();
    }

    /**
     * @return String
     */
    public String toString() {
        return "<"+this.username+"> "+ this.serialize().toJson();
    }


    /**
     * Serializes the user into a Document format for sending to/from the database/client
     * @return the document
     */
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

    /**
     * "loads" a user from a given document
     * @param doc the document in need of deserialization
     * @return the User instance
     */
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

        ArrayList<Document> courseDocs = doc.get("courses", ArrayList.class);
        if (courseDocs != null && courseDocs.size() > 0) {
            this.courses = Course.toCourseList(courseDocs);
        } else {
            this.courses = new ArrayList<>();
        }
        return this;
    }

}

