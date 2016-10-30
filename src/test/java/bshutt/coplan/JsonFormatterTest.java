package bshutt.coplan;

import static org.junit.Assert.assertEquals;

import bshutt.coplan.JsonFormatter;
import org.junit.Test;

public class JsonFormatterTest {
  @Test
  public void testSet() throws Exception {
    JsonFormatter jf = new JsonFormatter();
    jf.set("name", "Brady Shutt");
    jf.set("age", "21");
    jf.set("major", "Computer Science");
    //String formatted = jf.format();
    //assertEquals(formatted, "{\"major\":\"Computer Science\",\"name\":\"Brady Shutt\","
    //    + "\"age\":\"21\"}");
  }

  @Test
  public void testFormat() throws Exception {

  }

}