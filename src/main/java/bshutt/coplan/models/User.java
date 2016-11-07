package bshutt.coplan.models;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

public class User {

  private UserModel model = UserModel.getInstance();
  private Document attributes;
  private boolean isLoggedIn;

  public User() {
    this.attributes = new Document();
    this.isLoggedIn = false;
  }

  public User(String username) {
    this.attributes = new Document();
    this.attributes.append("username", username);
    this.isLoggedIn = false;
  }

  public static User loadUser(String username) {
    User user = new User();
    return UserModel.getInstance().loadAttributes(user);
  }

  public void set(String key, Object val) {
    this.attributes.append(key, val);
  }

  public Object get(String key) {
    return this.attributes.get(key);
  }

  public String toString() {
    return "User: \n" + this.attributes.toJson();
  }

  /**
   * Log the user in.
   *
   * @param password attempted password
   * @return True or false to whether the user logged in
   */
  public boolean login(String password) {
    this.model.loadAttributes(this);
    String hashed = (String) this.get("passwordHash");
    if (BCrypt.checkpw(password, hashed)) {
      this.isLoggedIn = true;
      return true;
    }
    return false;
  }

  /**
   * Registers the user then adds to database.
   *
   * @param password users password.
   * @param firstName first name of the user.
   * @param lastName last name of the user.
   * @return true/false if registration was successful
   */
  public User register(String password, String firstName, String lastName) {
    this.set("firstName", firstName);
    this.set("lastName", lastName);
    if (this.model.usernameExists((String) this.get("username"))) {
      String passwordHash  = BCrypt.hashpw(password, BCrypt.gensalt());
      this.set("passwordHash", passwordHash);
      this.model.create(this);
      return this;
    }
    return this;
  }

  public Document getAttributes() {
    return this.attributes;
  }


}
