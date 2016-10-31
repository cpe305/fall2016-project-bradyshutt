package bshutt.coplan.models;

import bshutt.coplan.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

public abstract class Model {

  protected Database mongo;

  public Model() {
    this.mongo = Database.getInstance();
  }

  public MongoCollection getCollection(String collection) {
    return this.mongo.db.getCollection(collection);
  }

  public Bson getFilter(String key, String value) {
    return Filters.eq(key, value);
  }

  public void create(MongoCollection collection, Document doc) {
    collection.insertOne(doc);
  }

  public FindIterable<Document> read(MongoCollection collection, Bson filter) {
    FindIterable<Document> cur = collection.find(filter);
    return cur;
  }

  public Document readOne(MongoCollection collection, Bson filter) {
    return this.read(collection, filter).first();
  }


  public abstract Document update(Document query, Document updated);
  public abstract boolean delete(Document query);

}
