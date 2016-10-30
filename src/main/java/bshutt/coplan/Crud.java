package bshutt.coplan;

public interface Crud {
  boolean create();

  Object read();

  boolean update(Object obj);

  boolean destroy();
}
