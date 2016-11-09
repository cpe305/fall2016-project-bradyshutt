package bshutt.coplan.models;

import bshutt.coplan.DBException;
import bshutt.coplan.Database;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class User extends Model<User>{

    private Database db = Database.getInstance();

    public Document attributes;
    public String username;
    public ObjectId userID;
    public ArrayList<ObjectId> courses;
    public boolean existsInDB;

    public User() {
        this.courses = new ArrayList<ObjectId>();
        this.attributes = new Document();
        this.existsInDB = false;
    }

    public User load(String username) throws DBException {
        Document userDoc = this.db
                .col("users")
                .find(this.db.filter("username", username))
                .first();
        if (userDoc == null) {
            return null;
        } else {
            this.attributes = userDoc;
            this.username = userDoc.getString("username");
            this.userID = userDoc.get("_id", ObjectId.class);
            this.existsInDB = true;
            return this;
        }
    }

    public String get(String key) {
        return this.attributes.getString(key);
    }

    public <T> T get(String key, Class<T> type) {
        return this.attributes.get(key, type);
    }

    public void set(String key, Object value) {
        if (this.attributes.containsKey(key))
            this.attributes.replace(key, value);
        else
            this.attributes.append(key, value);
    }

    public User build(Document userDoc) {
        this.attributes.putAll(userDoc);
        this.username = userDoc.getString("username");
        return this;
    }

    public Document getAttributes() {
        return this.attributes;
    }

    public void save() throws DBException {
        if (this.existsInDB) {
            this.db.col("users").findOneAndReplace(
                    this.db.filter("username", this.username),
                    this.attributes);
        } else {
            this.db.col("users").insertOne(this.attributes);
            this.existsInDB = true;
        }
    }

    public boolean validate() throws DBException {
        return ( this.attributes.containsKey("username")
                && this.attributes.containsKey("firstName")
                && this.attributes.containsKey("lastName")
                && this.attributes.containsKey("password") );
    }

    public boolean authenticate(String password) throws DBException {
        User user = new User().load(username);
        return (user.get("password").equals(password));
    }

    public static boolean isUsernameAvailable(String username) {
        try {
            User user = new User().load(username);
            return (user == null);
        } catch (DBException e) {
            return false;
        }
    }

}
