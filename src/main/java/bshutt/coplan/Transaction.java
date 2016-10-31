package bshutt.coplan;

// Template Method
public abstract class Transaction {

  public Request req;
  public Response res;

  public Transaction() {

  }

  public Response doTransaction() {
    this.req = this.readRequest();

    if (this.needsAuthentication(this.req)) {
      if (!this.authenticate(this.req)) {
        return new ResponseError("Authentication failure.");
      }
    }

    this.res = this.setResponse(this.req);
    return this.res;

  }

  public abstract Request readRequest();

  public abstract boolean needsAuthentication(Request req);

  public abstract boolean authenticate(Request req);

  public abstract Response setResponse(Request req);


}
