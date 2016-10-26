package coplan;

import junit.framework.TestCase;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class UserActionsTest {

   @BeforeClass
   public static void registerUser() throws Exception {
      User user = new User("bshutt");
      assertTrue(user.register("password", "Brady", "Shutt"));
      assertEquals("Brady", user.firstName);
      assertEquals("Shutt", user.lastName);
   }

   @Test
   public void testUserIsSaved() throws Exception {
//      User usr = new User("bshutt");
//      assertTrue(usr.register("password", "Brady", "Shutt"));
//      System.out.println("FUFEOEOJjkl;");
//      assertEquals("Brady", usr.firstName);
//      assertEquals("Shutt", usr.lastName);

      Database db = Database.getInstance();
      assertNotNull(db);

      Document user = db.getDocument("users", "username", "bshutt");
      assertNotNull(user);
      assertEquals("bshutt", (String) user.get("username"));
      assertEquals("Brady", (String) user.get("firstName"));
      assertEquals("Shutt", (String) user.get("lastName"));
      assertNotNull((String) user.get("passwordHash"));
   }

   @Test
   public void testLoginCorrect() throws Exception {
      User user = new User("bshutt");
      assertTrue(user.login("password"));
      assertEquals("Brady", user.firstName);
      assertEquals("Shutt", user.lastName);
   }

   @Test
   public void testLoginIncorrect() throws Exception {
      User user = new User("bshutt");
      assertFalse(user.login("wrongPassword"));
      assertNull(user.firstName);
      assertNull(user.lastName);
   }
   @AfterClass
   public static void dropDatabase() throws Exception {
      Database.getInstance()._db.drop();
   }
}
