package bshutt.coplan;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

class Database {

  private static int port = 27017;
  private static String url = "localhost";
  private static String dbName = "mydb";
  private static Database instance = new Database();

  public MongoClient mongo;
  public MongoDatabase db;

  private Database() {
    this.mongo = new MongoClient(Database.url, Database.port);
    this.db = this.mongo.getDatabase(Database.dbName);
  }

  public static Database getInstance() {
    return Database.instance;
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
