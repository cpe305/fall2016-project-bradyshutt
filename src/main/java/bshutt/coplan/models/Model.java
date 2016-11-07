package bshutt.coplan.models;

import bshutt.coplan.Database;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.logging.Level;
import java.util.logging.Logger;

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

  public FindIterable<Document> read(String collectionName, Bson filter) {
    FindIterable<Document> cur = this.getCollection(collectionName).find(filter);
    return cur;
  }

  public FindIterable<Document> read(MongoCollection collection, Bson filter) {
    FindIterable<Document> cur = collection.find(filter);
    return cur;
  }

  public Document readOne(MongoCollection collection, Bson filter) {
    return this.read(collection, filter).first();
  }

  public Document readOne(String collectionName, Bson filter) {
    return this.read(this.getCollection(collectionName), filter).first();
  }


  public void update(MongoCollection collection, Document filter, Document updated) {
    collection.updateOne(filter, updated);
  }

  public void delete(MongoCollection collection, Document filter) {
    collection.deleteOne(filter);
  }

}
