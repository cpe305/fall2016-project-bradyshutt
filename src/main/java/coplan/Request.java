package coplan;

import org.bson.Document;

import static org.bson.Document.parse;

public class Request {

  private Document reqDoc;
  private Document data;
  private String subsystem;
  private String action;

  public Request(Document req) {
    this.reqDoc = req;
  }

  public Request(String req) {
    this.reqDoc = parse(req);
  }

  public String getSubsystem() {
    if (this.subsystem == null)
      this.subsystem = (String) this.reqDoc.get("subsystem");
    return this.subsystem;
  }

  public String getAction() {
    if (this.action == null)
      this.action = (String) this.reqDoc.get("action");
    return this.action;
  }

  public Document getData() {
    if (this.data == null)
      this.data = (Document) this.reqDoc.get("data");
    return this.data;
  }

}
