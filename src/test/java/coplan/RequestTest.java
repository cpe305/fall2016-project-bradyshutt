package coplan;


import static org.junit.Assert.assertEquals;

import org.bson.Document;
import org.junit.Test;


public class RequestTest {

  @Test
  public void testRequest() {
    String json = new Document()
        .append("subsystem", "user")
        .append("action", "getUser")
        .append("data", new Document()
            .append("username", "bshutt"))
        .toJson();

    Request req = new Request(json);

    System.out.println("Subsystem: " + req.getSubsystem());
    System.out.println("Action: " + req.getAction());
    System.out.println("Data: " + req.getData().toString());

    assertEquals("user", req.getSubsystem());
    assertEquals("getUser", req.getAction());
    assertEquals("bshutt", req.getData().getString("username"));

  }
}
