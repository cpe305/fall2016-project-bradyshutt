package bshutt.coplan;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static int port = 27017;
    private static String url = "localhost";
    private static String dbName = "mydb";
    private static Database instance = new Database();

    public MongoClient client;
    public MongoDatabase db;

    private Database() {
        System.setProperty("DEBUG.MONGO", "true");
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        this.client = new MongoClient(Database.url, Database.port);
        this.db = this.client.getDatabase(Database.dbName);
        this.db.getCollection("users").createIndex(
                new Document("username", 1),
                new IndexOptions().unique(true));
        this.db.getCollection("courses").createIndex(
                new Document("courseName", 1),
                new IndexOptions().unique(true));
    }

    public static Database getInstance() {
        return Database.instance;
    }

    public Database changeDatabase(String dbName) {
        this.db = this.client.getDatabase(dbName);
        return this;
    }

    public MongoCollection<Document> col(String colName) throws DBException {
        MongoCollection<Document> collection = this.db.getCollection(colName);
        if (collection == null)
            throw new DBException("Collection '" + colName + "' not found!");
        else
            return collection;
    }

    public Bson filter(String key, String val) {
        return Filters.eq(key, val);
    }


}

