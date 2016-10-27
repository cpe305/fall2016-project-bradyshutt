package coplan;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import org.bson.Document;


class UserModel extends DatabaseModel {

  private static UserModel instance = new UserModel();

  private UserModel() {
    this.dbModel = DatabaseModel.getInstance();
    this.model = dbm.db.getCollection("users");
  }

  public static UserModel getInstance() {
    return this.instance;
  }
}

  public void createUser(User user) {
    Document doc = user.attributes;
    this.create(user);
  }

  public boolean usernameIsAvailable(String username) {
    Document user =
        (Document) this.model.find(eq("username", username)).first();
    return (user == null);
  }

