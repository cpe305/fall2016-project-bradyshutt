package coplan;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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