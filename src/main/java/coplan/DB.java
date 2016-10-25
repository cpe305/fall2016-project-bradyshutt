package coplan;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class DB {

   private static int port = 27017;
   private static String url = "localhost";
   private static String dbName = "mydb";
   private static DB instance = null;

   private MongoClient mongo;
   private MongoDatabase db;

   private DB() {
      this.mongo = new MongoClient(DB.url, DB.port);
      this.db = this.mongo.getDatabase(DB.dbName);
   }

   public static DB getInstance() {
      if (DB.instance == null) {
         DB.instance = new DB();
         return DB.instance;
      }
      else
         return DB.instance;
   }

   public static MongoCollection<Document> getCollection(String collection) {
      return DB.getInstance().db.getCollection(collection);
   }

   public static Document getDocument(String collection, String queryKey, String queryVal) {
      MongoCollection<Document> col = getCollection(collection);
      return col.find(eq(queryKey, queryVal)).first();

   }

   public static void setPort(int port){
      DB.port = port;
   }

   public void setUrl(String url) {
      DB.url = url;
   }

}
