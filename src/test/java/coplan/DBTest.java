package coplan;

import com.mongodb.client.MongoCollection;
import junit.framework.TestCase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class DBTest extends TestCase {

   public void testGetInstance() throws Exception {
      DB db1 = DB.getInstance();
      DB db2 = DB.getInstance();
      assertEquals(db1, db2);
   }

   public void testCollectionAccess1() throws Exception {
      DB db = DB.getInstance();
      MongoCollection<Document> users = db.getCollection("test-collection");

      Document doc = new Document("username", "bshutt")
              .append("passwordHash", "bad-password")
              .append("firstName", "Brady")
              .append("lastName", "Shutt");

      users.insertOne(doc);
      Document readDoc = users.find(eq("username", "bshutt")).first();
      assertEquals(doc.toJson(), readDoc.toJson());

      users.deleteOne(eq("username","bshutt"));
   }

   public void testGetDocument() throws Exception {
      DB db = DB.getInstance();
      MongoCollection<Document> users = db.getCollection("test-collection");

      Document doc = new Document("username", "bshutt")
              .append("passwordHash", "bad-password")
              .append("firstName", "Brady")
              .append("lastName", "Shutt");

      users.insertOne(doc);
      Document readDoc = DB.getDocument("test-collection", "username", "bshutt");
      assertEquals(doc.toJson(), readDoc.toJson());
      users.deleteOne(eq("username","bshutt"));
   }

   public void testSetPort() throws Exception {

   }

   public void testSetUrl() throws Exception {

   }

}