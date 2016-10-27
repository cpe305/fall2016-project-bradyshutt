package coplan;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

public class User {
  Document attributes;
  String username;
  String passwordHash;
  String firstName;
  String lastName;

  public User(String cusername) {
    this.attributes.append("username", username);
  }

  /**
   * Registers the user then adds to database.
   *
   * @param password users password.
   * @param firstName first name of the user.
   * @param lastName last name of the user.
   * @return true/false if registration was successful
   */
  public boolean register(String password, String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    if (User.usernameIsAvailable(this.username)) {
      this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
      this.create();
      return true;
    }
    return false;
  }

  /**
   * Log the user in.
   *
   * @param password attempted password
   * @return True or false to whether the user logged in
   */
  public boolean login(String password) {
    String username = this.attributes.getString("username");
    Document user = .getDocument("users", "username", username);
    if (user == null) {
      System.out.println("err");
      return false;
    }

    String hashed = (String) user.get("passwordHash");
    if (BCrypt.checkpw(password, hashed)) {
      this.firstName = user.getString("firstName");
      this.lastName = user.getString("lastName");
      return true;
    } else {
      return false;
    }
  }


  /**
   * Create user.
   *
   * @return whether or not it was successful.
   */
  public boolean create() {
    MongoCollection<Document> users = Database.getInstance().getCollection("users");
    Document doc = new Document("username", this.username)
        .append("passwordHash", this.passwordHash)
        .append("firstName", this.firstName)
        .append("lastName", this.lastName);
    users.insertOne(doc);
    return true;
  }

  public Object read() {
    return null;
  }

  public boolean update(Object update) {
    return false;
  }

  public boolean destroy() {
    return false;
  }
}
