package bshutt.coplan.models;

import bshutt.coplan.Database;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User extends Model<User> {
    private static final String collectionName = "users";
    private static final String filterKey = "username";
    private static final String SECRET_KEY= "123j3kkk89dfd73kk7zh";
    private static final String ISSUER = "http://coplan.bshutt.com/";

    private String username;
    private ArrayList<String> courses;
    private String firstName;
    private String lastName;
    private String email;
    private String jwt;
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
    public String getJwt() {
        return this.jwt;
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

    public String giveJwt() throws Exception {
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

    public static boolean validateJwt(String jwt) throws Exception {
        try {
            final JWTVerifier verifier = new JWTVerifier(User.SECRET_KEY);
            final Map<String, Object> claims = verifier.verify(jwt);
            String username = (String) claims.get("username");
            return username != null;
        } catch (JWTVerifyException exc) {
            return false;
        }
    }

    public static User loadFromJwt(String jwt) {
        try {
            final JWTVerifier verifier = new JWTVerifier(User.SECRET_KEY);
            final Map<String, Object> claims = verifier.verify(jwt);
            String username = (String) claims.get("username");
            if (username != null)
                return User.load(username);
            else
                return null;
        } catch (Exception exc) {
            return null;
        }
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

