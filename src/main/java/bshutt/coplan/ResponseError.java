package bshutt.coplan;

public class ResponseError extends Response {

  public ResponseError(String error) {
    super();
    this.include("Error", error);
    this.end();
  }

}
