package bshutt.coplan;


import bshutt.coplan.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonParserTest {

  @Test
  public void TestJsonParser() {
    String testJson = "{\"firstName\": \"Amy\", \"lastName\":\"Lewis\",\"major\": \"Software Engineering\"}";

    JsonParser jp = new JsonParser(testJson);
    assertEquals("Amy",jp.get("firstName"));
    assertEquals("Lewis",jp.get("lastName"));
    assertEquals("Software Engineering",jp.get("major"));
  }

  @Test
  public void TestNonexistantKey() {
    String testJson = "{\"firstName\": \"Amy\", \"lastName\":\"Lewis\",\"major\": \"Software Engineering\"}";

    JsonParser jp = new JsonParser(testJson);
    assertNull(jp.get("favoriteColor"));
  }
}