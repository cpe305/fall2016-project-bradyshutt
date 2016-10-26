package coplan;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.crypto.Data;

import static org.junit.Assert.*;


public class UserTest {

   public void testLogin() throws Exception {

   }

   @Test
   public void testRegisterThenLogin() throws Exception {
      // Register
      User user1 = new User("bshutt");
      assertEquals(true, user1.register("password", "Brady", "Shutt"));
      user1.create();

      // Login
      User user2 = new User("bshutt");
      assertEquals(true, user2.login("password"));
      assertEquals(user1.username, user2.username);
      assertEquals(user1.firstName, user2.firstName);
      assertEquals(user1.lastName, user2.lastName);

      Database db = Database.getInstance();
      Database.getInstance().getCollection("users").drop();
   }

   @Test
   public void testUsernameIsAvailable() throws Exception {
      // Register
      User user1 = new User("bshutt");
      assertEquals(true, user1.register("password", "Brady", "Shutt"));
      user1.create();

      // Login
      assertFalse(User.usernameIsAvailable("bshutt"));
      assertTrue(User.usernameIsAvailable("valid___username"));

      Database db = Database.getInstance();
      Database.getInstance().getCollection("users").drop();


   }

   public void testCreate() throws Exception {

   }

   public void testRead() throws Exception {

   }

   public void testUpdate() throws Exception {

   }

   public void testDestroy() throws Exception {

   }

}