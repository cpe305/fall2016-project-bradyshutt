package bshutt.coplan;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

  private static int port = 27017;
  private static String url = "localhost";
  private static String dbName = "mydb";
  private static Database instance = new Database();

  public MongoClient client;
  public MongoDatabase db;

  private Database() {
    this.client = new MongoClient(Database.url, Database.port);
    this.db = this.client.getDatabase(Database.dbName);
  }

  public static Database getInstance() {
    return Database.instance;
  }

  public Database changeDatabase(String dbName) {
    this.db = this.client.getDatabase(dbName);
    return this;
  }

  public MongoCollection<Document> getCollection(String collection) {
    return Database.instance.db.getCollection(collection);
  }

  public Document getDocument(String collection, String queryKey, String queryVal) {
    MongoCollection<Document> coll = getCollection(collection);
    FindIterable<Document> cur = coll.find(eq(queryKey, queryVal));
    Document first = cur.first();
    System.out.println(first);
    return first;

  }


}
