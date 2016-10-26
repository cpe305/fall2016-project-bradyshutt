package coplan;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import static com.mongodb.client.model.Filters.eq;

public class User implements CRUD {
   String username;
   String passwordHash;
   String firstName;
   String lastName;

   public User(String username) {
      this.username = username;
   }

   public boolean login(String password) throws Error {
      Document user = Database.getInstance().getDocument("users", "username", this.username);
      if (user == null) {
         System.out.println("err");
         return false;
      }

      String hashed = (String) user.get("passwordHash");
      if (BCrypt.checkpw(password, hashed)) {
         this.firstName = user.getString("firstName");
         this.lastName = user.getString("lastName");
         return true;
      }
      else
         return false;
   }

   public boolean register(String password, String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
      if (User.usernameIsAvailable(this.username)) {
         this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
         this.create();
         return true;
      }
      return false;
   }

   public static boolean usernameIsAvailable(String username) {
      Document user = Database.getInstance()
               .getCollection("users")
               .find(eq("username", username))
               .first();
      return (user == null);
   }

   public boolean create() {
      MongoCollection<Document> users = Database.getInstance().getCollection("users");
      Document doc = new Document("username", this.username)
              .append("passwordHash", this.passwordHash)
              .append("firstName", this.firstName)
              .append("lastName", this.lastName);
      users.insertOne(doc);
      return true;
   }

   public Object read() {
      return null;
   }

   public boolean update(Object update) {
      return false;
   }

   public boolean destroy() {
      return false;
   }
}
