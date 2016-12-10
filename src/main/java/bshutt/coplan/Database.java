package bshutt.coplan;

import bshutt.coplan.exceptions.DatabaseException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class Database {

    private static int port = 27017;
    private static String url = "localhost";
    private static String dbName = "mydb";
    private static Database instance = new Database();

    private MongoClient client;
    private MongoDatabase db;

    private Database() {
        ServerAddress address = new ServerAddress(Database.url, Database.port);
        this.setLogs();
        this.client = new MongoClient(address);
        this.db = this.client.getDatabase(Database.dbName);
        this.setIndexes();
    }

    private void setLogs() {
        System.setProperty("DEBUG.MONGO", "true");
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
    }

    public static Database getInstance() {
        return Database.instance;
    }

    public Database changeDatabase(String dbName) {
        this.db = this.client.getDatabase(dbName);
        this.setIndexes();
        return this;
    }

    public MongoDatabase getDB() {
        return this.db;
    }

    public MongoClient getClient() {
        return this.client;
    }

    private void setIndexes() {
        this.db.getCollection("users").createIndex(
                new Document("username", 1),
                new IndexOptions().unique(true));
        this.db.getCollection("courses").createIndex(
                new Document("courseName", 1),
                new IndexOptions().unique(true));
    }

    public MongoCollection<Document> col(String colName) throws DatabaseException {
        MongoCollection<Document> collection = this.db.getCollection(colName);
        if (collection == null)
            throw new DatabaseException("Collection '" + colName + "' not found!");
        else
            return collection;
    }

    public Bson filter(String key, String val) {
        return Filters.eq(key, val);
    }

}

