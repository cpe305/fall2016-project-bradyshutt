package coplan;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Crud {

  protected DatabaseModel dbModel;
  protected MongoCollection model;

  void create(Document doc) {
    this.model.insertOne(doc);
  }

  Document read(Document query) {
    return (Document) this.model.find(query).first();
  }

  void update(Document query, Document updated) {
    this.model.updateOne(query, updated);
  }

  void destroy(Document query) {
    this.model.deleteOne(query);
  }
}
