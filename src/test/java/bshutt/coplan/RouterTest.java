package bshutt.coplan;

import bshutt.coplan.Request;
import bshutt.coplan.Router;
import org.bson.Document;
import org.junit.Test;

public class RouterTest {

  @Test
  public void testRouteA() {

    Router router = Router.getInstance();

    String json = new Document()
        .append("subsystem", "user")
        .append("action", "getUser")
        .append("data", new Document()
            .append("username", "bshutt"))
        .toJson();

    Request req = new Request(json);

    router.route(req);
  }

}