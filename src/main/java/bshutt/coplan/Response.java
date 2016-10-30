package bshutt.coplan;

import org.bson.Document;

public class Response {

  private String response;
  private Document doc;
  private boolean isDone = false;

  public Response() {
    this.doc = new Document();
  }

  public Response include(String key, Object obj) {
    this.doc.append(key, obj);
    return this;
  }

  public Document end() {
    if (this.isDone) {
      throw new Error("This response has already finished. You can't end it again.");
    } else {
      System.out.println(doc.toJson());
      this.isDone = true;
      return doc;
    }
  }



}
