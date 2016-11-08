package bshutt.coplan.models;

import org.bson.Document;

public class UserModel extends Model {

  private static UserModel instance = null;

  private UserModel() { }

  public static UserModel getInstance() {
    if (UserModel.instance == null)
      UserModel.instance = new UserModel();
    return UserModel.instance;
  }

  public Document getUser(String username) {
    Document user = this.readOne("users", this.getFilter("username", username));
    return user;
  }

  public boolean usernameExists(String username) {
    Document user = this.readOne(
        this.getCollection("users"),
        this.getFilter("username", username));
    return (user == null);
  }

  public User loadAttributes(User user) {
   // Document userDoc = this.getUser((String) user.get("username"));
    //userDoc.forEach(user::set);
    return user;
  }

  public void create(User user) {
    //if (usernameExists((String) user.get("username")))
    //  throw new Error("That username already exists");
    //super.create(this.getCollection("users"), user.getAttributes());
  }


}



