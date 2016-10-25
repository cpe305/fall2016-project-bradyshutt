package coplan;

import junit.framework.TestCase;

/**
 * Created by brady on 10/23/16.
 */
public class JSONFormatterTest extends TestCase {
   public void testSet() throws Exception {
      JSONFormatter jf = new JSONFormatter();
      jf.set("name", "Brady Shutt");
      jf.set("age", "21");
      jf.set("major", "Computer Science");
      String formatted = jf.format();
      assertEquals(formatted, "{\"major\":\"Computer Science\",\"name\":\"Brady Shutt\",\"age\":\"21\"}");
   }

   public void testFormat() throws Exception {

   }

}