package bshutt.coplan;

import bshutt.coplan.models.User;
import bshutt.coplan.models.UserModel;
import org.bson.Document;

import static org.bson.Document.parse;

public class Request {

  public UserModel users = UserModel.getInstance();

  public Document data;
  public String route;
  public User user;

  public Request(Document req) {
    this.route = req.getString("route");
    this.data = (Document) req.get("data");
  }

  public Request(String route, Document data) {
    this.route = route;
    this.data = data;

  }

  public Request(String req) {
    Document reqDoc = parse(req);
    this.route = reqDoc.getString("route");
    this.data = (Document) reqDoc.get("data");
  }

  public String toString() {
    return
        "<REQ>:"
            + "\n\tRoute: " + this.route
            + "\n\tData: " + this.data.toJson();


  }

}
