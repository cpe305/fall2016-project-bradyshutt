package bshutt.coplan.models;

import static com.mongodb.client.model.Filters.eq;

import bshutt.coplan.Database;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;


public class User {

  private static UserModel model = new UserModel();

  public String username;
  public String passwordHash;
  public String firstName;
  public String lastName;

  public User(String username) {
    this.username = username;
  }

  /**
   * Log the user in.
   *
   * @param password attempted password
   * @return True or false to whether the user logged in
   */
  public boolean login(String password) {

    User.model.getUser();
        Document user = Database.getInstance().getDocument("users", "username", this.username);
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
    if (this.model.usernameExists(this.username)) {
      this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
      this.create();
      return true;
    }
    return false;
  }

  public static boolean usernameIsAvailable(String username) {
    Document user = Database.getInstance()
        .getCollection("users")
        .find(eq("username", username))
        .first();
    return (user == null);
  }

  public boolean create() {
    MongoCollection<Document> users = Database.getInstance().getCollection("users");
    Document doc = new Document("username", this.username)
        .append("passwordHash", this.passwordHash)
        .append("firstName", this.firstName)
        .append("lastName", this.lastName);
    users.insertOne(doc);
    return true;
  }
}
