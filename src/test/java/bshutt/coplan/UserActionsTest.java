package bshutt.coplan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserActionsTest {

//  /**
//  * Test if user can register.
//  */
//  @BeforeClass
//  public static void registerUser() {
//    User user = new User("bshutt");
//    assertTrue(user.register("password", "Brady", "Shutt"));
//    assertEquals("Brady", user.firstName);
//    assertEquals("Shutt", user.lastName);
//  }
//
//  @Test
//  public void testUserIsSaved() throws Exception {
//    //      User usr = new User("bshutt");
//    //      assertTrue(usr.register("password", "Brady", "Shutt"));
//    //      System.out.println("FUFEOEOJjkl;");
//    //      assertEquals("Brady", usr.firstName);
//    //      assertEquals("Shutt", usr.lastName);
//
//    Database db = Database.getInstance();
//    assertNotNull(db);
//
//    Document user = db.getDocument("users", "username", "bshutt");
//    assertNotNull(user);
//    assertEquals("bshutt", (String) user.getData("username"));
//    assertEquals("Brady", (String) user.getData("firstName"));
//    assertEquals("Shutt", (String) user.getData("lastName"));
//    assertNotNull((String) user.getData("passwordHash"));
//  }
//
//  @Test
//  public void testLoginCorrect() throws Exception {
//    User user = new User("bshutt");
//    assertTrue(user.login("password"));
//    assertEquals("Brady", user.firstName);
//    assertEquals("Shutt", user.lastName);
//  }
//
//  @Test
//  public void testLoginIncorrect() throws Exception {
//    User user = new User("bshutt");
//    assertFalse(user.login("wrongPassword"));
//    assertNull(user.firstName);
//    assertNull(user.lastName);
//  }
//
//  @AfterClass
//  public static void dropDatabase() throws Exception {
//    Database.getInstance().db.drop();
//  }
}
