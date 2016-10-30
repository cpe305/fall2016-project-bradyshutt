package bshutt.coplan;

import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseTest {

  @Test
  public void testOnlyStrings() throws Exception {
    Response res = new Response();
    res.include("name", "brady");
    res.include("age", "21");
    res.include("acceptedPhilosophicalTheoryOfMorality", "Utilitarian");
    Document result = res.end();
    assertEquals("brady", result.getString("name"));
    assertEquals("21", result.getString("age"));
    assertEquals("Utilitarian", result.getString("acceptedPhilosophicalTheoryOfMorality"));
  }

  @Test
  public void testHasList() throws Exception {
    List<String> friends = new ArrayList<>();
    friends.add("jackson");
    friends.add("kyle");
    friends.add("pradeep");
    friends.add("alex");
    friends.add("alex");

    Response res = new Response()
        .include("name", "brady")
        .include("roommates", friends);

    Document result = res.end();
    assertEquals("brady", result.getString("name"));
    assertEquals(friends.toString(), result.get("roommates").toString());
  }

}