package bshutt.coplan;

import static com.mongodb.client.model.Filters.eq;

import bshutt.coplan.Database;
import com.mongodb.client.MongoCollection;
import junit.framework.TestCase;
import org.bson.Document;
import org.junit.Test;

public class DatabaseTest extends TestCase {

  @Test
  public void testGetInstance() throws Exception {
    Database database1 = Database.getInstance();
    Database database2 = Database.getInstance();
    assertEquals(database1, database2);
  }

  @Test
  public void testDocInsertionDeletion1() throws Exception {
    Database database = Database.getInstance();
    MongoCollection<Document> users = database.getCollection("test-collection");

    Document doc = new Document("username", "bshutt")
        .append("passwordHash", "bad-password")
        .append("firstName", "Brady")
        .append("lastName", "Shutt");

    users.insertOne(doc);
    Document readDoc = users.find(eq("username", "bshutt")).first();
    assertEquals(doc.toJson(), readDoc.toJson());

    users.deleteOne(eq("username", "bshutt"));
  }

  @Test
  public void testGetDocument() throws Exception {
    Database database = Database.getInstance();
    MongoCollection<Document> users = database.getCollection("test-collection");

    Document doc = new Document("username", "bshutt")
        .append("passwordHash", "bad-password")
        .append("firstName", "Brady")
        .append("lastName", "Shutt");

    users.insertOne(doc);
    Document readDoc = Database.getInstance().getDocument("test-collection", "username", "bshutt");
    assertEquals(doc.toJson(), readDoc.toJson());
    users.deleteOne(eq("username", "bshutt"));
  }

  public void testSetPort() throws Exception {

  }

  public void testSetUrl() throws Exception {

  }

}