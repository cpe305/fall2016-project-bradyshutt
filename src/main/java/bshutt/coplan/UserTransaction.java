package bshutt.coplan;

public abstract class UserTransaction extends Transaction {

  @Override
  public Request readRequest() {
    return null;
  }

  @Override
  public boolean needsAuthentication(Request req) {
    return false;
  }

  @Override
  public boolean authenticate(Request req) {
    return false;
  }
}
