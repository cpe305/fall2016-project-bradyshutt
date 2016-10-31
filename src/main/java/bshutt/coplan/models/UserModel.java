package bshutt.coplan.models;

import org.bson.Document;

public class UserModel extends Model {

  public void getUser() {

  }

  public boolean usernameExists(String username) {
    Document user = this.readOne(
        this.getCollection("users"),
        this.getFilter("username", username)
    );
    return (user == null);
  }

  @Override
  public Document update(Document query, Document updated) {
    return null;
  }

  @Override
  public boolean delete(Document query) {
    return false;
  }
}

