package coplan;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DatabaseModel {

  private static int port = 27017;
  private static String url = "localhost";
  private static String dbName = "maindb";
  private static DatabaseModel instance = new DatabaseModel();

  public MongoClient mongo;
  public MongoDatabase db;

  protected DatabaseModel() {
    this.mongo = new MongoClient(DatabaseModel.url, DatabaseModel.port);
    this.db = this.mongo.getDatabase(DatabaseModel.dbName);
  }

  public static DatabaseModel getInstance() {
    return DatabaseModel.instance;
  }

  public void useDatabase(String newName) {
    DatabaseModel.dbName = newName;
    this.db = this.mongo.getDatabase(DatabaseModel.dbName);
  }

}
