package coplan;

import junit.framework.TestCase;

import static com.mongodb.client.model.Filters.eq;

public class UserTest extends TestCase {

   public void testLogin() throws Exception {

   }

   public void testRegisterThenLogin() throws Exception {
      // Register
      User user1 = new User("_bshutt");
      assertEquals(true, user1.register("badPassword", "Brady", "Shutt"));
      user1.create();

      // Login
      User user2 = new User("_bshutt");
      assertEquals(true, user2.login("badPassword"));
      assertEquals(user1.username, user2.username);
      assertEquals(user1.firstName, user2.firstName);
      assertEquals(user1.lastName, user2.lastName);

      DB.getCollection("users").drop();
   }

   public void testUsernameIsAvailable() throws Exception {

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