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
  public void testDBRunning() throws Exception {
    Database db = Database.getInstance();
    assertNotNull(db);
    assertNotNull(db.getClient());
    assertNotNull(db.getDB());
  }
}